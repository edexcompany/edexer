package com.edexer.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.edexer.model.User;
import com.edexer.model.MailConfig;
import com.edexer.util.encryptDecrypt;

@Service("dynamicmailservice")
public class DynamicMailService {
	
	private static final Logger LOGGER = Logger.getLogger(MailService.class);

	public void sendMail(User user, MailConfig mConfig,InternetAddress[] to,
			String subject, String body) throws MessagingException {
		//LOGGER.info("Sending mail to :" + to);
		encryptDecrypt enClass = new encryptDecrypt();
		try {
			String Auth = "false";
			if (mConfig.getAuthentication() != null) {
				if (mConfig.getAuthentication() == true) {
					Auth = "true";
				}
			}
			Properties props = new Properties();
			props.put("mail.smtp.host", mConfig.getOutgoingMailServer());		
			props.put("mail.smtp.auth", Auth);
			props.put("mail.smtp.port", mConfig.getPortNumber());
			
			if (mConfig.getEncryptionType().equals("SSL") ||mConfig.getEncryptionType().equals("None") )
			{
				props.put("mail.smtp.socketFactory.port", mConfig.getPortNumber());
				props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			}
			else if (mConfig.getEncryptionType().equals("TLS"))
			{
				props.put("mail.smtp.starttls.enable", "true");
			}
			final String fromEmail=mConfig.getFromEmail();	
			final String fromEmailPass= mConfig.getFromEmailPassword();
			
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(fromEmail,fromEmailPass);
						}
					});
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mConfig.getFromEmail()));
			message.addRecipients(Message.RecipientType.TO, to);
			message.setSubject(subject);
			message.setContent(body, "text/html");
			Transport.send(message);
			LOGGER.info("Mail sent Successfully to :" + to);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send mail to :" + to);
			e.printStackTrace();
			throw e;
		}
	}



}
