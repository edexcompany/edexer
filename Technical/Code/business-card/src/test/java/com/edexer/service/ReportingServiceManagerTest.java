package com.edexer.service;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.model.Countries;
import com.edexer.model.Sector;
import com.edexer.model.UserSubscription;
import com.edexer.model.UserSubscriptionId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class ReportingServiceManagerTest extends TestCase {
	@Autowired
	ReportingServicesManager reportingService;

	@Test
	public void runReport1() {
		UserSubscription us = new UserSubscription();
		us.setId(new UserSubscriptionId(59, 1));
		Countries c = new Countries();
		c.setIdCountry(1);
		Sector sector = new Sector();
		sector.setSectorId(1);
		try {
			reportingService.getBCCountPerSector(us);
			reportingService.getBCCountPerSector(us, c);
			reportingService.getTitleCountGivenCountryAndSector(us, c, sector);
		} catch (Exception e) {

		}

	}

}
