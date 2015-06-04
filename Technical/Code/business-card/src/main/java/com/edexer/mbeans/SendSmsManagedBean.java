package com.edexer.mbeans;

import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.service.SmsConfigServiceManager;

@ManagedBean

public class SendSmsManagedBean {

	@ManagedProperty("#{smsConfigServiceManagerImpl}")
	SmsConfigServiceManager smsConfigService;

	private List<BusinessCard> listBCards;
	private User user;
	private String Subject;
	private String Body;

	public boolean disableSendSms() {

		if (((List<BusinessCard>) HttpSessionUtil.getSession().getAttribute(
				Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY)).size() > 0)
			return false;
		return true;

	}

	public void sendSms() {

		try {
			user = HttpSessionUtil.getUser();
			listBCards = (List<BusinessCard>) HttpSessionUtil.getSession()
					.getAttribute(
							Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);
			if (listBCards!= null) {
				smsConfigService.SendSms(Subject, Body, listBCards);
				System.out.println ("sent done  ");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println ("error in send SMS ");
		}
	}
	public void Clear() {
		user=new User();
		listBCards=new ArrayList<BusinessCard>();
		Subject="";
		Body="";
		//From="";
		FacesUtil.addInfoMessage("grehjry", "vrethr");
	}

	public SmsConfigServiceManager getSmsConfigService() {
		return smsConfigService;
	}

	public void setSmsConfigService(SmsConfigServiceManager smsConfigService) {
		this.smsConfigService = smsConfigService;
	}

	public List<BusinessCard> getListBCards() {
		return listBCards;
	}

	public void setListBCards(List<BusinessCard> listBCards) {
		this.listBCards = listBCards;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String body) {
		Body = body;
	}
}
