package com.edexer.mbeans;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.junit.runner.Request;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.dao.UserEntityDaoImpl;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.Role;
import com.edexer.model.User;
import com.edexer.service.AdminConfigManager;
import com.edexer.service.UserServiceManager;
import com.edexer.util.CookieHelper;
import com.edexer.util.PasswordHash;

/**
 * @author Karim
 * 
 *         This is the managed bean responsible for user login action
 *
 */
@ManagedBean
@ViewScoped
public class LoginManagedBean implements Serializable {

	@ManagedProperty("#{userService}")
	UserServiceManager userServiceManager;
	@ManagedProperty("#{adminConfigManager}")
	private AdminConfigManager adminConfigManager;
	public PasswordHash passMethods;

	String email;
	String password;
	private boolean rememberMe;

	private String senderEmail;
	private String reciverEmail;
	private String tokenHash;

	ResourceBundle settings = ResourceBundle.getBundle("settings");
	private static final Logger logger = Logger
			.getLogger(LoginManagedBean.class);

	/*
	 * @PostConstruct void init() { reciverEmail =
	 * HttpSessionUtil.getSession().getAttribute("reciver") .toString();
	 * 
	 * }
	 */

	public Boolean chckRegState() {
		try {
			Boolean result = false;
			String state = getRegState();
			if (!state.equals("")) {
				if (state.toUpperCase().equals("CLOSED")) {
					result = false;
				} else if (state.toUpperCase().equals("OPENED")) {
					result = true;
				} else if (state.toUpperCase().equals("INVITATION")) {
					String input = senderEmail + reciverEmail;
					if (passMethods.validateGenericHash(input, tokenHash)) {
						result = true;
						HttpSessionUtil.getSession().setAttribute("reciver",
								reciverEmail);
					}
				}
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void checkCookie() {
		try {
			Cookie cookie = CookieHelper.getCookie(Constants.LOGIN_COOKIE_NAME);
			if (cookie == null)
				return;
			boolean validateCookie = userServiceManager
					.validateUserCookie(cookie);
			if (validateCookie) {
				String[] params = cookie.getValue().split("-");
				int userId = Integer.valueOf(params[0]);
				User user = userServiceManager.getUserById(userId);
				HttpSessionUtil.getSession(true).setAttribute(Constants.USER,
						user);
			}
		} catch (Exception e) {

		}

	}

	public String logOutAction() {
		// HttpSession session = HttpSessionUtil.getSession();
		userServiceManager.removeRememberMeCookie();
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).invalidate();
		// session.invalidate();

		return Constants.ACTION_LOGOUT;
	}

	public boolean isLoggedIn() {
		return HttpSessionUtil.getUser() != null;
	}

	public String aa() {
		return settings.getString("ROLE_USER_NAME");
	}

	public String loginAction() {
		logger.debug("LoginAction start");
		if (userServiceManager == null) {
			FacesUtil.addErrorMessage(Constants.FATAL_ERROR,
					"Contact sysadmin!");
			logger.debug("userServiceManager is null");
			return Constants.ERROR;
		}
		User u = null;
		try {
			logger.debug("email: " + email + ", pass: " + password);
			u = userServiceManager.getUser(email, password);
			// No user found in the db with the provided email and password.
			if (u == null) {
				logger.debug("user = null");
				FacesUtil.addErrorMessage("Invalid useremail or password",
						"");
				FacesContext context = FacesContext.getCurrentInstance();
				context.getExternalContext().getFlash().setKeepMessages(true);
				return Constants.INDEX;
			}
			logger.debug("user: " + u.getFirstName() + " " + u.getLastName());
			HttpSessionUtil.getSession().setAttribute(Constants.USER, u);
			if (rememberMe)
				setCookie(u);

			logger.debug("user added to session");
			// here user should be found in db.
			// next will get the user role (staff/ user)
			
			return Constants.INDEX;

		} catch (HibernateException e) {
			logger.error("Exception happened");
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			FacesUtil.addErrorMessage(Constants.FATAL_ERROR,
					"Contact sysadmin!");
			e.printStackTrace();
			return Constants.INDEX;
		}
	}

	public String getRegState() {
		return adminConfigManager.getAdminConfigValueByKey("Regstration_State");
	}

	private void setCookie(User u) {
		userServiceManager.setRememberMeCookie(u);
	}

	public UserServiceManager getUserServiceManager() {
		return userServiceManager;
	}

	public void setUserServiceManager(UserServiceManager userServiceManager) {
		this.userServiceManager = userServiceManager;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public AdminConfigManager getAdminConfigManager() {
		return adminConfigManager;
	}

	public void setAdminConfigManager(AdminConfigManager adminConfigManager) {
		this.adminConfigManager = adminConfigManager;
	}

	public PasswordHash getPassMethods() {
		return passMethods;
	}

	public void setPassMethods(PasswordHash passMethods) {
		this.passMethods = passMethods;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReciverEmail() {
		return reciverEmail;
	}

	public void setReciverEmail(String reciverEmail) {
		this.reciverEmail = reciverEmail;
	}

	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}
}
