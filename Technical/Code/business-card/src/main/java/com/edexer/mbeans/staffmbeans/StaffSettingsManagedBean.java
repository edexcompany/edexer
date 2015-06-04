package com.edexer.mbeans.staffmbeans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.edexer.service.AdminConfigManager;

@ManagedBean
@ViewScoped
public class StaffSettingsManagedBean {
	@ManagedProperty("#{adminConfigManager}")
	private AdminConfigManager adminServiceManager;

	private String maxRecordState;
	private String registrationState;

	@PostConstruct
	public void init() {
		try {
			maxRecordState = adminServiceManager
					.getAdminConfigValueByKey("WORKBOOK_UPLOAD_MAX");
			registrationState = adminServiceManager
					.getAdminConfigValueByKey("Regstration_State");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void save() {
		try {
			if(!maxRecordState.equals("0") ||!maxRecordState.equals("") )
			adminServiceManager.updateMaxRecordsCount(Integer
					.parseInt(maxRecordState));
			if (!registrationState.equals("-1")) {
				adminServiceManager.updateRegistrationState(registrationState);
			}
			FacesMessage msg = new FacesMessage("Configuration Changed");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			maxRecordState = "";
			registrationState = "";
			Clear();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void Clear() {
		setMaxRecordState("");
		setRegistrationState("");
	}

	public AdminConfigManager getAdminServiceManager() {
		return adminServiceManager;
	}

	public void setAdminServiceManager(AdminConfigManager adminServiceManager) {
		this.adminServiceManager = adminServiceManager;
	}

	public String getMaxRecordState() {
		return maxRecordState;
	}

	public void setMaxRecordState(String maxRecordState) {
		this.maxRecordState = maxRecordState;
	}

	public String getRegistrationState() {
		return registrationState;
	}

	public void setRegistrationState(String registrationState) {
		this.registrationState = registrationState;
	}

}
