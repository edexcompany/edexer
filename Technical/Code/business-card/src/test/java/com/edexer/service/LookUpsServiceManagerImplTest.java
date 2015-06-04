package com.edexer.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.model.ActAs;

import com.edexer.model.Countries;
import com.edexer.model.Subscription;
import com.edexer.model.SubscriptionStatus;
import com.edexer.model.Tags;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;


//TODO: add junit 4 dependency in pom.xml
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class LookUpsServiceManagerImplTest extends TestCase {

	
	@Autowired
	LookUpsServiceManager lookupsService;
	
	//@Test
	public void getCountries(){
		List<Countries> a = lookupsService.getCountriesList();
		
		for (Countries country : a) {
			//System.out.println(a.toString());
		}
	}
	
	@Test
	public void test(){
		
	}
}
