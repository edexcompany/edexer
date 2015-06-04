package com.edexer.mbeans;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;


import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.dao.UserEntityDaoImpl;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.Attachment;
import com.edexer.model.BcPermissions;
import com.edexer.model.BusinessCard;
import com.edexer.model.Countries;
import com.edexer.model.Email;
import com.edexer.model.EmailId;
import com.edexer.model.Fax;
import com.edexer.model.FaxId;
import com.edexer.model.Im;
import com.edexer.model.ImId;
import com.edexer.model.Mobile;
import com.edexer.model.MobileId;
import com.edexer.model.Phone;
import com.edexer.model.PhoneId;
import com.edexer.model.Sector;
import com.edexer.model.SocialNetwork;
import com.edexer.model.SocialNetworkId;
import com.edexer.model.SocialNetworksTypes;
import com.edexer.model.Tags;
import com.edexer.model.Title;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.model.Website;
import com.edexer.model.WebsiteId;
import com.edexer.service.BcPermissionManager;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.LookUpsServiceManager;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;

@ManagedBean
@ViewScoped
public class BusinessCardDetailsManagedBean implements Serializable {

	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;

	@ManagedProperty("#{lookupsServiceManager}")
	private LookUpsServiceManager lookupService;

	@ManagedProperty("#{userService}")
	private UserServiceManager userService;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubscriptionService;
	@ManagedProperty("#{tagsManagedBean}")
	private TagsManagedBean tagsManagedBean;
	@ManagedProperty("#{bcPermissionServiceManager}")
	private BcPermissionManager bcPermissionService;

	private int bcId;
	private BusinessCard businessCard;

	private Mobile mob;
	private List<Mobile> mobileList = new ArrayList<Mobile>();
	private List<Phone> phoneList = new ArrayList<Phone>();
	private List<Fax> faxList = new ArrayList<Fax>();
	private List<Email> emailList = new ArrayList<Email>();
	private List<Title> titleList = new ArrayList<Title>();
	private List<Im> imList = new ArrayList<Im>();
	private List<Website> websiteList = new ArrayList<Website>();
	private List<SocialNetwork> socialNetworkList = new ArrayList<SocialNetwork>();
	private List<Tags> selectedTags = new ArrayList<Tags>();
	private List<BcPermissions> permissionsList = new ArrayList<BcPermissions>();
	private List<Attachment> attachementList = new ArrayList<Attachment>();
	private Attachment attachment = null;
	private ResourceBundle settingsBundle;
	private String selectedCountryId = null;
	private Countries addressCountry = new Countries();
	private UploadedFile avatar;
	private List<UploadedFile> uploads = new ArrayList<UploadedFile>();
	private List<User> selectedCorporateUsers;
	private List<User> corporateUsers;
	FacesContext facesContext;
	private static final Logger logger = Logger
			.getLogger(BusinessCardDetailsManagedBean.class);

	public BusinessCardDetailsManagedBean() {
	}

	@PostConstruct
	private void initialize() {
		System.out.println(bcId);
		settingsBundle = ResourceBundle.getBundle("/settings");

	}

	public void handleBcId() {
		System.out.println(bcId);
		// try {
		// businessCard = bcService.getUserOwnCard(((User) HttpSessionUtil
		// .getSession().getAttribute(Constants.USER)).getUserId());
		// } catch (Exception e) {
		// FacesUtil.redirectToPage("/user/index.xhtml");
		// }
		
		if (bcId != 0 /* && (bcId != businessCard.getBusinessCardId()) */) {
			businessCard = bcService.get(bcId, true);
			boolean hasPermission = bcService.checkCardViewAuthority(
					((User) HttpSessionUtil.getSession().getAttribute(
							Constants.USER)), businessCard);
			if (!hasPermission) {
				// return Constants.NOT_AUTHORIZED;
				System.out.println("Not authorized case");
				FacesUtil.redirectToPage("/unauthorized.xhtml");
				return;
			}
		}
		Iterator it = businessCard.getMobiles().iterator();
		if (!it.hasNext())
			addMobile();
		while (it.hasNext()) {
			mobileList.add((Mobile) it.next());
		}
		it = businessCard.getPhones().iterator();
		if (!it.hasNext())
			addPhone();
		while (it.hasNext()) {
			phoneList.add((Phone) it.next());
		}
		it = businessCard.getFaxes().iterator();
		if (!it.hasNext())
			addFax();
		while (it.hasNext()) {
			faxList.add((Fax) it.next());
		}
		it = businessCard.getEmails().iterator();
		if (!it.hasNext())
			addEmail();
		while (it.hasNext()) {
			emailList.add((Email) it.next());
		}
		it = businessCard.getTitles().iterator();
		if (!it.hasNext())
			addtitle();
		while (it.hasNext()) {
			titleList.add((Title) it.next());
		}
		it = businessCard.getWebsites().iterator();
		if (!it.hasNext())
			addWebsite();
		while (it.hasNext()) {
			websiteList.add((Website) it.next());
		}
		it = businessCard.getIms().iterator();
		if (!it.hasNext())
			addIm();
		while (it.hasNext()) {
			imList.add((Im) it.next());
		}
		it = businessCard.getSocialNetworks().iterator();
		if (!it.hasNext())
			addSocialNetwork();
		while (it.hasNext()) {
			socialNetworkList.add((SocialNetwork) it.next());
		}
		it = businessCard.getTagses().iterator();
		while (it.hasNext()) {
			selectedTags.add((Tags) it.next());
		}
		it = businessCard.getAttachments().iterator();
		while (it.hasNext()) {
			// attachment found
			Attachment att = (Attachment) it.next();
			setAttachment(att);
			getAttachementList().add(att);
		}
		it = businessCard.getBcPermissionses().iterator();
		if (it.hasNext()) {
			// attachment found
			permissionsList.add((BcPermissions) it.next());
		}
		// if bc is corp, handle permissions

		try {
			if (businessCard.getUserSubscription().getId().getSubType() == Integer
					.valueOf(settingsBundle.getString("SUBSCRIPTION_TYPE_CORP"))) {
				// intialize lists

				selectedCorporateUsers = new ArrayList<User>();
				// get user from session
				User user = (User) HttpSessionUtil.getSession().getAttribute(
						Constants.USER);
				// get all corporate users
				corporateUsers = userService.getAllUsers();
				User userWithPermission = null;
				for (Object bcp : businessCard.getBcPermissionses()) {

					// Hibernate.initialize(((BcPermissions) bcp)
					// .getUserSubscription());
					// userWithPermission = (User) userService
					// .getUserById(((BcPermissions) bcp)
					// .getUserSubscription().getId().getUserId());
					BcPermissions bcPermission = bcPermissionService
							.getPermission(((BcPermissions) bcp)
									.getBcPermissionsId());
					userWithPermission = userService.getUserById(bcPermission
							.getUserSubscription().getId().getUserId());
					selectedCorporateUsers.add(userWithPermission);
					corporateUsers.remove(userWithPermission);
				}
			}
		} catch (Exception e) {
			logger.info("Card details for find on card");
		}
	}

	public void handleAttachementUpload(FileUploadEvent event) {
		uploads.add(event.getFile());

	}

	public StreamedContent downloadFile(String fileName) {
		String file = settingsBundle.getString("UPLOAD_PATH") + bcId + "//"
				+ fileName;
		InputStream stream;
		try {
			stream = new FileInputStream(file);
			return new DefaultStreamedContent(stream, null, fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<User> getCorporateUsers(String query) {
		List<User> result = new ArrayList<User>();
		for (User user : corporateUsers) {
			if (user.getFirstName().toLowerCase().contains(query.toLowerCase())
					|| user.getLastName().toLowerCase()
							.contains(query.toLowerCase())) {
				result.add(user);
			}
		}
		return result;
	}

	public void handleUserSelect(SelectEvent event) {
		User user = (User) event.getObject();
		selectedCorporateUsers.add(user);
		corporateUsers.remove(user);
	}

	public void handleUserUnselect(UnselectEvent event) {
		if (selectedCorporateUsers != null) {
			User user = (User) event.getObject();
			selectedCorporateUsers.remove(user);
			corporateUsers.add(user);
		}
	}

	public String actionSave() {
		System.out.println("Action performed");
		prepareBusinessCard();
		String validation = bcService.validateBusinessCard(businessCard, true);
		// String baseUrl = UtilitiesManagesBean.baseUrl;
		// String baseUrl = "/" +
		// settingsBundle.getString("APPLICATION_ROOT");
		if (validation == null) {
			try {
				bcService.updateBusinessCard(businessCard, avatar, uploads);
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil
						.addErrorMessage("Error",
								"Error occured while uploading. Please try again later");
				facesContext = FacesContext.getCurrentInstance();
				facesContext.getExternalContext().getFlash()
						.setKeepMessages(true);
				return Constants.BUSINESS_CARD_DETAILS_PAGE;// bc_details
			}
			return Constants.INDEX;
			// String url = baseUrl + "/user/businesscard_details.xhtml?id="
			// + businessCard.getBusinessCardId();
			// try {
			// FacesContext.getCurrentInstance().getExternalContext()
			// .redirect(url);
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		} else {
			FacesUtil.redirectToPage("/user/businesscard_details.xhtml?id="
					+ businessCard.getBusinessCardId());
			FacesUtil.addErrorMessage("Error", validation);
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash()
					.setKeepMessages(true);
			/*facesContext.addMessage(null, new FacesMessage(
					));*/
			return Constants.BUSINESS_CARD_DETAILS_PAGE;
		}

	}

	@SuppressWarnings("unchecked")
	private void prepareBusinessCard() {
		businessCard.setEmails(new HashSet(0));
		businessCard.setFaxes(new HashSet(0));
		businessCard.setIms(new HashSet(0));
		businessCard.setMobiles(new HashSet(0));
		businessCard.setPhones(new HashSet(0));
		businessCard.setSocialNetworks(new HashSet(0));
		businessCard.setTagses(new HashSet(0));
		businessCard.setTitles(new HashSet(0));
		businessCard.setWebsites(new HashSet(0));
		businessCard.setAttachments(new HashSet(0));

		for (Email email : emailList) {
			if (email.getId().getEmailAddress() != null
					&& !email.getId().getEmailAddress().equals(""))
				businessCard.getEmails().add(email);
		}
		for (Fax fax : faxList) {
			if (fax.getId().getFaxNum() != null
					&& !fax.getId().getFaxNum().equals(""))
				businessCard.getFaxes().add(fax);
		}
		for (Im im : imList) {
			if (im.getId().getIdentifier() != null
					&& !im.getId().getIdentifier().equals(""))
				businessCard.getIms().add(im);
		}
		for (Mobile mobile : mobileList) {
			if (mobile.getId().getMobileNum() != null
					&& !mobile.getId().getMobileNum().equals(""))
				businessCard.getMobiles().add(mobile);
		}
		for (Phone phone : phoneList) {
			if (phone.getId().getPhoneNum() != null
					&& !phone.getId().getPhoneNum().equals(""))
				businessCard.getPhones().add(phone);
		}
		for (SocialNetwork sn : socialNetworkList) {
			if (sn.getId().getIdentifier() != null
					&& !sn.getId().getIdentifier().equals(""))
				businessCard.getSocialNetworks().add(sn);
		}
		if (selectedTags != null) {
			for (Tags tag : selectedTags) {
				if (businessCard.getTagses() == null)
					businessCard.setTagses(new HashSet(0));
				businessCard.getTagses().add(tag);
			}
		}

		for (Website website : websiteList) {
			if (website.getId().getWebsite() != null
					&& !website.getId().getWebsite().equals(""))
				businessCard.getWebsites().add(website);
		}
		for (Attachment att : getAttachementList()) {
			businessCard.getAttachments().add(att);
		}
		for (Title title : titleList) {
			if ((title.getTitle() != null && !title.getTitle().equals(""))
					&& (title.getCompany() != null && !title.getCompany()
							.equals(""))
					&& (title.getDepartment() != null && !title.getDepartment()
							.equals("")))
				System.out.println("title.getCompany() " + title.getCompany());
			System.out
					.println("title.getDepartment() " + title.getDepartment());
			System.out.println("title.getTitle() " + title.getTitle());
			System.out.println("title.getSector() " + title.getSector());
			System.out.println("title.getTitleId() " + title.getTitleId());
			businessCard.getTitles().add(title);
		}
		// Set country in address
		if (selectedCountryId != null) {
			Countries country = new Countries();
			country.setIdCountry(Integer.valueOf(selectedCountryId));
			businessCard.getAddress().setCountries(country);
		}

		if (avatar != null) {
			businessCard.setAvatar(avatar.getFileName());
		}

		// permissions
		businessCard.setBcPermissionses(new HashSet(0));
		// businessCard.getBcPermissionses().add(
		// new BcPermissions(businessCard, businessCard
		// .getUserSubscription()));
		UserSubscription us;
		if (selectedCorporateUsers != null) {
			for (User user : selectedCorporateUsers) {
				us = userSubscriptionService.getUserSubscriptionByType(user
						.getUserSubscriptionsForUserId(), Integer
						.valueOf(settingsBundle
								.getString("SUBSCRIPTION_TYPE_CORP")));
				businessCard.getBcPermissionses().add(
						new BcPermissions(us,businessCard ));
			}
		}

	}

	public void handleFileUpload(FileUploadEvent event) {
		setAvatar(event.getFile());

	}

	public void handleTagSelect(SelectEvent event) {
		Object tag = event.getObject();
		selectedTags.add((Tags) tag);
	}

	public void handleTagUnselect(UnselectEvent event) {
		Object tag = event.getObject();
		selectedTags.remove((Tags) tag);
	}

	public List<Tags> getTagsList(String query) {
		if (businessCard.getUserSubscription().getSubscription()
				.getSubscriptionId() == Constants.CORP_SUB_TYPE) {
			return tagsManagedBean.getCorporateTagsList(query);
		} else {
			return tagsManagedBean.getPersonalTagsList(query);
		}
	}

	public boolean isCountrySet() {
		if (businessCard.getAddress().getCountries() == null)
			return false;
		return true;
	}

	public void addMobile() {
		Mobile newMobile = new Mobile(new MobileId(getBcId(), ""), businessCard);
		mobileList.add(newMobile);

	}

	public void addPhone() {
		Phone newPhone = new Phone(new PhoneId(getBcId(), null), businessCard);
		phoneList.add(newPhone);
	}

	public void addFax() {
		Fax newFax = new Fax(new FaxId(getBcId(), null), businessCard);
		faxList.add(newFax);
	}

	public void addEmail() {
		Email newEmail = new Email(new EmailId(getBcId(), null), businessCard);
		emailList.add(newEmail);
	}

	public void addtitle() {
		Sector sector = new Sector();
		sector.setSectorId(Constants.DEFAULT_SECTOR);
		Title newTitle = new Title(businessCard);
		newTitle.setSector(sector);
		titleList.add(newTitle);
	}

	public void addWebsite() {
		Website newWebsite = new Website(new WebsiteId(null, getBcId()),
				businessCard);
		websiteList.add(newWebsite);

	}

	public void addIm() {
		Im newIm = new Im(new ImId(null, getBcId()), businessCard, null);
		imList.add(newIm);
	}

	public void addSocialNetwork() {
		SocialNetworksTypes snt = new SocialNetworksTypes();
		SocialNetwork newSocialNetwork = new SocialNetwork(new SocialNetworkId(
				getBcId(), null),snt, businessCard);
		socialNetworkList.add(newSocialNetwork);
	}

	public void removeAttachement(int id) {
		Iterator it = attachementList.iterator();
		System.err.println("-------------------------------");
		System.out.println("To be deleted id: " + id);
		while (it.hasNext()) {
			Attachment att = (Attachment) it.next();
			System.out.println("Id: " + att.getAttachmentId());
			System.out.println("PAth: " + att.getPath());
			if (att.getAttachmentId() == id)
				it.remove();
		}

	}

	public int getBcId() {
		return bcId;
	}

	public void setBcId(int bcId) {
		this.bcId = bcId;
	}

	public BusinessCard getBusinessCard() {
		return businessCard;
	}

	public void setBusinessCard(BusinessCard businessCard) {
		this.businessCard = businessCard;
	}

	public BusinessCardServiceManager getBcService() {
		return bcService;
	}

	public void setBcService(BusinessCardServiceManager bcService) {
		this.bcService = bcService;
	}

	public LookUpsServiceManager getLookupService() {
		return lookupService;
	}

	public void setLookupService(LookUpsServiceManager lookupService) {
		this.lookupService = lookupService;
	}

	public UserSubscriptionServiceManager getUserSubscriptionService() {
		return userSubscriptionService;
	}

	public void setUserSubscriptionService(
			UserSubscriptionServiceManager userSubscriptionService) {
		this.userSubscriptionService = userSubscriptionService;
	}

	public Mobile getMob() {
		return mob;
	}

	public void setMob(Mobile mob) {
		this.mob = mob;
	}

	public List<Mobile> getMobileList() {
		return mobileList;
	}

	public void setMobileList(List<Mobile> mobileList) {
		this.mobileList = mobileList;
	}

	public List<Phone> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(List<Phone> phoneList) {
		this.phoneList = phoneList;
	}

	public List<Fax> getFaxList() {
		return faxList;
	}

	public void setFaxList(List<Fax> faxList) {
		this.faxList = faxList;
	}

	public List<Email> getEmailList() {
		return emailList;
	}

	public void setEmailList(List<Email> emailList) {
		this.emailList = emailList;
	}

	public List<Title> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<Title> titleList) {
		this.titleList = titleList;
	}

	public List<Im> getImList() {
		return imList;
	}

	public void setImList(List<Im> imList) {
		this.imList = imList;
	}

	public List<SocialNetwork> getSocialNetworkList() {
		return socialNetworkList;
	}

	public void setSocialNetworkList(List<SocialNetwork> socialNetworkList) {
		this.socialNetworkList = socialNetworkList;
	}

	public List<Website> getWebsiteList() {
		return websiteList;
	}

	public void setWebsiteList(List<Website> websiteList) {
		this.websiteList = websiteList;
	}

	public List<Tags> getSelectedTags() {
		return selectedTags;
	}

	public void setSelectedTags(List<Tags> selectedTags) {
		this.selectedTags = selectedTags;
	}

	public TagsManagedBean getTagsManagedBean() {
		return tagsManagedBean;
	}

	public void setTagsManagedBean(TagsManagedBean tagsManagedBean) {
		this.tagsManagedBean = tagsManagedBean;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public String getSelectedCountryId() {
		return selectedCountryId;
	}

	public void setSelectedCountryId(String selectedCountryId) {
		this.selectedCountryId = selectedCountryId;
	}

	public Countries getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(Countries addressCountry) {
		this.addressCountry = addressCountry;
	}

	public UploadedFile getAvatar() {
		return avatar;
	}

	public void setAvatar(UploadedFile avatar) {
		this.avatar = avatar;
	}

	public List<User> getSelectedCorporateUsers() {
		return selectedCorporateUsers;
	}

	public void setSelectedCorporateUsers(List<User> selectedCorporateUsers) {
		this.selectedCorporateUsers = selectedCorporateUsers;
	}

	public List<User> getCorporateUsers() {
		return corporateUsers;
	}

	public void setCorporateUsers(List<User> corporateUsers) {
		this.corporateUsers = corporateUsers;
	}

	public UserServiceManager getUserService() {
		return userService;
	}

	public void setUserService(UserServiceManager userService) {
		this.userService = userService;
	}

	public List<Attachment> getAttachementList() {
		return attachementList;
	}

	public void setAttachementList(List<Attachment> attachementList) {
		this.attachementList = attachementList;
	}

	public BcPermissionManager getBcPermissionService() {
		return bcPermissionService;
	}

	public void setBcPermissionService(BcPermissionManager bcPermissionService) {
		this.bcPermissionService = bcPermissionService;
	}

}
