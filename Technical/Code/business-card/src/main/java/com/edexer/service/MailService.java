package com.edexer.service;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.hibernate.property.Setter;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailService {

	private static final Logger LOGGER = Logger.getLogger(MailService.class);
	// @Autowired
	// MailSender mailSender;
	private static ResourceBundle settingsBundle = ResourceBundle
			.getBundle("settings");
	private static Properties props = new Properties();
	static {
		props.put("mail.smtp.host", settingsBundle.getString("mail.smtp.host"));
		props.put("mail.smtp.socketFactory.port",
				settingsBundle.getString("mail.smtp.socketFactory.port"));
		props.put("mail.smtp.socketFactory.class",
				settingsBundle.getString("mail.smtp.socketFactory.class"));
		props.put("mail.smtp.auth", settingsBundle.getString("mail.smtp.auth"));
		props.put("mail.smtp.port", settingsBundle.getString("mail.smtp.port"));
	}

	public void sendMail(String from, String to, String subject, String body)
			throws Exception {
		LOGGER.info("Sending mail to :" + to);
		try {
			final String UserName = settingsBundle
					.getString("mail.smtp.emailaddress");
			final String Password = settingsBundle
					.getString("mail.smtp.emailpassword");
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(UserName,
									Password);
						}
					});
			MimeMessage message = new MimeMessage(session);
			
			JavaMailSenderImpl sender = new JavaMailSenderImpl();
			sender.setJavaMailProperties(props);
			sender.setUsername(UserName);
			sender.setPassword(Password);
			
			
			MimeMessageHelper helper=new MimeMessageHelper(message,true);
			helper.setTo(to);
			helper.setText(body, true);
			sender.send(message);
			
			// from = "karim.tawfik@meshreq.com";
			// from=String.valueOf(settingsBundle.getString("MAIL_SERVER"));
			/*if (from != null) {
				message.setFrom(new InternetAddress(from));
			}
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(subject);
			message.setContent(body, "text/html");
			Transport.send(message);*/
			
			LOGGER.info("Mail sent Successfully to :" + to);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send mail to :" + to);
			e.printStackTrace();
			throw e;
		}
	}

	public void sendMail(String to, String subject, String body)
			throws Exception {
		sendMail(null, to, subject, body);
	}

}
