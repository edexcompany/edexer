package com.edexer.mbeans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.edexer.faces.util.FacesUtil;
import com.edexer.model.PasswordReset;
import com.edexer.service.PasswordResetServiceManager;
import com.edexer.service.UserServiceManager;

@ManagedBean
@ViewScoped
public class PasswordResetManagedBean {

	@ManagedProperty("#{passwordResetServiceManager}")
	private PasswordResetServiceManager passwordResetService;

	@ManagedProperty("#{userService}")
	private UserServiceManager userService;

	private String email;
	private String token;
	private PasswordReset ps;
	private String newPassword;;

	@PostConstruct
	public void init() {
		;
	}

	public void createPasswordReset() {
		try {
			passwordResetService.createPasswordResetRequest(email);
		} catch (Exception e) {

		} finally {
			FacesUtil.addInfoMessage("Password Reset",
					"Recovery link is sent to your mail.");
		}

	}

	public String savePassword() {
		try {
			userService.updatePassword(ps.getUser(), null, newPassword);
			return Constants.INDEX;
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Reset Password",
					"Error occured while reseting your password");
			return "";
		}

	}

	public void getPasswordReset() {
		if (token == null)
			return;
		ps = passwordResetService.retrievePasswordReset(token);
	}

	public PasswordResetServiceManager getPasswordResetService() {
		return passwordResetService;
	}

	public void setPasswordResetService(
			PasswordResetServiceManager passwordResetService) {
		this.passwordResetService = passwordResetService;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public PasswordReset getPs() {
		return ps;
	}

	public void setPs(PasswordReset ps) {
		this.ps = ps;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public UserServiceManager getUserService() {
		return userService;
	}

	public void setUserService(UserServiceManager userService) {
		this.userService = userService;
	}

}
