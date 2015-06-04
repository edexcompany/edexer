package com.edexer.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edexer.dao.PrivacyLevelEntityDaoImpl;
import com.edexer.model.PrivacyLevel;

@Service("privacyLevelServiceManager")
public class PrivacyLevelServiceManagerImpl implements
		PrivacyLevelServiceManager, Serializable {

	@Autowired
	PrivacyLevelEntityDaoImpl privacyLevelDao;

	@Override
	public PrivacyLevel getPrivacyLevelById(Integer Id) {
		try {
			return privacyLevelDao.getPrivacyLevelById(Id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	@Override
	public PrivacyLevel getPrivacyLevelByName(String Name) {
		try {
			return privacyLevelDao.getPrivacyLevelByName(Name);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PrivacyLevel> listAllPrivacyLevel() {
		try {
			return privacyLevelDao.listAllPrivacyLevel();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
