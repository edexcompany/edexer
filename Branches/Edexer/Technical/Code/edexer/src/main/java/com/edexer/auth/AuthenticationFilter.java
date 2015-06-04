package com.edexer.auth;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedProperty;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.edexer.mbeans.Constants;
import com.edexer.mbeans.UtilitiesManagesBean;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserServiceManagerImpl;
import com.edexer.util.CookieHelper;
import com.edexer.util.UserUtil;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter(filterName = "authFilter", urlPatterns = "*.xhtml")
public class AuthenticationFilter implements Filter {
	ResourceBundle settingsBundle = ResourceBundle.getBundle("settings");

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			//String applicationRoot = settingsBundle.getString("APPLICATION_ROOT");
			String applicationRoot = UtilitiesManagesBean.baseUrl;
			if(applicationRoot==null)applicationRoot="";
			HttpSession session = ((HttpServletRequest) request)
					.getSession(false);
			if (session == null) {
				session = checkCookie((HttpServletRequest) request);
			}
			// check whether session variable is set
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			// HttpSession session = req.getSession(false);
			// allow user to procceed if url is index.xhtml or user logged in or
			// user is accessing any page in //public folder
			String reqURI = req.getRequestURI();
			User u = null;
			boolean isUserUrl = reqURI.indexOf(Constants.USER) >= 0;
			boolean isValidSession = session != null;
			boolean isValidUserInSession = isValidSession
					&& (u = (User) session.getAttribute(Constants.USER)) != null;
			// below boolean check if the user has the right permissions to be
			// able to navigate to pages under /user directory.
			// User role with role_id = 2, refer to normal users with normal
			// privilage, no extra advantages.

			boolean isUserHome = reqURI.indexOf("user/index.xhtml") >= 0;
			boolean isUserURL = reqURI.indexOf("/user/") >= 0;
			boolean isStaffURL = reqURI.indexOf("/staff/") >= 0;
			boolean isCorpUrl = reqURI.indexOf("corp_user_home") >= 0;
			boolean loggedInUser = (session != null && session
					.getAttribute(Constants.USER) != null);
			boolean isStaffMember = loggedInUser
					&& ((User) session.getAttribute(Constants.USER)).getRole()
							.getRoleId() == Integer.valueOf(settingsBundle
							.getString("STAFF_MEMBER"));
			boolean isUserWithNormalPrivilage = isValidUserInSession
					&& u.getRole().getRoleId() != Integer
							.valueOf(settingsBundle.getString("STAFF_MEMBER"));

			if (isStaffURL) {
				if (isStaffMember) {
					chain.doFilter(request, response);
					return;
				} else {
					res.sendRedirect(req.getContextPath()
							+ "/unauthorized.xhtml");
					return;
				}
			}

			if (isUserUrl && !loggedInUser) {
				res.sendRedirect(req.getContextPath() + "/index.xhtml");
				return;
			}

			// User is accessing the correct resources.
			if (reqURI.indexOf("/login.xhtml") >= 0
					|| reqURI.indexOf("/register.xhtml") >= 0
					|| reqURI.indexOf("/unauthorized.xhtml") >= 0
					|| reqURI.indexOf("/passwordreset.xhtml") >= 0
					|| reqURI.indexOf("/activation_view.xhtml") >= 0) {
				chain.doFilter(request, response);
				return;
			}
			if (isCorpUrl && loggedInUser) {
				Integer corpSubTypeId = Integer.valueOf(settingsBundle
						.getString("SUBSCRIPTION_TYPE_CORP"));
				UserSubscription userCorpSub = UserUtil
						.getUserSubscriptionByType(
								u.getUserSubscriptionsForUserId(),
								corpSubTypeId);
				if (userCorpSub == null) {
					res.sendRedirect(req.getContextPath() + "/user/index.xhtml");
				} else {
					chain.doFilter(request, response);
					return;
				}
			}

			if (isUserHome && loggedInUser) {
				chain.doFilter(request, response);
				return;
			}

			if ((loggedInUser && (reqURI.endsWith(applicationRoot
					+ "/index.xhtml")))
					|| (loggedInUser && (reqURI.endsWith(applicationRoot)) || (loggedInUser && (reqURI
							.endsWith(applicationRoot + "/"))))) {
				res.sendRedirect(req.getContextPath() + "/user/index.xhtml");
			}
			if (isUserUrl && isUserWithNormalPrivilage) {
				chain.doFilter(request, response);
				return;
			} else if (reqURI.indexOf(applicationRoot + "/index.xhtml") >= 0
					|| loggedInUser || reqURI.contains("javax.faces.resource")
					|| (applicationRoot + "/index.xhtml").contains(reqURI)) {
				chain.doFilter(request, response);
				// Here am sure that he is logged in.
			} else {
				// logger.warn("redirecting to index.xhtml");
				res.sendRedirect(req.getContextPath() + "/index.xhtml"); // Anonymous
				// user.
				// Redirect
				// to
				// login
				// page
			}

		} catch (Exception t) {
			// logger.error("Exception occurred while doing the filter check." +
			// t.getMessage());
			t.printStackTrace();
		}
		// chain.doFilter(request, response);
	}

	private void handleUserAuthorizationToAccessPages() {

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

	/**
	 * Default constructor.
	 */
	public AuthenticationFilter() {

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {

	}

	public HttpSession checkCookie(HttpServletRequest request) {
		try {

			System.out.println("Setting cookies");

			WebApplicationContext springContext = WebApplicationContextUtils
					.getWebApplicationContext(request.getServletContext());
			UserServiceManager userService = springContext
					.getBean(UserServiceManager.class);

			Cookie cookie = CookieHelper.getCookieFromRequest(
					Constants.LOGIN_COOKIE_NAME, request);
			if (cookie == null)
				return null;
			boolean validateCookie = userService.validateUserCookie(cookie);
			if (validateCookie) {
				String[] params = cookie.getValue().split("-");
				int userId = Integer.valueOf(params[0]);
				User user = userService.getUserById(userId);
				request.getSession(true).setAttribute(Constants.USER, user);
				// HttpSessionUtil.getSession(true).
				request.getSession(true).setAttribute(Constants.SESSION_URL,
						request.getRequestURI());
				return request.getSession(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
