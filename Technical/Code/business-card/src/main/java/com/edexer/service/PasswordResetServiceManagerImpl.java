package com.edexer.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.dao.PasswordResetEntityDaoImpl;
import com.edexer.mbeans.UtilitiesManagesBean;
import com.edexer.model.PasswordReset;
import com.edexer.model.User;
import com.edexer.util.TokenGenerator;

@Transactional
@Service("passwordResetServiceManager")
public class PasswordResetServiceManagerImpl implements
		PasswordResetServiceManager {

	private static final Logger logger = Logger
			.getLogger(PasswordResetServiceManagerImpl.class);

	ResourceBundle settings = ResourceBundle.getBundle("settings");
	ResourceBundle lang = ResourceBundle.getBundle("lang");
	@Autowired
	private MailService mailService;
	@Autowired
	private UserServiceManager userService;

	@Autowired
	private PasswordResetEntityDaoImpl passwordResetDao;

	@Override
	public void createPasswordResetRequest(String email) {
		User u = userService.getUser(email);
		if (u == null)
			return;
		PasswordReset pr = new PasswordReset(u,
				TokenGenerator.generateUUID(true), new Timestamp(
						(new Date()).getTime()), "N");
		passwordResetDao.add(pr);
		sendEmail(pr);
	}

	/**
	 * Retrieve by token, then validate; check if not used and check if not
	 * expired. if retrieved it will be marked as used.
	 * 
	 * @param token
	 * @return PasswordReset object or null if failed in validation
	 */

	@Override
	public PasswordReset retrievePasswordReset(String token) {
		PasswordReset ps = passwordResetDao.getPasswordResetByToken(token);
		if (ps == null)
			return null;
		if (isConsumed(ps) || isExprired(ps))
			return null;
		consume(ps);
		return ps;
	}

	private boolean isConsumed(PasswordReset ps) {
		if (ps.getUsed().equals("Y"))
			return true;
		return false;
	}

	@SuppressWarnings("deprecation")
	private boolean isExprired(PasswordReset ps) {
		int expiryHours = Integer.valueOf(settings
				.getString("PASS_RESET_EXPIRY_HOURS"));
		Date currentDate = new Date();
		Date expiryDate = ps.getCreationTime();
		expiryDate.setHours(expiryDate.getHours() + expiryHours);
		return currentDate.after(expiryDate);
	}

	private void consume(PasswordReset ps) {
		ps.setUsed("Y");
		passwordResetDao.update(ps);
	}

	private void sendEmail(PasswordReset ps) {
		try {
			String body = lang.getString("PASS_RESER_MAIL_BODY_HEADER");
			String baseUrl = UtilitiesManagesBean.baseUrl;
			// String baseUrl = settings.getString("APPLICATION_DOMAIN")+
			// settings.getString("APPLICATION_ROOT");
			String url = baseUrl + "/passwordreset.xhtml?token="
					+ ps.getToken();
			body = body + "\n" + url + "\n\n"
					+ lang.getString("PASS_RESER_MAIL_BODY_TAIL");
			// PASS_RESER_MAIL_BODY_HEADER
			mailService.sendMail(ps.getUser().getUserEmail(),
					lang.getString("PASS_RESER_MAIL_TITLE"), body);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
