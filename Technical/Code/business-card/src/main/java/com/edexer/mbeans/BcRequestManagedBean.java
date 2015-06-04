package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.atmosphere.util.StringEscapeUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.BcRequest;
import com.edexer.model.BcRequestStatus;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.service.BcRequestServiceManager;
import com.edexer.service.BcRequestServiceManagerImpl;
import com.edexer.service.BcRequestStatusServiceManager;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.UserServiceManager;

@ManagedBean
@ViewScoped
public class BcRequestManagedBean {

	private String bcRequestStatusString;
	private List<BcRequestStatus> bcRequestStatus = new ArrayList<BcRequestStatus>();
	private Map<String, String> requestStatuses;
	private List<BcRequest> listSentBcRequest;
	private List<BcRequest> listReceivedBcRequest;
	private String messageFromIndex;
	private Boolean render = false;
	private String countNewReq = "0";
	@ManagedProperty("#{bcRequestServiceManagerImpl}")
	private BcRequestServiceManager bcRequestservice;

	@ManagedProperty("#{userService}")
	private UserServiceManager userServiceManager;

	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bCardserviceManager;

	@ManagedProperty("#{bcRequestStatusServiceManager}")
	private BcRequestStatusServiceManager bcRequestStatusServiceManager;

	private static final checkNotificationManagedBean checkNotifyMBeans = new checkNotificationManagedBean();

	@PostConstruct
	void init() {
		bcRequestStatus = bcRequestStatusServiceManager
				.listAllBcRequestStatus();
		getStatus();
		getSentRequests();
		getreceivedRequests();
		getNewRequestReceived();
	}

	public List<BcRequest> getSentRequests() {
		try {
			User user = HttpSessionUtil.getUser();
			if (user != null) {
				listSentBcRequest = bcRequestservice.listSentBcRequests(user);
				if (listSentBcRequest != null) {
					for (BcRequest item : listSentBcRequest) {
						if (item.getBcRequestStatus().getStatusName()
								.equals("Ignore")) {
							item.setBcRequestStatus(null);
						}
					}
				}

			}
			return listSentBcRequest;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public List<BcRequest> getreceivedRequests() {
		try {
			User user = HttpSessionUtil.getUser();
			if (user != null) {
				listReceivedBcRequest = bcRequestservice
						.listReciverBcRequests(user);

			}
			return listReceivedBcRequest;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void getStatus() {
		requestStatuses = new HashMap<String, String>();
		for (BcRequestStatus item : bcRequestStatus) {
			requestStatuses.put(item.getStatusName(), item.getStatusId()
					.toString());
		}
	}

	public Integer getNewRequestReceived() {
		try {
			User user = (User) HttpSessionUtil.getSession().getAttribute(
					Constants.USER);
			Integer count = 0;
			List<BcRequest> list = bcRequestservice.listReciverBcRequests(user);
			if (list != null) {
				for (BcRequest item : list) {
					if (item.getBcRequestStatus().getStatusName()
							.equals("Sent")) {
						count++;
					}
				}
			}
			if (count > 0) {
				render = true;
				// Constants.PUSH_RECEIVERS.add(user.getUserId());
				countNewReq = count.toString();
			}
			return count;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public void sendBcRequest(int bcId) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		try {
			if (bcId != 0) {
				if (messageFromIndex == null || messageFromIndex.equals(""))
					messageFromIndex = "Business Card Request";
				final User sender = HttpSessionUtil.getUser();
				BusinessCard bCard = bCardserviceManager.get(bcId, true);
				final User receiver = userServiceManager.getUserById(bCard
						.getCreator());
				// --check if this sender had sent request to reciver or not
				List<BcRequest> bcreq = bcRequestservice
						.listBcRequestsBySenderANDreciver(sender, receiver);
				if (bcreq != null && bcreq.size() > 0) {
					 facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Warning","you had sent a request to this user befor !"));
					 Clear();
				} else {
					new Thread() {
						public void run() {
							try {
								System.out.println(messageFromIndex);
								bcRequestservice.sendBcRequest(sender,
										receiver, messageFromIndex);
								bcRequestservice.sendEmailAfterRequest(
										receiver, messageFromIndex);
								Constants.PUSH_RECEIVERS.add(receiver
										.getUserId());
								getNewRequestReceived();
								messageFromIndex = "";
								Clear();

							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}.start();
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void Clear() {
		messageFromIndex = "";
	}

	public void onRowEdit(BcRequest bcReq) {
		if (bcRequestStatusString != null && !bcRequestStatusString.equals("")) {
			BcRequestStatus bcStatus = bcRequestStatusServiceManager
					.getBcRequestStatusById(Integer
							.parseInt(bcRequestStatusString));
			BcRequest bcRequest = bcRequestservice.getBcRequestById(bcReq
					.getBcRequestId());
			if (bcRequest != null) {
				bcRequest.setBcRequestStatus(bcStatus);
				bcRequestservice.updateBcRequest(bcRequest);
				if (bcStatus.getStatusName().equals("Accept")) {
					User sender = userServiceManager.getUserById(bcRequest
							.getUserBySenderUserId().getUserId());
					User reciver = userServiceManager.getUserById(bcRequest
							.getUserByReciverUserId().getUserId());
					bcRequestservice.exchangeCardswhenAccept(sender, reciver);
				}
				getreceivedRequests();
				getNewRequestReceived();
				FacesMessage msg = new FacesMessage("Request Edited");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

	public void onRowCancel() {
		FacesMessage msg = new FacesMessage("Request Cancelled");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public List<BcRequestStatus> getBcRequestStatus() {
		return bcRequestStatus;
	}

	public void setBcRequestStatus(List<BcRequestStatus> bcRequestStatus) {
		this.bcRequestStatus = bcRequestStatus;
	}

	public BcRequestServiceManager getBcRequestservice() {
		return bcRequestservice;
	}

	public void setBcRequestservice(BcRequestServiceManager bcRequestservice) {
		this.bcRequestservice = bcRequestservice;
	}

	public List<BcRequest> getListSentBcRequest() {
		return listSentBcRequest;
	}

	public void setListSentBcRequest(List<BcRequest> listSentBcRequest) {
		this.listSentBcRequest = listSentBcRequest;
	}

	public List<BcRequest> getListReceivedBcRequest() {
		return listReceivedBcRequest;
	}

	public void setListReceivedBcRequest(List<BcRequest> listReceivedBcRequest) {
		this.listReceivedBcRequest = listReceivedBcRequest;
	}

	public String getMessageFromIndex() {
		return messageFromIndex;
	}

	public void setMessageFromIndex(String messageFromIndex) {
		this.messageFromIndex = messageFromIndex;
	}

	public UserServiceManager getUserServiceManager() {
		return userServiceManager;
	}

	public void setUserServiceManager(UserServiceManager userServiceManager) {
		this.userServiceManager = userServiceManager;
	}

	public BusinessCardServiceManager getbCardserviceManager() {
		return bCardserviceManager;
	}

	public void setbCardserviceManager(
			BusinessCardServiceManager bCardserviceManager) {
		this.bCardserviceManager = bCardserviceManager;
	}

	public BcRequestStatusServiceManager getBcRequestStatusServiceManager() {
		return bcRequestStatusServiceManager;
	}

	public void setBcRequestStatusServiceManager(
			BcRequestStatusServiceManager bcRequestStatusServiceManager) {
		this.bcRequestStatusServiceManager = bcRequestStatusServiceManager;
	}

	public String getBcRequestStatusString() {
		return bcRequestStatusString;
	}

	public void setBcRequestStatusString(String bcRequestStatusString) {
		this.bcRequestStatusString = bcRequestStatusString;
	}

	public Map<String, String> getRequestStatuses() {
		return requestStatuses;
	}

	public void setRequestStatuses(Map<String, String> requestStatuses) {
		this.requestStatuses = requestStatuses;
	}

	public Boolean getRender() {
		return render;
	}

	public void setRender(Boolean render) {
		this.render = render;
	}

	public String getcountNewReq() {
		return countNewReq;
	}

	public void setcountNewReq(String countNewReq) {
		this.countNewReq = countNewReq;
	}
}
