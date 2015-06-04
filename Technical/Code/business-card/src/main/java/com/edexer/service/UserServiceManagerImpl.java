package com.edexer.service;

import java.net.CookieHandler;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.dao.BcPermissionsEntityDaoImpl;
import com.edexer.dao.PrivacyLevelEntityDaoImpl;
import com.edexer.dao.UserEntityDaoImpl;
import com.edexer.dao.UserSubscriptionEntityDaoImpl;
import com.edexer.mbeans.Constants;
import com.edexer.model.PrivacyLevel;
import com.edexer.model.User;
import com.edexer.model.UserStatus;
import com.edexer.model.UserSubscription;
import com.edexer.util.CookieHelper;
import com.edexer.util.PasswordHash;

@Transactional
@Service("userService")
public class UserServiceManagerImpl implements UserServiceManager {

	//
	@Autowired
	UserEntityDaoImpl userDao;

	@Autowired
	BcPermissionsEntityDaoImpl bcPermissionDao;

	@Autowired
	UserSubscriptionEntityDaoImpl userSubDao;

	@Autowired
	UserSubscriptionServiceManager userSubscriptionService;
	@Autowired
	PrivacyLevelEntityDaoImpl privacyLevelDao;
	ResourceBundle settings = ResourceBundle.getBundle("settings");

	public UserEntityDaoImpl getUserDao() {
		return userDao;
	}

	public void setUserDao(UserEntityDaoImpl userDao) {
		this.userDao = userDao;
	}

	/*
	 * Adds a user.
	 */
	public Integer insertUser(User user) {
		String pass = "";
		try {
			pass = PasswordHash.createHash(user.getPassword());
			String[] params = pass.split(":");
			user.setPassword(params[PasswordHash.PBKDF2_INDEX]);
			user.setSalt(params[PasswordHash.SALT_INDEX]);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return (Integer) userDao.add(user);
	}

	public User getUserById(int userId) {
		return userDao.getUserByUserId(userId);
	}

	public boolean isUserExists(String email) {
		return userDao.isUserExists(email);
	}

	public User getUser(String username) {
		return userDao.getUserByUserEmail(username);

	}

	/*
	 * 
	 * 
	 * Returns list of staff members users. Staff member is the user having role
	 * staff or equivalent.
	 */
	public List<User> getUsers() {
		try {
			List<User> users;
			users = userDao.getUserList();
			return users;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<User> getAllUsers() {
		try {
			List<User> users;
			users = userDao.getAllUserList();
			return users;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public User getUser(String userEmail, String password) {
		User userFromDb = getUser(userEmail);
		if (userFromDb == null)
			return null;
		boolean valid = false;
		try {
			valid = PasswordHash.validatePassword(password,
					userFromDb.getPassword(), userFromDb.getSalt());
			if (valid)
				return userFromDb;
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// return userDao.getUser(userEmail, password);
	}

	// Updates a user.
	@Override
	public void update(User user) {
		try {
			userDao.update(user);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(User user, Integer adminUserId) {
		bcPermissionDao.updateByUserIdAndSubScribeType(adminUserId,
				user.getUserId());
		UserSubscription sub = userSubDao.getUserSubscription(user, false);
		userSubDao.delete(sub);
		userDao.delete(user);
	}

	/*
	 * Deletes staff member. Returns true if deleted successfully. Deletion here
	 * is soft delete. The field user status is updated to “deleted”.
	 */
	@Override
	public void delete(User user) {
		try {
			UserStatus userStatusDeleted = new UserStatus();
			userStatusDeleted.setUserStatusId(Integer.valueOf(settings
					.getString("USER_STATUS_DELETED")));
			user.setUserStatus(userStatusDeleted);
			userDao.update(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public BcPermissionsEntityDaoImpl getBcPermissionDao() {
		return bcPermissionDao;
	}

	public void setBcPermissionDao(BcPermissionsEntityDaoImpl bcPermissionDao) {
		this.bcPermissionDao = bcPermissionDao;
	}

	public void setUserSubService(UserSubscriptionEntityDaoImpl userSubService) {
		this.userSubDao = userSubService;
	}

	/*
	 * Method to update a password for some staff user. Take the password for
	 * user in session and compare it to the user in session, if valid password
	 * is changed.
	 */
	@Override
	public Boolean changeStaffPassword(User userInSession,
			String confirmationPassword, User targetUser, String newPassword) {
		Boolean result = false;
		try {
			String UserPassword = userInSession.getPassword();
			if (UserPassword.equals(confirmationPassword)) {
				int target_user_id = targetUser.getUserId();
				if (userDao.updateStaffPassword(newPassword, target_user_id)) {
					result = true;
				}
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return result;
		}
	}

	@Override
	public List<User> getCorporateUsers(User user) {
		UserSubscription parentSubscription = userSubscriptionService
				.getParentCorpSubscriptionByUser(user);
		if (parentSubscription == null)
			return null;
		return userDao
				.getCorporateUsers(parentSubscription.getId().getUserId());
	}

	@Override
	public User initializeUserRelations(User user) {
		return userDao.initializeUser(user);
	}

	@Override
	public boolean updatePassword(User user, String oldPassword,
			String newPassword) {
		User u = getUser(user.getUserEmail());
		try {
			boolean validate = false;
			if (oldPassword == null) {
				validate = true;
			} else {
				validate = PasswordHash.validatePassword(oldPassword,
						u.getPassword(), u.getSalt());
			}
			if (!validate)
				return false;
			String pass = PasswordHash.createHash(newPassword);
			String[] params = pass.split(":");
			u.setPassword(params[PasswordHash.PBKDF2_INDEX]);
			u.setSalt(params[PasswordHash.SALT_INDEX]);
			update(u);
			HttpSession session = HttpSessionUtil.getSession();
			session.setAttribute(Constants.USER, u);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void setRememberMeCookie(User u) {
		try {
			CookieHelper.setCookie(
					Constants.LOGIN_COOKIE_NAME,
					u.getUserId()
							+ "-"
							+ PasswordHash.createCookieHash(u.getUserEmail(),
									u.getSalt()),
					Integer.valueOf(settings.getString("LOGIN_COOKIE_EXPIRY")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean validateUserCookie(Cookie cookie) {
		
		try {
			String[] params = cookie.getValue().split("-");
			User u = getUserById(Integer.valueOf(params[0]));
			return PasswordHash.validateCookieHash(u.getUserEmail(),
					u.getSalt(), params[1]);
		} catch (Exception e) {
			//TODO add logger statement
			return false;
		}
	}

	@Override
	public void removeRememberMeCookie() {
		try {
			CookieHelper.setCookie(Constants.LOGIN_COOKIE_NAME, "-", 3600);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
		/*
	 * Updates the user privacy level for a certain user. Return null if updated
	 * else returns the error string.
	 */
	@Override
	public String updateUserPrivacyLevel(User user, PrivacyLevel privacyLevel) {
		// TODO Auto-generated method stub
		try {

			PrivacyLevel pLevel = privacyLevelDao.get(PrivacyLevel.class,
					privacyLevel.getPrivacyId());
			user.setPrivacyLevel(pLevel);
			userDao.update(user);
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}

	}
	public PrivacyLevelEntityDaoImpl getPrivacyLevelDao() {
		return privacyLevelDao;
	}

	public void setPrivacyLevelDao(PrivacyLevelEntityDaoImpl privacyLevelDao) {
		this.privacyLevelDao = privacyLevelDao;
	}
	
}
