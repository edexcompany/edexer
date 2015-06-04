package com.edexer.service;

import java.util.List;

import javax.servlet.http.Cookie;

import com.edexer.model.User;

public interface UserServiceManager {

	Integer insertUser(User user);

	User getUserById(int userId);

	User getUser(String username);

	User getUser(String userName, String password);

	List<User> getUsers();
	public List<User> getAllUsers();

	boolean isUserExists(String userEmail);

	public void update(User user);

	public void delete(User user);

	void delete(User user, Integer adminUserId);

	public Boolean changeStaffPassword(User userInSession,
			String confirmationPassword, User targetUser, String newPassword);

	/**
	 * Gets other users on the same corporate subscription with this user
	 * 
	 * @param user
	 *            the user to get his colleagues
	 * @return list of all users for the given user's corporate subscription
	 */
	public List<User> getCorporateUsers(User user);

	/**
	 * initialize dependencies
	 * 
	 * @param user
	 * @return
	 */
	public User initializeUserRelations(User user);

	/**
	 * Updates the user password after validating old password
	 * 
	 * @param user
	 * @param oldPassword
	 *            , set null if validation is not required
	 * @param newPassword
	 * @return true if updated
	 */
	public boolean updatePassword(User user, String oldPassword,
			String newPassword);

	/**
	 * adds the remember me cookie
	 * 
	 * @param u
	 *            the user to add cookie
	 */
	public void setRememberMeCookie(User u);

	public void removeRememberMeCookie();

	boolean validateUserCookie(Cookie cookie);
	
	
	
}
