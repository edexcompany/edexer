package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.service.MailConfigServiceManager;

@ManagedBean
@SessionScoped
public class sendEmailManagedBean {
	@ManagedProperty("#{mailConfigServiceManagerImpl}")
	private MailConfigServiceManager mailConfigService;

	private List<BusinessCard> listBCards;
	private User user;
	private String Subject;
	private String Body;
	private String From;

	public void sendEmail() {
		try {
			user = HttpSessionUtil.getUser();
			listBCards = (List<BusinessCard>) HttpSessionUtil.getSession()
					.getAttribute(
							Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);
			if (listBCards.size() > 0) {
				mailConfigService.sendEmail(user, Subject, Body, listBCards);
				
			}
			Clear();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	public void Clear() {
		user=new User();
		listBCards=new ArrayList<BusinessCard>();
		Subject="";
		Body="";
		From="";
		FacesUtil.addInfoMessage("grehjry", "vrethr");
	}

	public void viewDialog() {
		RequestContext.getCurrentInstance().openDialog("send_email_dialog");
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

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String body) {
		Body = body;
	}

	public List<BusinessCard> getListBCards() {
		return listBCards;
	}

	public void setListBCards(List<BusinessCard> listBCards) {
		this.listBCards = listBCards;
	}

	public String getFrom() {
		return From;
	}

	public void setFrom(String from) {
		From = from;
	}

}
