package com.edexer.faces.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.edexer.mbeans.Constants;
import com.edexer.mbeans.UtilitiesManagesBean;

public class FacesUtil {

	public static void addInfoMessage(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_INFO, summary, detail);
	}

	public static void addErrorMessage(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
	}

	public static void addWarnMessage(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_WARN, summary, detail);
	}

	private static void addMessage(FacesMessage.Severity serverity,
			String summary, String detail) {
		FacesMessage message = new FacesMessage(serverity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static String redirectToPage(String url) {
		String baseUrl = UtilitiesManagesBean.baseUrl;
		//String baseUrl = "/" + settingsBundle.getString("APPLICATION_ROOT");
		String absolutePath = baseUrl + url;
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(absolutePath);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			addInfoMessage("Navigation error occured, Please contact administration.", "Technical Error");
			return e.getMessage();
		}
	}
}
