/**
 * 
 */
package com.edexer.mbeans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultStreamedContent;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserServiceManagerImpl;

/**
 * @author Mostafa
 *
 */
@ViewScoped
@ManagedBean
public class BusinessCardExportManagedBean {
	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;

	@ManagedProperty("#{userService}")
	private UserServiceManager userService;

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

	public boolean renderExport() {
		User u = getUserService().getUser(HttpSessionUtil.getUser().getUserEmail());
		Iterator it = u.getUserSubscriptionsForUserId().iterator();
		boolean exportEnabled = ((UserSubscription) it.next()).getCanExport()
				.equals("Y");
		return exportEnabled;
	}

	public boolean disableExport() {
		@SuppressWarnings("unchecked")
		boolean selectionFlag = ((List<BusinessCard>) HttpSessionUtil
				.getSession().getAttribute(
						Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY))
				.size() > 0;
		if (selectionFlag)
			return false;
		return true;
	}

	private User getUserFromSession() {
		HttpSession session = HttpSessionUtil.getSession();
		User u = (User) session.getAttribute(Constants.USER);
		return u;
	}

	public UserServiceManager getUserService() {
		return userService;
	}

	public void setUserService(UserServiceManager userService) {
		this.userService = userService;
	}

}
