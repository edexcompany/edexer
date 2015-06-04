package com.edexer.service;

import com.edexer.model.User;

public interface AdminConfigManager {
	public String getAdminConfigValueByKey(String key);
	public void updateRegistrationState(String state);
	Boolean sendInvitation(User user, String Mail, String Subject,
			String Message);
	public void updateMaxRecordsCount(int count);
	
}
