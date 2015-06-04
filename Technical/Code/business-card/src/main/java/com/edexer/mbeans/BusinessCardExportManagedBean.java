/**
 * 
 */
package com.edexer.mbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultStreamedContent;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.service.BusinessCardServiceManager;

/**
 * @author Mostafa
 *
 */
@ViewScoped
@ManagedBean
public class BusinessCardExportManagedBean {
	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;

	public BusinessCardServiceManager getBcService() {
		return bcService;
	}

	public void setBcService(BusinessCardServiceManager bcService) {
		this.bcService = bcService;
	}

	public DefaultStreamedContent exportBusinessCard() {
		HttpSession session = HttpSessionUtil.getSession();

		List<BusinessCard> bcList = (List<BusinessCard>) session
				.getAttribute(Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);

		return bcService.exportBusinessCards(bcList, getUserFromSession());
	}

	
	
	

	private User getUserFromSession() {
		HttpSession session = HttpSessionUtil.getSession();
		User u = (User) session.getAttribute(Constants.USER);
		return u;
	}

}
