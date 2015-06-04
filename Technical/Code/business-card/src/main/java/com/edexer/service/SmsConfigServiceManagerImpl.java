package com.edexer.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.dao.BusinessCardEntityDaoImpl;
import com.edexer.dao.SendSMSDao;
import com.edexer.model.BusinessCard;
import com.edexer.model.Email;
import com.edexer.model.Mobile;
import com.edexer.model.User;

@Transactional
@Service
public class SmsConfigServiceManagerImpl implements SmsConfigServiceManager {

	@Autowired
	BusinessCardEntityDaoImpl BCentityDao;

	@Autowired
	SendSMSDao senddao;

	@Override
	public String SendSms(String Subject, String Body, List<BusinessCard> bCard) {

		try {
			for (int i = 0; i < bCard.size(); i++) {
				int busid = bCard.get(i).getBusinessCardId();
				BusinessCard BC = BCentityDao.get(busid, true);
			
				Iterator it = BC.getMobiles().iterator();
				List<String> list = new ArrayList<String>();
				List<String> listFinal=new ArrayList<String>();
				while (it.hasNext()) {
					Mobile mobilenumber = (Mobile) it.next();
					if (mobilenumber != null) {
						list.add(mobilenumber.getId().getMobileNum());
						listFinal.add(list.get(0));
					}
					System.out
							.println("message number:" + i + "sent succesful");
				}
				if (listFinal.size() > 0) {
					
					String[] x = {listFinal.get(0)};
					senddao.Sendaction(x, Subject, Body);
					System.out.println("sent donein service manager");
				}

			}
		} catch (Exception ex) {

			ex.printStackTrace();
			System.out.println("error in sending sms in service impl");

		}

		return null;
	}

	public BusinessCardEntityDaoImpl getBCentityDao() {
		return BCentityDao;
	}

	public void setBCentityDao(BusinessCardEntityDaoImpl bCentityDao) {
		BCentityDao = bCentityDao;
	}

}
