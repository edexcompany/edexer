package com.edexer.service;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.dao.SectorEntityDaoImpl;
import com.edexer.model.Sector;
import com.edexer.model.Tags;
import com.edexer.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class SectorDaoImpl extends TestCase{
	@Autowired
	SectorEntityDaoImpl sectorDao;

	@Test
	public void getSectorByName() {
		Sector s = sectorDao.getsectorByName("Apparel Stores");
		System.out.println(s.getSectorName());
	}
}
