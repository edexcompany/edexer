package com.edexer.mbeans;

import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.dao.MailConfigEntityDaoImpl;
import com.edexer.model.BusinessCard;
import com.edexer.model.MailConfig;
import com.edexer.model.User;
import com.edexer.service.MailConfigServiceManager;
import com.edexer.util.Base64EncoderUtil;
import com.edexer.util.encryptDecrypt;
import com.mysql.jdbc.util.Base64Decoder;
import com.sun.mail.util.BASE64EncoderStream;

@ManagedBean
@SessionScoped
public class MailConfigManagedBean {

	@ManagedProperty("#{mailConfigServiceManagerImpl}")
	private MailConfigServiceManager mailConfigService;

	@ManagedProperty("#{MailConfigEntityDao}")
	private MailConfigEntityDaoImpl mailConfigDao;
	FacesContext facesContext;
	private User user;
	private MailConfig mConfig;
	private String fromEmail;
	private String fromEmailPass;
	private String outGoingMail;
	private String encryptType;
	private Integer portNumber;
	private String authentication;
	private Integer timeOut;
	private Boolean disableSave = true;
	private boolean selectionExist;
	private boolean configured;
	ResourceBundle settings = ResourceBundle.getBundle("settings");

	@PostConstruct
	public void init() {
		user = HttpSessionUtil.getUser();
		fillForm(user);
	}

	public void fillForm(User user) {
		mConfig = mailConfigDao.checkIfExist(user.getUserId());
		if (mConfig != null) {
			fromEmail = mConfig.getFromEmail();
			encryptDecrypt enClass = new encryptDecrypt();
			fromEmailPass = enClass.decrypt(mConfig.getFromEmailPassword());
			outGoingMail = mConfig.getOutgoingMailServer();
			encryptType = mConfig.getEncryptionType();
			portNumber = mConfig.getPortNumber();
			timeOut = mConfig.getTimeOut();
			authentication = (mConfig.getAuthentication().toString())
					.equals("true") ? "1" : "0";
		}
	}

	public void testConfiguration() {
		try {
			// if (mConfig == null) {
			user = HttpSessionUtil.getUser();
			mConfig = new MailConfig();
			mConfig.setUser(user);
			mConfig.setFromEmail(fromEmail);
			mConfig.setFromEmailPassword(fromEmailPass);
			mConfig.setOutgoingMailServer(outGoingMail);
			mConfig.setEncryptionType(encryptType);
			mConfig.setPortNumber(portNumber);
			if (authentication.equals("1")) {
				mConfig.setAuthentication(true);
			} else if (authentication == null || authentication.equals("")) {
				mConfig.setAuthentication(false);
			}
			mConfig.setTimeOut(timeOut);
			String result = mailConfigService.validateOutgoingMailConfig(user,
					mConfig);
			if (result == null) {
				disableSave = false;
				facesContext = FacesContext.getCurrentInstance();
				facesContext.getExternalContext().getFlash()
						.setKeepMessages(true);
				facesContext.addMessage(null, new FacesMessage(
						"testing succeed"));
			}
			else
			{
				disableSave = true;
				facesContext = FacesContext.getCurrentInstance();
				facesContext.getExternalContext().getFlash().setKeepMessages(true);
				facesContext.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_FATAL, Constants.FATAL_ERROR,
						"Email Configuration not Right..!"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			
		}

	}

	public void SaveConfiguration() {
		try {
			user = HttpSessionUtil.getUser();
			mConfig = new MailConfig();
			mConfig.setUser(user);
			mConfig.setFromEmail(fromEmail);
			encryptDecrypt enClass = new encryptDecrypt();
			String passEncrypted = enClass.encrypt(fromEmailPass);
			mConfig.setFromEmailPassword(passEncrypted);
			mConfig.setOutgoingMailServer(outGoingMail);
			mConfig.setEncryptionType(encryptType);
			mConfig.setPortNumber(portNumber);
			if (authentication.equals("1")) {
				mConfig.setAuthentication(true);
			} else {
				mConfig.setAuthentication(false);
			}
			mConfig.setTimeOut(timeOut);
			mailConfigService.InsertOrUpdateMailConfig(mConfig, user);
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage(
					"Configuration Saved Succesfully"));
			fillForm(user);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * public void addMessage(ActionEvent actionEvent) {
	 * 
	 * FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
	 * "Hello", null); FacesContext.getCurrentInstance().addMessage(null,
	 * message); }
	 */
	public void clearAll() {
		user = new User();
		mConfig = new MailConfig();
		fromEmail = "";
		fromEmailPass = "";
		outGoingMail = "";
		encryptType = "";
		portNumber = 0;
		authentication = "";
		timeOut = 0;
	}

	public void isMaiConfigExist() throws IOException {
		User user = HttpSessionUtil.getUser();
		if (user.getMailConfigs().size() <= 0) {
			String baseUrl = UtilitiesManagesBean.baseUrl;
			// String baseUrl = "/" + settings.getString("APPLICATION_ROOT");
			String url = baseUrl + "/user/usersettings.xhtml";
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url);
		}
	}

	@SuppressWarnings("unchecked")
	public boolean disableSendEmail() {
		selectionExist = (((List<BusinessCard>) HttpSessionUtil
				.getSession()
				.getAttribute(Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY))
				.size() > 0);
		configured = (HttpSessionUtil.getUser().getMailConfigs().size() > 0);
		if (selectionExist && configured)
			return false;
		return true;
	}

	public boolean showSendEmailDlg() {
		User user = HttpSessionUtil.getUser();
		if (user.getMailConfigs().size() <= 0) {
			return false;
		}
		return true;
	}

	public MailConfigServiceManager getMailConfigService() {
		return mailConfigService;
	}

	public void setMailConfigService(MailConfigServiceManager mailConfig) {
		this.mailConfigService = mailConfig;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getFromEmailPass() {
		return fromEmailPass;
	}

	public void setFromEmailPass(String fromEmailPass) {
		this.fromEmailPass = fromEmailPass;
	}

	public String getOutGoingMail() {
		return outGoingMail;
	}

	public void setOutGoingMail(String outGoingMail) {
		this.outGoingMail = outGoingMail;
	}

	public String getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

	public Integer getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(Integer portNumber) {
		this.portNumber = portNumber;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public Integer getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	public Boolean getDisableSave() {
		return disableSave;
	}

	public void setDisableSave(Boolean disableSave) {
		this.disableSave = disableSave;
	}

	public MailConfigEntityDaoImpl getMailConfigDao() {
		return mailConfigDao;
	}

	public void setMailConfigDao(MailConfigEntityDaoImpl mailConfigDao) {
		this.mailConfigDao = mailConfigDao;
	}

	public boolean isSelectionExist() {
		return selectionExist;
	}

	public void setSelectionExist(boolean selectionExist) {
		this.selectionExist = selectionExist;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

}
