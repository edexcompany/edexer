package com.edexer.mbeans;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.Countries;
import com.edexer.model.Sector;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.ReportingServicesManager;
import com.edexer.service.UserSubscriptionServiceManager;

@ManagedBean
@SessionScoped
public class ReportingManagedBean {

	@ManagedProperty("#{reportingServiceManager}")
	private ReportingServicesManager reportingService;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubscriptionService;

	ResourceBundle settingsBuldle = ResourceBundle.getBundle("settings");
	private StreamedContent report;
	private Countries reportCountry;
	private Sector reportSector;

	public StreamedContent getReport() {

		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String subscriptionType = params.get("subType");
		User userFromSession = (User) HttpSessionUtil.getSession()
				.getAttribute(Constants.USER);

		@SuppressWarnings("unchecked")
		UserSubscription userSubscription = userSubscriptionService
				.getUserSubscriptionByType(
						userFromSession.getUserSubscriptionsForUserId(),
						subscriptionType.equals(settingsBuldle
								.getString("SUBSCRIPTION_TYPE_CORP")) ? Integer
								.valueOf(settingsBuldle
										.getString("SUBSCRIPTION_TYPE_CORP"))
								: 0);

		// report = new DefaultStreamedContent(stream, "image/jpg",
		// "report.jpg");

		try {
			if (reportSector == null)
				if (reportCountry == null)
					report = reportingService
							.getBCCountPerSector(userSubscription);
				else
					report = reportingService.getBCCountPerSector(
							userSubscription, reportCountry);
			else
				report = reportingService.getTitleCountGivenCountryAndSector(
						userSubscription, reportCountry, reportSector);
			StreamedContent sc = new DefaultStreamedContent(
					report.getStream(),
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
					"report.xlsx");
			return sc;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			FacesUtil.addErrorMessage("Error while running the report",
					"An error occured while running the report.");
		}

		return report;
	}

	public void setReport(StreamedContent report) {
		this.report = report;
	}

	public Countries getReportCountry() {
		return reportCountry;
	}

	public void setReportCountry(Countries reportCountry) {
		this.reportCountry = reportCountry;
	}

	public Sector getReportSector() {
		return reportSector;
	}

	public void setReportSector(Sector reportSector) {
		this.reportSector = reportSector;
	}

	public ReportingServicesManager getReportingService() {
		return reportingService;
	}

	public void setReportingService(ReportingServicesManager reportingService) {
		this.reportingService = reportingService;
	}

	public UserSubscriptionServiceManager getUserSubscriptionService() {
		return userSubscriptionService;
	}

	public void setUserSubscriptionService(
			UserSubscriptionServiceManager userSubscriptionService) {
		this.userSubscriptionService = userSubscriptionService;
	}

}
