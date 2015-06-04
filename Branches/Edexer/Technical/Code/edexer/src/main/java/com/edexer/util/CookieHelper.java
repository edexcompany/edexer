package com.edexer.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CookieHelper {

	public static void setCookie(String name, String value, int expiry)
			throws UnsupportedEncodingException {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		Map<String, Object> properties = new HashMap<>();
		properties.put("maxAge", 31536000);
		properties.put("path", "/");

		facesContext.getExternalContext().addResponseCookie(name,
				URLEncoder.encode(value, "UTF-8"), properties);

		// HttpServletRequest request = (HttpServletRequest) facesContext
		// .getExternalContext().getRequest();
		// Cookie cookie = null;
		//
		// Cookie[] userCookies = request.getCookies();
		// if (userCookies != null && userCookies.length > 0) {
		// for (int i = 0; i < userCookies.length; i++) {
		// if (userCookies[i].getName().equals(name)) {
		// cookie = userCookies[i];
		// break;
		// }
		// }
		// }
		//
		// if (cookie != null) {
		// cookie.setValue(value);
		// } else {
		// cookie = new Cookie(name, value);
		// cookie.setPath(request.getContextPath());
		// }
		//
		// cookie.setMaxAge(expiry);
		//
		// HttpServletResponse response = (HttpServletResponse) facesContext
		// .getExternalContext().getResponse();
		// response.addCookie(cookie);
	}

	public static Cookie getCookieFromRequest(String name,
			HttpServletRequest request) {
		// FacesContext facesContext = FacesContext.getCurrentInstance();
		// HttpServletRequest request = (HttpServletRequest)
		// facesContext.getExternalContext().getRequest();
		Cookie cookie = null;

		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					return cookie;
				}
			}
		}
		return null;
	}

	public static Cookie getCookie(String name)
			throws UnsupportedEncodingException {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		Cookie cookie = (Cookie) facesContext.getExternalContext()
				.getRequestCookieMap().get(name);
		String value = URLDecoder.decode(cookie.getValue(), "UTF-8");
		return cookie;

		// HttpServletRequest request = (HttpServletRequest) facesContext
		// .getExternalContext().getRequest();
		// Cookie cookie = null;
		//
		// Cookie[] userCookies = request.getCookies();
		// if (userCookies != null && userCookies.length > 0) {
		// for (int i = 0; i < userCookies.length; i++) {
		// if (userCookies[i].getName().equals(name)) {
		// cookie = userCookies[i];
		// return cookie;
		// }
		// }
		// }
		// return null;
	}

}