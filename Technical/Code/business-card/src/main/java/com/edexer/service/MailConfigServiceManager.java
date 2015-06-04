package com.edexer.service;

import java.util.ArrayList;
import java.util.List;

import com.edexer.model.BusinessCard;
import com.edexer.model.MailConfig;
import com.edexer.model.User;

public interface MailConfigServiceManager {
	public enum BusinessCardTypes {FindOn,Personal,Corporate};
	public String InsertOrUpdateMailConfig(MailConfig mail,User user);
	public String validateOutgoingMailConfig(User user,MailConfig mail);
	public String sendEmail(User user,String subject,String body,List<BusinessCard> bCard);
}
