package com.edexer.service;

import java.util.List;

import com.edexer.model.PrivacyLevel;

public interface PrivacyLevelServiceManager {

	public PrivacyLevel getPrivacyLevelById(Integer Id);
	public PrivacyLevel getPrivacyLevelByName(String Name);
	public List<PrivacyLevel> listAllPrivacyLevel();
}
