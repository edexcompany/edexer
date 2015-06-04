package com.edexer.servlets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@WebServlet("/paypal/*")
public class PaypalTest extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String selector = request.getPathInfo().substring(1);
		if (selector.equals("pay")) {
			// case send payment request
			System.out.println("Amount: " +request.getParameter("amount"));
			JSONObject payResponse = excutePayPost(request.getParameter("amount"));

			try {
				JSONObject responseEnvelope = payResponse
						.getJSONObject("responseEnvelope");
				System.out
						.println("Acknowlege: " + responseEnvelope.get("ack"));
				
				if (responseEnvelope.get("ack").equals("Success")) {
					String payKey = (String) payResponse.get("payKey");
					System.out.println("Pay Key: " + payKey);
					response.sendRedirect("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey="
							+ payKey);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private JSONObject excutePayPost(String amount) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(
					"https://svcs.sandbox.paypal.com/AdaptivePayments/Pay");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			// Sandbox API credentials for the API Caller account
			connection.setRequestProperty("X-PAYPAL-SECURITY-USERID",
					"saad.saleh-facilitator_api1.meshreq.com");
			connection.setRequestProperty("X-PAYPAL-SECURITY-PASSWORD",
					"EGG7W43GS2F2YAV5");
			connection.setRequestProperty("X-PAYPAL-SECURITY-SIGNATURE",
					"ARDakvJtOA8YbHhqKIc19y3XHps3AANrSUktCJdx3JRtpbqrpe7MK8uY");

			// Global Sandbox Application ID
			connection.setRequestProperty("X-PAYPAL-APPLICATION-ID",
					"APP-80W284485P519543T");
			// : APP-80W284485P519543T

			// Input and output formats
			connection.setRequestProperty("X-PAYPAL-REQUEST-DATA-FORMAT",
					"JSON");
			connection.setRequestProperty("X-PAYPAL-RESPONSE-DATA-FORMAT",
					"JSON");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Send request
			OutputStream os = connection.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write(formatPayload(amount).toString());
			pw.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();

			System.out.println("Response:");
			System.out.println(response.toString());

			return new JSONObject(response.toString());

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public JSONObject formatPayload(String amount) throws JSONException {
		JSONObject receiversList = new JSONObject();
		JSONObject receiver = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONObject requestEnvelope = new JSONObject();
		requestEnvelope.put("errorLanguage", "en_US");// Language used to //
														// display errors
		requestEnvelope.put("detailLevel", "ReturnAll");// Error detail level

		receiver.put("amount", amount);
		receiver.put("email", "saad.saleh-facilitator@meshreq.com");
		List<JSONObject> list = new ArrayList<JSONObject>();
		list.add(receiver);
		receiversList.put("receiver", list);

		// failure
		obj.put("cancelUrl", "http://localhost:8080/business-card/failure.jsp");
		obj.put("requestEnvelope", requestEnvelope);

		// successful
		obj.put("returnUrl", "http://localhost:8080/business-card/success.jsp");

		obj.put("receiverList", receiversList);
		obj.put("currencyCode", "USD");
		obj.put("actionType", "PAY");

		System.out.print(obj);
		return obj;
	}
}
