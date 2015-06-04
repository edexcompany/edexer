package com.edexer.mbeans.staffmbeans;


import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.edexer.service.WebsiteMessageServiceManager;

@ManagedBean
@ViewScoped
public class DynamicWebSiteMessageManagedBean {
	ResourceBundle settings = ResourceBundle.getBundle("settings");
	private String MessageSubject;
	private String Message;
	private String footer;

	@ManagedProperty("#{webSiteServiceManager}")
	private WebsiteMessageServiceManager websiteerviceManager;
	
	@PostConstruct
	void init() {
		setValues();
	}
	public void setValues()
	{
		try {
			String KeySubjectfromBundel=settings.getString("WEB_SITE_MESSAGE_SUBJECT_KEY");
			String KeyMessagefromBundel=settings.getString("WEB_SITE_MESSAGE_KEY");
			String keyFooterfromBundel=settings.getString("WEB_SITE_FOOTER_KEY");
			MessageSubject= websiteerviceManager.getwebSiteMessageValueByKey(KeySubjectfromBundel);
			Message= websiteerviceManager.getwebSiteMessageValueByKey(KeyMessagefromBundel);
			footer=websiteerviceManager.getwebSiteMessageValueByKey(keyFooterfromBundel);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	public void insert() {
		try {
			websiteerviceManager.updateSubjectMessage(MessageSubject);
			websiteerviceManager.updateBodyMessage(Message);
			websiteerviceManager.updateFooterMessage(footer);
			FacesMessage msg = new FacesMessage("Message Changed");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			ex.printStackTrace();
			
		}
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getMessageSubject() {
		return MessageSubject;
	}
	public void setMessageSubject(String messageSubject) {
		MessageSubject = messageSubject;
	}
	public WebsiteMessageServiceManager getWebsiteerviceManager() {
		return websiteerviceManager;
	}
	public void setWebsiteerviceManager(
			WebsiteMessageServiceManager websiteerviceManager) {
		this.websiteerviceManager = websiteerviceManager;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
}
