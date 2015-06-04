package com.edexer.mbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.BusinessCardServiceManager;

/**
 * @author Mostafa
 *
 */
@ViewScoped
@ManagedBean
public class BusinessCardImportManagedBean implements Serializable{
	private UploadedFile file;
	private StreamedContent downloadFile;
	private ResourceBundle settingsBundle;
	private User userFromSession;
	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;
	private Set<UserSubscription> userSubscriptions;
	private boolean isPersonal;
	private boolean flagForSubscriptionChooiceRender;
	private boolean flagForUpload = true;
	private String subType;
	ResourceBundle settings = ResourceBundle.getBundle("settings");

	@PostConstruct
	public void init() {
		userFromSession = getUserFromSession();

		File file = new File(settings.getString("UPLOAD_PATH"),
				settings.getString("IMPORT_XLSX_TEMPLATE"));

		// InputStream stream = ((ServletContext) FacesContext
		// .getCurrentInstance().getExternalContext().getContext())
		// .getResourceAsStream("/resources/template.xlsx");
		InputStream stream;
		try {
			stream = new FileInputStream(file);
			downloadFile = new DefaultStreamedContent(stream,
					"application/xls", "template.xlsx");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		userSubscriptions = userFromSession.getUserSubscriptionsForUserId();
		UserSubscription freeUserSub = getUserSubscriptionByType(
				userSubscriptions, Constants.FREE_SUBSCRIB_ID);
		UserSubscription corpUserSub = getUserSubscriptionByType(
				userSubscriptions, Constants.CORP_SUBSCRIB_ID);
		if (freeUserSub != null && corpUserSub != null) {
			this.flagForSubscriptionChooiceRender = true;
			this.flagForUpload = false;
		} else {
			this.isPersonal = true;
		}

	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public BusinessCardServiceManager getBcService() {
		return bcService;
	}

	public void setBcService(BusinessCardServiceManager bcService) {
		this.bcService = bcService;
	}

	public StreamedContent getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(StreamedContent downloadFile) {
		this.downloadFile = downloadFile;
	}

	public boolean isPersonal() {
		return isPersonal;
	}

	public void setPersonal(boolean isPersonal) {
		this.isPersonal = isPersonal;
	}

	public boolean isFlagForSubscriptionChooiceRender() {
		return flagForSubscriptionChooiceRender;
	}

	public void setFlagForSubscriptionChooiceRender(
			boolean flagForSubscriptionChooiceRender) {
		this.flagForSubscriptionChooiceRender = flagForSubscriptionChooiceRender;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public boolean isFlagForUpload() {
		return flagForUpload;
	}

	public void setFlagForUpload(boolean flagForUpload) {
		this.flagForUpload = flagForUpload;
	}

	public void upload(FileUploadEvent event) {
		settingsBundle = ResourceBundle.getBundle("/settings");

		try {
			File targetFolder = new File(
					settingsBundle.getString("UPLOAD_PATH")+settingsBundle.getString("BUFFER_UPLOAD_PATH"));
			InputStream inputStream = event.getFile().getInputstream();

			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = fmt.format(new Date())
					+ Math.random()
					+ event.getFile()
							.getFileName()
							.substring(
									event.getFile().getFileName()
											.lastIndexOf('.'));

			FileOutputStream out = new FileOutputStream(new File(targetFolder,
					fileName));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			inputStream.close();
			out.flush();
			out.close();
			File outFile = new File(targetFolder, fileName);
			if (flagForSubscriptionChooiceRender == true) {
				if (this.subType.equalsIgnoreCase("personal")) {
					this.isPersonal = true;
				} else {
					this.isPersonal = false;
				}

			}
			bcService.uploadWorkbook(
					outFile,
					event.getFile()
							.getFileName()
							.substring(
									event.getFile().getFileName()
											.lastIndexOf('.') + 1),
					userFromSession, isPersonal);
			closeDialog();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private User getUserFromSession() {
		HttpSession session = HttpSessionUtil.getSession();
		User u = (User) session.getAttribute(Constants.USER);
		return u;
	}

	private UserSubscription getUserSubscriptionByType(
			Set<UserSubscription> userSubscrptionSet, Integer subScriptionType) {
		if (userSubscrptionSet == null || userSubscrptionSet.size() < 1) {
			return null;
		}
		for (UserSubscription userSub : userSubscrptionSet) {
			if (subScriptionType.equals(userSub.getId().getSubType())) {
				return userSub;
			}
		}
		return null;

	}

	public void selectRadioItem() {
		this.flagForUpload = true;
	}

	public void closeDialog() throws IOException {
		FacesMessage msg = new FacesMessage("Processing");

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null, msg);
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("index.xhtml");

	}
}
