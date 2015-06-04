package com.edexer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edexer.dao.BcRequestStatusEntityDaoImpl;
import com.edexer.model.BcRequestStatus;

@Service("bcRequestStatusServiceManager")
public class BcRequestStatusServiceManagerImpl implements
		BcRequestStatusServiceManager {

	@Autowired
	BcRequestStatusEntityDaoImpl bcResquestStatusDao;

	@Override
	public List<BcRequestStatus> listAllBcRequestStatus() {
		// TODO Auto-generated method stub
		List<BcRequestStatus> reqList = new ArrayList<BcRequestStatus>();
		try {
			reqList = bcResquestStatusDao.listAllBcRequestStatus();
			return reqList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	@Override
	public BcRequestStatus getBcRequestStatusById(Integer Id) {
		// TODO Auto-generated method stub
		BcRequestStatus bcReqStatus = null;
		try {
			if (!Id.equals(0)) {
				bcReqStatus = bcResquestStatusDao
						.get(BcRequestStatus.class, Id);
			}
			return bcReqStatus;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}
}
