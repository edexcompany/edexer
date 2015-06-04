package com.edexer.mbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.atmosphere.util.StringEscapeUtils;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.User;

@ManagedBean
@RequestScoped
public class checkNotificationManagedBean {
	public EventBus eventBus;
	public String session_id;
	public String channel;

	public synchronized void makePush(User reciver,Integer count) {
		session_id = reciver.getUserId().toString();
		eventBus = EventBusFactory.getDefault().eventBus();
		List<Integer> list = Constants.PUSH_RECEIVERS;	
		
		if (list.contains(reciver.getUserId())) {
			try {
				list.remove(reciver.getUserId());
				String summry = "Request Status";
				String detail = "you have "+count+" new request(s).Receiver --> "+session_id;
				FacesMessage message=new FacesMessage(StringEscapeUtils.escapeJava(summry),StringEscapeUtils.escapeJava(detail));
				String CHANNEL="/{"+session_id+"}/{"+session_id+"}";
				System.out.println(CHANNEL);
				eventBus.publish(CHANNEL,message);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

	public void notificationPUSH() {
		String summry = "Request Status";
		String detail = "you have new request(s).";
		String channel = "/events";
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		try {
			eventBus.publish(channel,
					new FacesMessage(StringEscapeUtils.escapeJava(summry),
							StringEscapeUtils.escapeJava(detail)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
