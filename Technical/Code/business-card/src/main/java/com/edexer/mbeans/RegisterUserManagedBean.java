package com.edexer.mbeans;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.usertype.LoggableUserType;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.ActAs;
import com.edexer.model.Address;
import com.edexer.model.BusinessCard;
import com.edexer.model.PrivacyLevel;
import com.edexer.model.Role;
import com.edexer.model.SubscriptionStatus;
import com.edexer.model.User;
import com.edexer.model.UserStatus;
import com.edexer.model.UserSubscription;
import com.edexer.model.UserSubscriptionId;
import com.edexer.service.AdminConfigManager;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.MailService;
import com.edexer.service.PrivacyLevelServiceManager;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;
import com.edexer.util.PasswordHash;
import com.edexer.util.TokenGenerator;

/**
 * This class act as the User manager bean, that handle user registration module
 * 
 * @author Karim
 *
 */
@ManagedBean
@ViewScoped
public class RegisterUserManagedBean implements Serializable {

	@ManagedProperty("#{userService}")
	private UserServiceManager userService;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubScribtionService;

	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;

	@ManagedProperty("#{adminConfigManager}")
	private AdminConfigManager adminManager;
	
	@ManagedProperty("#{privacyLevelServiceManager}")
	private PrivacyLevelServiceManager privacyManager;
	private TokenGenerator tokenGenerator;

	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String title;
	private String mobile;
	private String phone;
	private String reciver;
    private PrivacyLevel privacyLevel;
	private Integer subscriptionType;
	private Integer parentUserId;

	ResourceBundle settings = ResourceBundle.getBundle("settings");
	private static final Logger LOGGER = Logger.getLogger(MailService.class);

	@PostConstruct
	void init() {
		try {
			email = (String) HttpSessionUtil.getSession().getAttribute(
					"reciver");
		} catch (Exception e) {

		}
	}

	public Boolean chckEmailEmpty() {
		try {
			Boolean result = false;
			if (email != null && !email.equals("")) {
				result = true;
			}
			return result;
		} catch (Exception ex) {
			LOGGER.error("Chk Email Empty Method Error" + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}

	public RegisterUserManagedBean() {
	}

	public com.edexer.service.UserServiceManager getUserService() {
		return userService;
	}

	public void setUserService(com.edexer.service.UserServiceManager userService) {
		this.userService = userService;
	}

	/**
	 * This method handle the user registration action.
	 */
	public String registerAction() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		try {
			// Check user presence in the db with email.
			if (userService.isUserExists(email)) {
				facesContext.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"User name already exists!", "Try another userName"));
				return Constants.INDEX;
			}
			// Manage to insert user into the db.
			User u = getUser();
			if (u == null) {
				FacesUtil
						.addErrorMessage("Error",
								"Problem occured while registering, please try again later");
			}
			int userId = userService.insertUser(u);

			// reaching here, user should be inserted successfully.
			// will manage to add the default subscription record for the user
			// (free subscribe)
			UserSubscriptionId subscriptionId = null;
			if (Constants.FREE_SUBSCRIB_ID.equals(subscriptionType)
					|| subscriptionType == null) {
				subscriptionId = userSubScribtionService
						.insertFreeUserSubscription(userId);
			} else if (Constants.CORP_SUBSCRIB_ID.equals(subscriptionType)
					&& parentUserId != null) {
				subscriptionId = userSubScribtionService
						.insertFreeUserSubscription(userId);
				userSubScribtionService.insertCorpUserSubscription(userId,
						parentUserId);
			}

			// Insert a new business card for this user based on the subId.
			if (subscriptionId != null) {
				// UserSubscription newUserSub = new UserSubscription();
				// newUserSub.setId(subscriptionId);
				BusinessCard bc = new BusinessCard();
				Address address = new Address();
				bc.setBcFirstName(u.getFirstName());
				bc.setBcLastName(u.getLastName());
				bc.setCreator(userId);
				bc.setAddress(address);
				UserSubscription userSub = new UserSubscription();

				// UserSubscriptionId userSubId = new
				// UserSubscriptionId(u.getUserId(),
				// subscriptionId.getSubType());
				// userSub.setId(userSubId);
				// bc.setUserSubscription(userSub);
				// bc.setUserSubscription(newUserSub);
				getBcService().addBusinessCard(bc);
			}

			// Add the user to the session.
			HttpSession session = HttpSessionUtil.getSession();
			session.setAttribute(Constants.USER, u);
			final User user = u;
			final String baseUrl = UtilitiesManagesBean.baseUrl;
			new Thread() {
				public void run() {
					try {
						sendActivationStr(user, baseUrl);
					} catch (Exception ex) {
						LOGGER.error("send activation error" + ex.getMessage());
						ex.printStackTrace();
					}
				}
			}.start();
		} catch (HibernateException e) {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_FATAL, Constants.FATAL_ERROR,
					"Please try again later!"));
			e.printStackTrace();
			return Constants.ERROR;
		}
		// facesContext.addMessage(null, new
		// FacesMessage(FacesMessage.SEVERITY_INFO, "User added",
		// "Congrat. !"));
		return Constants.USER;
	}

	public void sendActivationStr(User user, String baseUrl) {
		try {
			if (!user.equals(null)) {
				String activationStr = user.getActivationStr();
				// String baseUrl = UtilitiesManagesBean.baseUrl;
				// String baseUrl = settings.getString("APPLICATION_DOMAIN")+
				// settings.getString("APPLICATION_ROOT");
				String baseUrl2 = UtilitiesManagesBean.baseUrl;
				String url = baseUrl2 + "/activation_view.xhtml?activation="
						+ activationStr + "&userid=" + user.getUserId();
				String bodyWords = "Please Click On The Link Below to Activate Your Find On Account..";
				String body = bodyWords + "<br/>" + url + "<br/><br/>";
				adminManager.sendInvitation(user, email, "FindOn Invitation",
						body);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * helper method to return User object
	 * 
	 * @return
	 */
	private User getUser() {
		User u = new User();
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setMobile(mobile);
		u.setPassword(password);
		u.setPhone(phone);
		u.setUserEmail(email);
		privacyLevel=privacyManager.getPrivacyLevelByName("Private");
		u.setPrivacyLevel(privacyLevel);
		UserStatus us = new UserStatus();
		us.setUserStatusId(Integer.valueOf(settings
				.getString("USER_STATUS_ACTIVE")));
		u.setUserStatus(us);
		String activationStr = tokenGenerator.generateUUID(true);
		u.setActivationStr(activationStr);
		// Adding users with normal user privilages.
		// For admins, there will be a dedicated form to add admins.
		Role r = new Role();
		r.setRoleId(2);
		u.setRole(r);
		return u;
	}

	/**
	 * This is a helper method just to insert the free user subscription that
	 * should be inserted with every new user registration (that can be upgraded
	 * later), user inserted with Free user subscription.
	 * 
	 * @param userId
	 */
	private void insertFreeUserSubscription(int userId) {
		UserSubscription userSub = new UserSubscription();

		UserSubscriptionId usid = new UserSubscriptionId();
		usid.setSubType(1);
		usid.setUserId(userId);

		userSub.setId(usid);

		Date curDate = new Date();
		userSub.setLastEditDate(new Date());
		userSub.setStartDate(curDate);

		User onTheFlyUser = new User();
		onTheFlyUser.setUserId(userId);
		userSub.setUserByLastEditBy(onTheFlyUser);

		SubscriptionStatus subStatus = new SubscriptionStatus();
		subStatus.setSubStatusId(1);
		userSub.setSubscriptionStatus(subStatus);

		ActAs actas = new ActAs();
		actas.setActAsId(1);
		userSub.setActAs(actas);

		userSubScribtionService.insertUserSubscription(userSub);
	}

	/**
	 * password getter method
	 * 
	 * @return String, the password entered by the users
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFisrtName() {
		return firstName;
	}

	public void setFisrtName(String fisrtName) {
		this.firstName = fisrtName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserSubscriptionServiceManager getUserSubScribtionService() {
		return userSubScribtionService;
	}

	public void setUserSubScribtionService(
			UserSubscriptionServiceManager userSubScribtionService) {
		this.userSubScribtionService = userSubScribtionService;
	}

	public Integer getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(Integer subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public void handleRequestParams() {
		// FacesUtil.addInfoMessage("subscriptionType=" + subscriptionType, "");
		// FacesUtil.addInfoMessage("parentUserId=" + parentUserId, "");
	}

	public Integer getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(Integer parentUserId) {
		this.parentUserId = parentUserId;
	}

	public BusinessCardServiceManager getBcService() {
		return bcService;
	}

	public void setBcService(BusinessCardServiceManager bcService) {
		this.bcService = bcService;
	}

	public AdminConfigManager getAdminManager() {
		return adminManager;
	}

	public void setAdminManager(AdminConfigManager adminManager) {
		this.adminManager = adminManager;
	}

	public TokenGenerator getTokenGenerator() {
		return tokenGenerator;
	}

	public void setTokenGenerator(TokenGenerator tokenGenerator) {
		this.tokenGenerator = tokenGenerator;
	}

	public String getReciver() {
		return reciver;
	}

	public void setReciver(String reciver) {
		this.reciver = reciver;
	}
public PrivacyLevel getPrivacyLevel() {
		return privacyLevel;
	}

	public void setPrivacyLevel(PrivacyLevel privacyLevel) {
		this.privacyLevel = privacyLevel;
	}
	public PrivacyLevelServiceManager getPrivacyManager() {
		return privacyManager;
	}

	public void setPrivacyManager(PrivacyLevelServiceManager privacyManager) {
		this.privacyManager = privacyManager;
	}
}
