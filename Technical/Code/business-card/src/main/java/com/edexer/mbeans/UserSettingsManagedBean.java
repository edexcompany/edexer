package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.component.layout.LayoutUnit;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.component.panel.Panel;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.PrivacyLevel;
import com.edexer.model.User;
import com.edexer.service.PrivacyLevelServiceManager;
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
    private String privacySelected;
	@ManagedProperty("#{privacyLevelServiceManager}")
	private PrivacyLevelServiceManager privacyServiceManager;
	private List<SelectItem> privacyselectItem = new ArrayList<SelectItem>();
	
	@PostConstruct
	public void init() {
		user = (User) HttpSessionUtil.getSession().getAttribute(Constants.USER);
		for (PrivacyLevel s : privacyServiceManager.listAllPrivacyLevel()) {
			privacyselectItem.add(new SelectItem(s.getPrivacyId(), s
					.getPrivacyName()));
			
		}
		if(user.getPrivacyLevel()!=null)
		{
			privacySelected=user.getPrivacyLevel().getPrivacyId().toString();
		}
	}

	public String saveProfileUnit() {
	if(privacySelected!=null && !privacySelected.equals(""))
		{
			PrivacyLevel privacy=new PrivacyLevel();
			privacy=privacyServiceManager.getPrivacyLevelById(Integer.parseInt(privacySelected));
			user.setPrivacyLevel(privacy);
		}	
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

	public List<SelectItem> getPrivacyselectItem() {
		return privacyselectItem;
	}

	public void setPrivacyselectItem(List<SelectItem> privacyselectItem) {
		this.privacyselectItem = privacyselectItem;
	}

	public PrivacyLevelServiceManager getPrivacyServiceManager() {
		return privacyServiceManager;
	}

	public void setPrivacyServiceManager(
			PrivacyLevelServiceManager privacyServiceManager) {
		this.privacyServiceManager = privacyServiceManager;
	}

	public String getPrivacySelected() {
		return privacySelected;
	}

	public void setPrivacySelected(String privacySelected) {
		this.privacySelected = privacySelected;
	}

}
