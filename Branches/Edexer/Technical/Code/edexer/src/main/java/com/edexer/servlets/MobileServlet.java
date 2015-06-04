package com.edexer.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedProperty;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.edexer.dao.FiltersKeys;
import com.edexer.mbeans.Constants;
import com.edexer.model.BusinessCard;
import com.edexer.model.Email;
import com.edexer.model.Fax;
import com.edexer.model.Im;
import com.edexer.model.Mobile;
import com.edexer.model.Phone;
import com.edexer.model.SocialNetwork;
import com.edexer.model.Title;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.model.Website;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;
import com.edexer.util.FiltersUtils;
import com.google.gson.JsonObject;

@WebServlet("/mobile")
public class MobileServlet extends HttpServlet {

	public static final String BC_LIST_REQUEST_NAME = "bcListRequest";
	public static final String BC_DETAILS_REQUEST_NAME = "bcDetailsRequest";
	public static final String BC_LIST_RESPONSE_NAME = "bcListResponse";
	public static final String BC_DETAILS_RESPONSE_NAME = "bcDetailsResponse";

	@Autowired
	UserServiceManager userService;
	@Autowired
	BusinessCardServiceManager bcService;
	@Autowired
	UserSubscriptionServiceManager userSubscriptionService;

	ResourceBundle settings = ResourceBundle.getBundle("settings");

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		StringBuffer jb = new StringBuffer();
		JSONObject jsonObject;
		String line = null;
		BufferedReader reader;
		try {
			reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) { /* report an error */
			// TODO error while reading request
		}
		response.setContentType("application/json");
		// response.addHeader("Access-Control-Allow-Origin","*");
		JSONObject responseObject = null;
		try {
			jsonObject = new JSONObject(jb.toString());
			System.out.println("Request: " + jsonObject);
			if (jb.toString().contains(BC_LIST_REQUEST_NAME)) {
				responseObject = processListRequest(jsonObject);
			} else if (jb.toString().contains(BC_DETAILS_REQUEST_NAME)) {
				responseObject = processDetailsRequest(jsonObject);
			} else {
				responseObject = composeFailureResponse("",
						"invalid request type");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PrintWriter out = response.getWriter();
			out.print(responseObject.toString());
		}

	}

	private JSONObject processListRequest(JSONObject jsonRequest)
			throws JSONException, IOException {
		String email = jsonRequest.getJSONObject("bcListRequest")
				.getJSONObject("Login").getString("email");
		String password = jsonRequest.getJSONObject("bcListRequest")
				.getJSONObject("Login").getString("password");
		User user = userService.getUser(email, password);
		if (user == null) {
			// TODO
			return composeFailureResponse(BC_LIST_RESPONSE_NAME,
					"invalid user name or password");
		} else {
			System.out.println("User validated");
			Map<String, Object> filters = initializeFilters(user);
			List<BusinessCard> bcList = bcService.advancedFilter(0, 5000,
					FiltersKeys.ORDER_BY_FNAME_ASC, filters, false, true, true);
			return composeBcList(bcList);

		}

	}

	private JSONObject processDetailsRequest(JSONObject jsonRequest)
			throws JSONException {
		String email = jsonRequest.getJSONObject(BC_DETAILS_REQUEST_NAME)
				.getJSONObject("Login").getString("email");
		String password = jsonRequest.getJSONObject(BC_DETAILS_REQUEST_NAME)
				.getJSONObject("Login").getString("password");
		User user = userService.getUser(email, password);
		if (user == null) {
			return composeFailureResponse(BC_DETAILS_RESPONSE_NAME,
					"invalid user name or password");
		} else {
			System.out.println("User validated");
			int bcId = Integer.valueOf(jsonRequest.getJSONObject(
					BC_DETAILS_REQUEST_NAME).getString("bcid"));
			String cardString = bcToJson4Details(bcId);
			if (cardString == null) {
				return composeFailureResponse(BC_DETAILS_REQUEST_NAME,
						"Card not found");
			}
			StringBuffer sb = new StringBuffer(
					"{ bcDetailsResponse:{ \"status\":\"success\", \"error\":\"\",\"businesscard\":"
							+ cardString + "} }");
			return new JSONObject(sb.toString());
		}
	}

	private Map<String, Object> initializeFilters(User user) {
		UserSubscription corpSubscription = userSubscriptionService
				.getUserSubscriptionByType(
						user.getUserSubscriptionsForUserId(), Integer
								.parseInt(settings
										.getString("SUBSCRIPTION_TYPE_CORP")));
		int ownerId = (corpSubscription == null ? 0 : corpSubscription
				.getUserBySubscriptionOwner().getUserId());
		Map<String, Object> filters = FiltersUtils.constructFilter(
				user.getUserId(), 7, "", "", "", "", "", "", "", "",
				FiltersKeys.ORDER_BY_ID_ASC, ownerId, "", "", "", "");
		filters.put(FiltersKeys.FIND_ON_KEY, true);
		filters.put(FiltersKeys.PERSONAL_KEY, true);
		filters.put(FiltersKeys.CORP_KEY, true);
		return filters;
	}

	private JSONObject composeBcList(List<BusinessCard> list)
			throws JSONException {
		StringBuffer objectString = new StringBuffer(
				"{ bcListResponse:{\"status\":\"success\",\"error\":\"\",\"cardslist\":[");
		for (int i = 0; i < list.size(); i++) {
			objectString.append(bcToJson4List(list.get(i)));
			if ((list.size() - i) != 1)
				objectString.append(",");
		}
		objectString.append("]}}");
		System.out.println("Object String: ");
		System.out.println(objectString.toString());
		return new JSONObject(objectString.toString());
	}

	private String bcToJson4Details(int bcId) {
		BusinessCard bc = bcService.get(bcId, true);
		if (bc == null)
			return null;
		StringBuffer sb = new StringBuffer("{\"ims\":[");
		Iterator it = bc.getIms().iterator();
		while (it.hasNext()) {
			Im im = (Im) it.next();
			sb.append("{\"id\":\"" + im.getId().getIdentifier()
					+ "\",\"network\":\"" + im.getNetwork() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"emails\":[");
		it = bc.getEmails().iterator();
		while (it.hasNext()) {
			Email email = (Email) it.next();
			sb.append("{\"email\":\"" + email.getId().getEmailAddress() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"websites\":[");
		it = bc.getWebsites().iterator();
		while (it.hasNext()) {
			Website website = (Website) it.next();
			sb.append("{\"website\":\"" + website.getId().getWebsite() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"phones\":[");
		it = bc.getPhones().iterator();
		while (it.hasNext()) {
			Phone phone = (Phone) it.next();
			sb.append("{\"phone\":\"" + phone.getId().getPhoneNum() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"businessCardId\":" + bc.getBusinessCardId()
				+ ",\"bcFirstName\":\"" + bc.getBcFirstName()
				+ "\",\"faxes\":[");
		it = bc.getFaxes().iterator();
		while (it.hasNext()) {
			Fax fax = (Fax) it.next();
			sb.append("{\"fax\":\"" + fax.getId().getFaxNum() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"address\":{\"steet1\":\"" + bc.getAddress().getStreet1()
				+ "\",\"street2\":\"" + bc.getAddress().getStreet2()
				+ "\",\"state\":\"" + bc.getAddress().getState()
				+ "\",\"city\":\"" + bc.getAddress().getCity()
				+ "\",\"country\":\""
				+ bc.getAddress().getCountries().getCountryName()
				+ "\",\"zip\":\"" + "\"},\"mobiles\":[");
		it = bc.getMobiles().iterator();
		while (it.hasNext()) {
			Mobile mobile = (Mobile) it.next();
			sb.append("{\"mobile\":\"" + mobile.getId().getMobileNum() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"titles\":[");
		it = bc.getTitles().iterator();
		while (it.hasNext()) {
			Title title = (Title) it.next();
			sb.append("{\"company\":\"" + title.getCompany()
					+ "\",\"department\":\"" + title.getDepartment()
					+ "\",\"sector\":\"" + title.getSector()
					+ "\",\"title\":\"" + title.getTitle() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"socialNetworks\":[");
		it = bc.getSocialNetworks().iterator();
		while (it.hasNext()) {
			SocialNetwork sn = (SocialNetwork) it.next();
			sb.append("{\"mobile\":\"" + sn.getId().getIdentifier() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"notes\":\"" + bc.getNotes() + "\",\"bcLastName\":\""
				+ bc.getBcLastName() + "\"}");
		return sb.toString();
	}

	private String bcToJson4List(BusinessCard bc1) {
		BusinessCard bc = bcService.get(bc1.getBusinessCardId(), true);
		StringBuffer sb = new StringBuffer("{\"bcFirstName\":\"");
		sb.append(bc.getBcFirstName() + "\",\"bcLastName\":\"");
		sb.append(bc.getBcLastName() + "\",");
		sb.append("\"emails\":[");
		Iterator it = bc.getEmails().iterator();
		while (it.hasNext()) {
			Email email = (Email) it.next();
			sb.append("{\"email\":\"" + email.getId().getEmailAddress() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"businessCardId\":" + bc.getBusinessCardId() + ",");
		sb.append("\"mobiles\":[");
		it = bc.getMobiles().iterator();
		while (it.hasNext()) {
			Mobile mobile = (Mobile) it.next();
			sb.append("{\"mobile\":\"" + mobile.getId().getMobileNum() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("],\"titles\":[");
		it = bc.getTitles().iterator();
		while (it.hasNext()) {
			Title title = (Title) it.next();
			sb.append("{\"title\":\"" + title.getTitle() + "\",\"company\":\""
					+ title.getCompany() + "\"}");
			if (it.hasNext())
				sb.append(",");
		}
		sb.append("] }");

		return sb.toString();
	}

	private JSONObject composeFailureResponse(String requestName, String error)
			throws JSONException {
		return new JSONObject("{ " + requestName
				+ ":{\"status\":\"failure\",\"error\":\"" + error + "\"}}");
	}
}
