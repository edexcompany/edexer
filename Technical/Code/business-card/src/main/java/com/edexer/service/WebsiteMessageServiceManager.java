package com.edexer.service;

public interface WebsiteMessageServiceManager {
	public String getwebSiteMessageValueByKey(String key);
	public void updateSubjectMessage(String Subject);
	public void updateBodyMessage(String Message);
	public void updateFooterMessage(String Footer);
	public void insertSubjectMessage(String Subject);
	public void insertBodyMessage(String Message);
	public void insertFooterMessage(String Footer);
}
