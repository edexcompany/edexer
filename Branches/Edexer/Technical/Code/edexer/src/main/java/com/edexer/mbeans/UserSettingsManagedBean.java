package com.edexer.mbeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.layout.LayoutUnit;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.component.panel.Panel;
import org.springframework.stereotype.Service;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.User;
import com.edexer.service.UserServiceManager;
import com.edexer.util.PasswordHash;

@ManagedBean
@ViewScoped

public class UserSettingsManagedBean {
	@ManagedProperty("#{userService}")
	private UserServiceManager userService;
	private User user;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	FacesContext facesContext;

	@PostConstruct
	public void init() {
		user = (User) HttpSessionUtil.getSession().getAttribute(Constants.USER);
	}

	public String saveProfileUnit() {
		
		getUserService().update(user);
		return "saved";
	}

	public String savePasswordUnit() {
		boolean updateVal = userService.updatePassword(user, oldPassword,
				newPassword);
		if (updateVal) {
			return "saved";
		} else {
			FacesUtil.addErrorMessage("Error",
					"Please enter the correct password");
			return "";
		}
	}

	public String saveEmailUnit() {
		User u = userService.getUser(user.getUserEmail(), oldPassword);
		if (u != null) {
			user.setUserEmail(user.getUserEmail());
			getUserService().update(user);
		} else {
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_FATAL, Constants.FATAL_ERROR,
					"Password doesnot match!"));
		}

		return "saved";
	}

	public String saveNetworkUnit() {

		return "";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
