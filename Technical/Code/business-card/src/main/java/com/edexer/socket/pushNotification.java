package com.edexer.socket;

import javax.faces.application.FacesMessage;

import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;
//@PushEndpoint("/{room}/{user}")
//@Singleton
public class pushNotification {
	@PathParam("room")
	private String sessionId;
	@PathParam("user")
	private String user;
	

	@OnMessage(encoders = { JSONEncoder.class })
	public FacesMessage onMessage(FacesMessage pushData) {
		return  pushData;
	}

	@OnOpen
	public void onOpen(RemoteEndpoint r, EventBus eventBus) {
		if (sessionId == null)
			System.out
					.println("<ERROR> Push connection not recognized, sessionId got <null>");
		else
			System.out.println("Push connection opened with sessionId "
					+ sessionId+user);

	}

	@OnClose
	public void onClose(RemoteEndpoint r, EventBus eventBus) {
		if (sessionId == null)
			System.out.println("Push connection with null sessionId closed");
		else
			System.out.println("Push connection closed with sessionId "
					+ sessionId+user);
	}

}
