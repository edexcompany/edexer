package com.edexer.mbeans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
//import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@ManagedBean
@RequestScoped
public class NotifyManagedBean {
	private final static String CHANNEL = "/notify";

	private String summary;

	private String detail;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	 public void send() {
	 EventBus eventBus = EventBusFactory.getDefault().eventBus();
	 //eventBus.publish(CHANNEL, new FacesMessage(("rgthjuyt"), ("grehyju5y")));
	 }

}
