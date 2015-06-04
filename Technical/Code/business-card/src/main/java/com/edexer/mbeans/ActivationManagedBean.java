package com.edexer.mbeans;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.User;
import com.edexer.service.AdminConfigManager;
import com.edexer.service.UserServiceManager;
import com.edexer.util.TokenGenerator;

@ManagedBean
@ViewScoped
public class ActivationManagedBean {

	private String activationStr;
	private Boolean chkActivation;
	private String UserID;

	@ManagedProperty("#{adminConfigManager}")
	private AdminConfigManager adminManager;
	private TokenGenerator tokenGenerator;

	@ManagedProperty("#{userService}")
	private UserServiceManager userManager;

	ResourceBundle settings = ResourceBundle.getBundle("settings");

	@PostConstruct
	void init() {
		updateChkActivation();
	}

	public void updateChkActivation() {
		try {
			User user = null;
			if (UserID != null) {
				Integer userid = Integer.parseInt(UserID);
				user = userManager.getUserById(userid);
			}
			if(user==null)
			{
				user=HttpSessionUtil.getUser();
			}
			if (user != null) {
				String userActStr = user.getActivationStr();
				chkActivation = chActivationForCurrentUser(userActStr);
				if(activationStr !=null && activationStr.equals(userActStr))
				{
					user.setActivationStr("ACTIVATE");
					userManager.update(user);
					chkActivation = chActivationForCurrentUser(user
							.getActivationStr());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Boolean chActivationForCurrentUser(String Activation) {
		Boolean result = true;
		try {
			if (Activation !=null && !Activation.equals("")) {
				if (Activation.equals("ACTIVATE")) {
					result = false;
				}
			}
			return result;

		} catch (Exception ex) {
			ex.printStackTrace();
			return result;
		}
	}

	public void sendActivationStr() {
		try {
			User user = HttpSessionUtil.getUser();
			if (!user.equals(null)) {
				String activationString = user.getActivationStr();
				String baseUrl = UtilitiesManagesBean.baseUrl;
				//String baseUrl = settings.getString("APPLICATION_DOMAIN")+ settings.getString("APPLICATION_ROOT")
				String url = baseUrl
						+ "/activation_view.xhtml?activation="
						+ activationString + "&userid=" + user.getUserId();
				String bodyWords = "Please Click On The Link Below to Activate Your Find On Account..";
				String body = bodyWords + "<br/>" + url + "<br/><br/>";
				adminManager.sendInvitation(user, user.getUserEmail(),
						"FindOn Invitation", body);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Boolean getChkActivation() {
		return chkActivation;
	}

	public void setChkActivation(Boolean chkActivation) {
		this.chkActivation = chkActivation;
	}

	public void setActivationStr(String str) {
		this.activationStr = str;
		updateChkActivation();
	}

	public String getActivationStr() {
		return this.activationStr;
	}

	public void handleActivationStr() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("error.xhtml?msg=activation_link_error");
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

	public UserServiceManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserServiceManager userManager) {
		this.userManager = userManager;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
		updateChkActivation();
	}

}
