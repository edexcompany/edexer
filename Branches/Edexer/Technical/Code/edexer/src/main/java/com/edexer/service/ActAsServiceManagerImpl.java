package com.edexer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.dao.ActAsEntityDaoImpl;
import com.edexer.model.ActAs;

@Service("actAsServiceManager")
@Transactional
public class ActAsServiceManagerImpl implements ActAsServiceManager {

	@Autowired
	ActAsEntityDaoImpl actAsDao;
	
	public List<ActAs> getActAsList(){
		return actAsDao.getActAsList();
	}
	
	
}
