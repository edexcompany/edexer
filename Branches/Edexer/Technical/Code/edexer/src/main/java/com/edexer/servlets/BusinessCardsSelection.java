package com.edexer.servlets;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.mbeans.Constants;
import com.edexer.model.BusinessCard;

@WebServlet("/selectioncheck/*")
public class BusinessCardsSelection extends HttpServlet {
	private ResourceBundle settingsBundle;
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		List<BusinessCard> bcList = (List<BusinessCard>) session
				.getAttribute(Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);
		int id = Integer.valueOf(request.getPathInfo().substring(1));
		boolean found = false;
		if (bcList != null)
			for (BusinessCard businessCard : bcList) {
				if (businessCard.getBusinessCardId() == id) {
					found = true;
					break;
				}
			}
		response.getOutputStream().print(found);
		response.getOutputStream().flush();
	}
}
