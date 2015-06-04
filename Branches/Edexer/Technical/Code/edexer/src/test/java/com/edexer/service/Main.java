package com.edexer.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.model.PasswordReset;
import com.edexer.util.TokenGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class Main extends TestCase {

	@Autowired
	private PasswordResetServiceManager passreset;

	@Test
	public void ade() {
		// passreset.createPasswordResetRequest("saadsaleh88@gmail.com");
		PasswordReset a = passreset
				.retrievePasswordReset("95b1551076ed4402aaef61e4f8d9330c");
		System.out.println(a.getUsed());
	}

	public Main() {

	}

}
