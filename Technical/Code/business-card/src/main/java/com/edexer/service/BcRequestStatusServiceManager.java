package com.edexer.service;

import java.util.List;

import com.edexer.model.BcRequestStatus;

public interface BcRequestStatusServiceManager {
	
   public List<BcRequestStatus> listAllBcRequestStatus();
   public BcRequestStatus getBcRequestStatusById(Integer Id);
}
