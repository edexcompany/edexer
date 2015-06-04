package com.edexer.mbeans;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.service.MailConfigServiceManager;
import com.edexer.service.UserServiceManager;

@ManagedBean
@SessionScoped
public class TemplateImportManagedBean {
	ResourceBundle settings = ResourceBundle.getBundle("settings");
	FacesContext facesContext;
	@ManagedProperty("#{userService}")
	private UserServiceManager userManager;
	@ManagedProperty("#{mailConfigServiceManagerImpl}")
	private MailConfigServiceManager mailConfigService;

	private UploadedFile htmlFile;
	private UploadedFile zipFile;
	private String fileName;
	private Map<String, String> templates;
	private String selectedTemplateName;
	private User user;
	private List<BusinessCard> listBCards;
	private String Subject;

	@PostConstruct
	public void init() {
		templates = new HashMap<String, String>();
		getHtmlTemplates();
	}

	private User getUserFromSession() {
		HttpSession session = HttpSessionUtil.getSession();
		User u = (User) session.getAttribute(Constants.USER);
		return u;
	}

	public void uploadHtml(FileUploadEvent event) throws IOException {
		if (event != null) {
			FacesMessage message;
			String ext = event.getFile().getContentType();
			if (ext != null) {
				if (ext.equals("text/html")) {
					importHtmlFile(event);
					templates.clear();
					getHtmlTemplates();
				} else {
					message = new FacesMessage("Error In Upload",
							"File type is not supported!");
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
			} else {
				message = new FacesMessage("Choose Template", "Choose Template");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
	}

	public void uploadZip(FileUploadEvent event) throws IOException {
		if (event != null) {
			String ext = event.getFile().getContentType();
			if (ext != null) {
				if (ext.equals("application/octet-stream")) {
					importZipFile(event);
					templates.clear();
					getHtmlTemplates();
				} else {
					facesContext = FacesContext.getCurrentInstance();
					facesContext.getExternalContext().getFlash()
							.setKeepMessages(true);
					facesContext.addMessage(null, new FacesMessage(
							"Error In Upload", "File type is not supported!"));
				}
			} else {
				facesContext = FacesContext.getCurrentInstance();
				facesContext.getExternalContext().getFlash()
						.setKeepMessages(true);
				facesContext.addMessage(null, new FacesMessage(
						"Choose Template", "Choose Template"));
			}
		}
	}

	/*
	 * import html file and save it into bcfiles --> template
	 */
	public void importHtmlFile(FileUploadEvent event) {
		InputStream inputStream = null;
		FileOutputStream out = null;

		try {
			String templateDir = settings.getString("UPLOAD_PATH")
					+ settings.getString("UPLOAD_PATH_FOR_TEMPLATE");
			fileName = event.getFile().getFileName();
			User user = getUserFromSession();
			// chk if file exist or not
			String userIdFileDir = templateDir + user.getUserId();
			File dirForUserInSession = new File(userIdFileDir);
			if (!dirForUserInSession.exists()) {
				dirForUserInSession.mkdir();
			}
			dirForUserInSession.mkdirs();

			inputStream = event.getFile().getInputstream();
			out = new FileOutputStream(new File(dirForUserInSession, fileName));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage(
					"Html Template Saved",
					"Html Template Uploaded Succesfully!"));

		} catch (IOException e1) {
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage("Error",
					"Error in Upload,Please Try Again!"));
			e1.printStackTrace();
		}
	}

	/*
	 * importing zip files and extract it ..
	 */
	public void importZipFile(FileUploadEvent event) {
		ZipInputStream zin = null;
		InputStream fis = null;
		try {
			String templateDir = settings.getString("UPLOAD_PATH")
					+ settings.getString("UPLOAD_PATH_FOR_TEMPLATE");
			fileName = event.getFile().getFileName();
			User user = getUserFromSession();

			// chk if file exist or not
			String userIdFileDir = templateDir + user.getUserId();
			File dirForUserInSession = new File(userIdFileDir);
			if (!dirForUserInSession.exists()) {
				dirForUserInSession.mkdir();
			}
			dirForUserInSession.mkdirs();

			// get Files from Zip file
			fis = event.getFile().getInputstream();
			zin = new ZipInputStream(fis);
			ZipEntry entry = zin.getNextEntry();
			while (entry != null) {
				String filePath = userIdFileDir + File.separator
						+ entry.getName();
				if (!entry.isDirectory()) {
					extractFile(zin, filePath);
				} else {
					File dir = new File(filePath);
					dir.mkdir();
				}
				zin.closeEntry();
				entry = zin.getNextEntry();
			}
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage(
					"ZIP File Template Saved",
					"ZIP File Template Uploaded Succesfully!"));
		} catch (Exception ex) {
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage("Error",
					"Error in Upload,Please Try Again!"));
			ex.printStackTrace();
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void extractFile(ZipInputStream zipIn, String filePath)
			throws IOException {
		if (zipIn != null && filePath != null && !filePath.equals("")) {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(filePath));
			int BUFFER = 2048;
			byte data[] = new byte[BUFFER];
			int read = 0;
			while ((read = zipIn.read(data)) != -1) {
				bos.write(data, 0, read);
			}
			bos.close();
		}
	}

	public void getHtmlTemplates() {
		try {

			readHtmlFile();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void readHtmlFile() throws IOException {
		InputStream stream = null;
		try {
			User user = getUserFromSession();
			String templateDir = settings.getString("UPLOAD_PATH")
					+ settings.getString("UPLOAD_PATH_FOR_TEMPLATE")
					+ user.getUserId();
			File tempDir = new File(templateDir);
			if (tempDir.exists()) {
				tempDir.mkdir();
				File[] listOfFiles = tempDir.listFiles();
				if (listOfFiles != null && listOfFiles.length > 0) {
					for (int i = 0; i < listOfFiles.length; i++) {
						File file = listOfFiles[i];
						if (file.isFile()) {
							if (file.getName().endsWith(".html")) {
								templates.put(file.getName(), file.getPath());
								// writeHtmlOnPage(file);
							} else {

							}
						} else {
							File[] listOfFilesTwo = file.listFiles();
							if (listOfFilesTwo != null
									&& listOfFilesTwo.length > 0) {
								for (int j = 0; j < listOfFilesTwo.length; j++) {
									File fileTwo = listOfFilesTwo[j];
									if (fileTwo.isFile()) {
										if (fileTwo.getName().endsWith(".html")) {
											templates.put(fileTwo.getName(),
													fileTwo.getPath());
											// writeHtmlOnPage(fileTwo);
										} else {

										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stream != null)
				stream.close();
		}
	}

	public void writeHtmlOnPage() {
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("text/html");
		PrintWriter outWriter;

		if (selectedTemplateName != null && !selectedTemplateName.equals("")) {
			File file = new File(selectedTemplateName);
			if (file != null && file.isFile() && file.exists()) {
				String content;
				try {
					content = FileUtils.readFileToString(file);
					outWriter = response.getWriter();
					outWriter.println(content);
					outWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String contentToSendEmail() {
		String content = "";
		try {
			if (selectedTemplateName != null
					&& !selectedTemplateName.equals("")) {
				File file = new File(selectedTemplateName);
				if (file != null && file.isFile() && file.exists()) {
					content = FileUtils.readFileToString(file);
				}
			}
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void Clear() {
		selectedTemplateName = "-1";
		Subject="";
	}

	public void sendTemplateEmail() {
		try {
			user = HttpSessionUtil.getUser();
			listBCards = (List<BusinessCard>) HttpSessionUtil.getSession()
					.getAttribute(
							Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);
			if (listBCards.size() > 0) {
				String Sub = Subject;
				String Body = "";
				if (contentToSendEmail() != null)
					Body = contentToSendEmail();
				mailConfigService.sendEmail(user, Sub, Body, listBCards);
				Clear();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			FacesUtil
					.addErrorMessage(
							"Error Sending Email",
							"Error occured while sending email. Please contact your email service provider for details.");
		}

	}

	public UploadedFile getZipFile() {
		return zipFile;
	}

	public void setZipFile(UploadedFile zipFile) {
		this.zipFile = zipFile;
	}

	public Map<String, String> getTemplates() {
		return templates;
	}

	public void setTemplates(Map<String, String> templates) {
		this.templates = templates;
	}

	public String getSelectedTemplateName() {
		return selectedTemplateName;
	}

	public void setSelectedTemplateName(String selectedTemplateName) {
		this.selectedTemplateName = selectedTemplateName;
	}

	public UploadedFile getHtmlFile() {
		return htmlFile;
	}

	public void setHtmlFile(UploadedFile file) {
		this.htmlFile = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public UserServiceManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserServiceManager userManager) {
		this.userManager = userManager;
	}

	public MailConfigServiceManager getMailConfigService() {
		return mailConfigService;
	}

	public void setMailConfigService(MailConfigServiceManager mailConfig) {
		this.mailConfigService = mailConfig;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<BusinessCard> getListBCards() {
		return listBCards;
	}

	public void setListBCards(List<BusinessCard> listBCards) {
		this.listBCards = listBCards;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}
}
