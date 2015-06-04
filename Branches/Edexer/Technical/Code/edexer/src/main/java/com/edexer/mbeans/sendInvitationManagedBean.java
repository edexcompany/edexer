package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.User;
import com.edexer.service.AdminConfigManager;
import com.edexer.util.PasswordHash;

@ManagedBean
@SessionScoped
public class sendInvitationManagedBean {

	public PasswordHash passMethods;

	private String senderEmail;
	private String inviteEmail;
	private String timeString;
	ResourceBundle settings = ResourceBundle.getBundle("settings");

	@ManagedProperty("#{adminConfigManager}")
	private AdminConfigManager adminManager;

	@PostConstruct
	void init() {
		User user = HttpSessionUtil.getUser();
		if (user != null) {
			senderEmail = user.getUserEmail();
		}
	}

	public List<String> getListEmails(String Emails) {
		List<String> emailsList = new ArrayList<String>();
		try {
			if (!Emails.equals("")) {
				String[] emailsArr = Emails.split(",");
				for (String email : emailsArr) {
					if (!email.equals("")) {
						emailsList.add(email);
					}
				}
			}
			return emailsList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void send() {
		try {

			List<String> emails = new ArrayList<String>();
			emails = getListEmails(inviteEmail);
			for (String email : emails) {

				String tokenBeforHash = senderEmail + email;
				String tokenAfterHash = passMethods
						.createGenericHash(tokenBeforHash);
				String baseUrl = UtilitiesManagesBean.baseUrl;
				// String baseUrl = settings.getString("APPLICATION_DOMAIN")+
				// settings.getString("APPLICATION_ROOT");
				String url = baseUrl + "/index.xhtml?sender=" + senderEmail
						+ "&reciver=" + email + "&token=" + tokenAfterHash;
				String bodyWords = "you got an invitation From "
						+ senderEmail
						+ " <br/> please click on the link below and discover our achievements.<br/>";
				String body = bodyWords + "<br/>" + url + "<br/><br/>";
				User user = HttpSessionUtil.getUser();
				adminManager.sendInvitation(user, email, "FindOn Invitation",
						body);
				FacesMessage msg = new FacesMessage(
						"Invitation Sent Sucessfully..");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				setInviteEmail("");
				inviteEmail = "";

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
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

	public String getInviteEmail() {
		return inviteEmail;
	}

	public void setInviteEmail(String inviteEmail) {
		this.inviteEmail = inviteEmail;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public AdminConfigManager getAdminManager() {
		return adminManager;
	}

	public void setAdminManager(AdminConfigManager adminManager) {
		this.adminManager = adminManager;
	}
}
