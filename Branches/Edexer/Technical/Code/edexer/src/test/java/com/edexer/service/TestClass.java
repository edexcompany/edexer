/*package com.edexer.service;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.model.BusinessCard;

public class TestClass {
	@Autowired
	LookUpsServiceManager lookupsService;
	@Test
	public void test(){
		List<BusinessCard> bcList = new ArrayList<BusinessCard>();
		BusinessCard bc = new BusinessCard();
//		bcList.add(bcService.get(50, true));
//		bcList.add(bcService.get(51, true));
		bcList.add(new BusinessCard());
		bcList.add(new BusinessCard());
		bcList.add(new BusinessCard());
		JSONObject a = new JSONObject(bcList);
		System.out.println(a);
		System.out.println(new JSONObject(new BusinessCard()));
	}
}
*/