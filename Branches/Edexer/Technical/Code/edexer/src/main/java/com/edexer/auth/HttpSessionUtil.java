package com.edexer.auth;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.edexer.mbeans.Constants;
import com.edexer.model.User;

public class HttpSessionUtil {

	/**
	 * this method returns a HttpSession object
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
	}

	/**
	 * this method returns a HttpSession object
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getSession(boolean create) {
		System.out.println("");
		HttpSession h = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(create);
		return h;
	}

	public static User getUser() {
		return (User) HttpSessionUtil.getSession().getAttribute(Constants.USER);
	}

	/**
	 * get the entire request
	 * 
	 * @return HttpServletRequest, represents the Http request with its all
	 *         attrbutes.
	 */
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	/**
	 * get username from the session
	 * 
	 * @return String represents the user name.
	 */
	public static String getUserName() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		return session.getAttribute("username").toString();
	}

	/**
	 * get userId from the session
	 * 
	 * @return String represents the user id.
	 */
	public static String getUserId() {
		HttpSession session = getSession();
		if (session != null)
			return (String) session.getAttribute("userid");
		else
			return null;
	}
}
