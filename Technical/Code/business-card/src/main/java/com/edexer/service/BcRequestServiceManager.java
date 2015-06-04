package com.edexer.service;

import java.util.List;

import com.edexer.model.BcRequest;
import com.edexer.model.User;


public interface BcRequestServiceManager {

	
	public String sendBcRequest(User senderUser,User reciverUser,String Message);
	public List<BcRequest> listSentBcRequests(User senderUser);
	public List<BcRequest> listReciverBcRequests(User reciverUser);
	public List<BcRequest> listBcRequestsBySenderANDreciver(User senderUser,User reciverUser);
	public void updateBcRequest(BcRequest bcRequest);
	public void acceptBcRequest(BcRequest bcRequest);
	public void ignoreBcRequest(BcRequest bcRequest);
	public BcRequest getBcRequestById(Integer id);
	public void sendEmailAfterRequest(User reciverUser, String Message);
	public void exchangeCardswhenAccept(User sender,User reciver);
	
	
}
