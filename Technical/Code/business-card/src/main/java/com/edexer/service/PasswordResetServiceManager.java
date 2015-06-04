package com.edexer.service;

import com.edexer.model.PasswordReset;

public interface PasswordResetServiceManager {

	/**
	 * Creates reset password object
	 * 
	 * @param email
	 */
	public void createPasswordResetRequest(String email);

	/**
	 * Retrieve by token, then validate; check if not used and check if not
	 * expired. if retrieved it will be marked as used.
	 * 
	 * @param token
	 * @return PasswordReset object or null if failed in validation
	 */
	public PasswordReset retrievePasswordReset(String token);

}
