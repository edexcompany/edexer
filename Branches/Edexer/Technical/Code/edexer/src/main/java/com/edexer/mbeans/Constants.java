package com.edexer.mbeans;

/**
 * This class used to hold the constant values used in the applications
 * 
 * @author Karim
 *
 */
public class Constants {

	/**
	 * Used to be returned in case of error occured in any managed bean, to be
	 * used in the navigations rules.
	 */
	public static final String ERROR = "error";

	/**
	 * Used to be returned in case of normal user has been signed in or
	 * registered.
	 */
	public static final String USER = "user";

	/**
	 * Used to be returned in case of staff user logged in.
	 */
	public static final String STAFF = "staff";

	/**
	 * Used to be returned in case of admin user logged in.
	 */
	public static final String ADMIN = "admin";

	/**
	 * used to be returned in case of bean want to navigate to the index page.
	 */
	public static final String INDEX = "index";
	public static final String ACTION_LOGOUT = "actionlogout";

	public static final String ADD_NEW_CARD = "newcard";

	public static final String FATAL_ERROR = "FATAL ERROR!";

	public static final String LOGIN_ERROR = "Login ERROR!";

	public static final Integer FREE_SUBSCRIB_ID = 1;
	// public static final String UPLOAD_PATH = "E://BcFiles//";
	public static final Integer PRO_SUBSCRIB_ID = 2;
	//public static final Integer CORP_SUBSCRIB_ID = 3;

	public static final Integer DEFAULT_SECTOR = 1;
	public static final String BUSINESS_CARD_SELECTED_LIST_SESSION_KEY = "BUSINESS_CARD_SELECTED_LIST_SESSION_KEY";

	public static final String NOT_AUTHORIZED = "NOT_AUTHORIZED";

	public static final String LOGIN_COOKIE_NAME = "FindOnLoginCookie";

	public static final String SKIP_COOKIE_CHECK = "SKIP_COOKIE_CHECK";

	public static final String SESSION_URL = "login_request_url";
	public static final String BUSINESS_CARD_DETAILS_PAGE = "bc_details";
	public static final int CORP_OWNER = 1;
	public static final int CORP_SUB_TYPE = 1;

	public static final String UNAUTHORIZED = "unauthorized";
}
