package com.edexer.dao;

import org.springframework.stereotype.Repository;

import CBFSMS.SMS;
import CBFSMS.SMSClientException;
import CBFSMS.SendSMS;

@Repository
public class SendSMSDao {

	/* Your username and password go here */

	private static final String username = "mansour1";
	private static final String password = "n04YeRLY";

	/* Replace the destination number with your test destination */
	/*
	 * private static final String[] destination = { "00201092018178" }; private
	 * static final String source = "rady";
	 */
	/*
	 * public void SendSMSExample(String username, String password) throws
	 * SMSClientException { SendSMS.initialise(username, password); }
	 */

	public void Sendaction(String[] mobilenumber, String source, String body) {

		// SendSMSExample e = new SendSMSExample(username, password);

		try {
			System.out.println("methode called !!!");
			SendSMS.initialise(username, password);
			
			SMS sms = new SMS();

			/* Set the destination address */
			sms.setDestination(mobilenumber);

			/* Set the source address */
			sms.setSourceAddr(source);

			/* Set the user reference */
			sms.setUserReference("AF31C0D");

			/* Set delivery receipts to 'on' */
			sms.setDR("1");

			/* Set the message content */
			sms.setMessage(body);
			int[] responses = SendSMS.sendSMS(sms);
			System.out.println("Example 7.1 Response:");
			for (int i = 0; i < responses.length; i++) {
				System.out.println("\t" + responses[i]);
			}
			System.out.println("sent done");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/*
	 * try { //SendSMSExample e = new SendSMSExample(username, password);
	 * e.example7_1(); // e.example7_2(); // e.example7_3(); // e.example7_4();
	 * // e.example7_5(); // e.example7_6(); // e.example7_7(); //
	 * e.example7_8(); } catch (SMSClientException e) {
	 * System.err.println(e.getMessage()); } }
	 */

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}
}
