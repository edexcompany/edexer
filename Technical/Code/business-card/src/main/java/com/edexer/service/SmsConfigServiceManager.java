package com.edexer.service;

import java.util.List;

import com.edexer.model.BusinessCard;
import com.edexer.model.User;

public interface SmsConfigServiceManager {

	public String SendSms(String Subject, String Body, List<BusinessCard> bCard);

}
