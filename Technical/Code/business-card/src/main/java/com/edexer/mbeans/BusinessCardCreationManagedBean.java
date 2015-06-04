package com.edexer.mbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.Address;
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
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.LookUpsServiceManager;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;

@ManagedBean
@ViewScoped
public class BusinessCardCreationManagedBean implements Serializable {

	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;

	@ManagedProperty("#{lookupsServiceManager}")
	private LookUpsServiceManager lookupService;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubscriptionService;
	@ManagedProperty("#{tagsManagedBean}")
	private TagsManagedBean tagsManagedBean;

	@ManagedProperty("#{userService}")
	private UserServiceManager userService;

	private int selectedCountryId;
	private int selectedSocialNetworkId;

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
	private Attachment attachment = null;
	private ResourceBundle settingsBundle;
	private AutoComplete tagsAutocompleteControl;
	private String subscriptionType;
	private Map<String, String> subscriptionTypeMap;
	private User user;
	private UploadedFile file;
	private Part avatar;
	private List<User> corporateUsers;
	private List<User> selectedCorporateUsers;
	private static final Logger logger = Logger
			.getLogger(BusinessCardCreationManagedBean.class);
	private List<UploadedFile> uploads = new ArrayList<UploadedFile>();

	public BusinessCardCreationManagedBean() {
		businessCard = new BusinessCard();
		businessCard.setAddress(new Address());
	}

	@PostConstruct
	private void initialize() {
		settingsBundle = ResourceBundle.getBundle("/settings");
		setSubscriptionTypeMap(new HashMap<String, String>());
		getSubscriptionTypeMap().put("Personal", "P");
		getSubscriptionTypeMap().put("Corporate", "C");
		subscriptionType = "P";
		user = ((User) HttpSessionUtil.getSession()
				.getAttribute(Constants.USER));
		Hibernate.initialize(user.getUserSubscriptionsForUserId());
		corporateUsers = userService.getCorporateUsers(user);
		// corporateUsers =
		// If user has corporate subscription then put corporate as a choice
		// if
		// (userSubscriptionService.getUserSubscription(getUser().getUserId(),
		// Integer.valueOf(settingsBundle
		// .getString("SUBSCRIPTION_TYPE_CORP"))) != null)
		// getSubscriptionTypeMap().put("Corporate", "C");
		addMobile();
		addPhone();
		addFax();
		addEmail();
		addtitle();
		addWebsite();
		addIm();
		addSocialNetwork();

	}

	private String saveCard(String returnWord) {
		String validation = validateBusinessCard(businessCard, false);
		if (validation == null) {
			logger.info("validation = null");
			try {
				if (file != null)
					bcService.addBusinessCard(businessCard, file, uploads);
				else
					bcService.addBusinessCard(businessCard, null, uploads);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return returnWord.equals(Constants.ADD_NEW_CARD) ? Constants.ADD_NEW_CARD
					: Constants.INDEX;
		} else {
			FacesUtil.addErrorMessage("Error", validation);
			return validation;
		}
	}

	public String actionSave() {
		logger.info("Action Save Called");
		prepareBusinessCard();
		saveCard(Constants.INDEX);
		String url = "/user/index.xhtml";
		//FacesUtil.redirectToPage(url);
		return Constants.INDEX;
	}

	public String actionSaveAndNew() {
		logger.info("Action Save and new Called");
		prepareBusinessCard();
		saveCard(Constants.ADD_NEW_CARD);
		String url = "/user/addbusinesscard.xhtml";
		//FacesUtil.redirectToPage(url);
		return Constants.ADD_NEW_CARD;
	}

	public void setAutocompleteMethod() {
		System.out.println("Testing...");
		System.out.println("Select value: " + getSubscriptionType());
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ExpressionFactory f = app.getExpressionFactory();
		MethodExpression expr;
		if (getSubscriptionType().equals("C"))
			expr = f.createMethodExpression(context.getELContext(),
					"#{tagsManagedBean.getCorporateTagsList}", List.class,
					new Class[] { String.class });
		else
			expr = f.createMethodExpression(context.getELContext(),
					"#{tagsManagedBean.getPersonalTagsList}", List.class,
					new Class[] { String.class });

		tagsAutocompleteControl.setCompleteMethod(expr);
	}

	public boolean hasCorpSubscription() {
		UserSubscription us = userSubscriptionService
				.getUserSubscriptionByType(
						user.getUserSubscriptionsForUserId(), Integer
								.valueOf(settingsBundle
										.getString("SUBSCRIPTION_TYPE_CORP")));
		if (us == null)
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	private void prepareBusinessCard() {
		// Decalre
		businessCard.setEmails(new HashSet(0));
		businessCard.setFaxes(new HashSet(0));
		businessCard.setIms(new HashSet(0));
		businessCard.setMobiles(new HashSet(0));
		businessCard.setPhones(new HashSet(0));
		businessCard.setSocialNetworks(new HashSet(0));
		businessCard.setTagses(new HashSet(0));
		businessCard.setTitles(new HashSet(0));
		businessCard.setWebsites(new HashSet(0));
		businessCard.setBcPermissionses(new HashSet(0));
		// add
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
				businessCard.getTagses().add(tag);
			}
		}
		for (Website website : websiteList) {
			if (website.getId().getWebsite() != null
					&& !website.getId().getWebsite().equals(""))
				businessCard.getWebsites().add(website);
		}
		for (Title title : titleList) {
			if ((title.getTitle() != null && !title.getTitle().equals(""))
					&& (title.getCompany() != null && !title.getCompany()
							.equals(""))
					&& (title.getDepartment() != null && !title.getDepartment()
							.equals("")))
				businessCard.getTitles().add(title);
		}
		Countries country = new Countries();
		country.setIdCountry(selectedCountryId);
		businessCard.getAddress().setCountries(country);

		// others
		// creator
		businessCard.setCreator(getUser().getUserId());
		// user subscription

		if (subscriptionType.equals("C"/* Case corp */)) {

			UserSubscription parentCorpSub = userSubscriptionService
					.getParentCorpSubscriptionByUser(user);
			businessCard.setUserSubscription(parentCorpSub);
			// businessCard.setUserSubscription(getUserSubscriptionByType(
			// getUser().getUserSubscriptionsForUserId(), Integer
			// .valueOf(settingsBundle
			// .getString("SUBSCRIPTION_TYPE_CORP"))));
		} else {
			User u = userService.getUserById(user.getUserId());
			businessCard.setUserSubscription(getUserSubscriptionByType(
					u.getUserSubscriptionsForUserId(), 0/*
														 * Case personal
														 */));

		}
		// file attachment
		if (file != null) {
			businessCard.setAvatar(file.getFileName());
		}

		// done

		// permissions
		businessCard.getBcPermissionses().add(
				new BcPermissions(businessCard,businessCard
						.getUserSubscription()));
		UserSubscription us;
		if (selectedCorporateUsers != null) {
			for (User user : selectedCorporateUsers) {
				us = userSubscriptionService.getUserSubscriptionByType(user
						.getUserSubscriptionsForUserId(), Integer
						.valueOf(settingsBundle
								.getString("SUBSCRIPTION_TYPE_CORP")));
				businessCard.getBcPermissionses().add(
						new BcPermissions(businessCard,us));
			}
		}

	}

	private String validateBusinessCard(BusinessCard businessCard2,
			boolean updateMode) {
		return bcService.validateBusinessCard(businessCard2, updateMode);
	}

	public void handleFileUpload(FileUploadEvent event) {
		setFile(event.getFile());

	}

	public void handleAttachementUpload(FileUploadEvent event) {
		uploads.add(event.getFile());

	}

	private UserSubscription getUserSubscriptionByType(
			Set<UserSubscription> userSubscrptionSet, Integer subScriptionType) {
		if (userSubscrptionSet == null || userSubscrptionSet.size() < 1) {
			return null;
		}

		for (UserSubscription userSub : userSubscrptionSet) {
			if (subScriptionType == 0
					&& (userSub.getId().getSubType() == Integer
							.valueOf(settingsBundle
									.getString("SUBSCRIPTION_TYPE_FREE")) || userSub
							.getId().getSubType() == Integer
							.valueOf(settingsBundle
									.getString("SUBSCRIPTION_TYPE_PRO"))))
				return userSub;

			if (subScriptionType.equals(userSub.getId().getSubType())) {
				return userSub;
			}
		}
		return null;

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
				.getSubscriptionId() == Constants.CORP_SUBSCRIB_ID) {
			return tagsManagedBean.getCorporateTagsList(query);
		} else {
			return tagsManagedBean.getPersonalTagsList(query);
		}
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
		Title newTitle = new Title( businessCard);
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
				getBcId(), null), businessCard,snt);
		socialNetworkList.add(newSocialNetwork);
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

	public int getCountryId() {
		return selectedCountryId;
	}

	public void setCountryId(int countryId) {
		this.selectedCountryId = countryId;
	}

	public AutoComplete getTagsAutocompleteControl() {
		return tagsAutocompleteControl;
	}

	public void setTagsAutocompleteControl(AutoComplete tagsAutocompleteControl) {
		this.tagsAutocompleteControl = tagsAutocompleteControl;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public Map<String, String> getSubscriptionTypeMap() {
		return subscriptionTypeMap;
	}

	public void setSubscriptionTypeMap(Map<String, String> subscriptionTypeMap) {
		this.subscriptionTypeMap = subscriptionTypeMap;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public int getSelectedSocialNetworkId() {
		return selectedSocialNetworkId;
	}

	public void setSelectedSocialNetworkId(int selectedSocialNetworkId) {
		this.selectedSocialNetworkId = selectedSocialNetworkId;
	}

	public Part getAvatar() {
		return avatar;
	}

	public void setAvatar(Part avatar) {
		this.avatar = avatar;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserServiceManager getUserService() {
		return userService;
	}

	public void setUserService(UserServiceManager userService) {
		this.userService = userService;
	}

	public List<User> getSelectedCorporateUsers() {
		return selectedCorporateUsers;
	}

	public void setSelectedCorporateUsers(List<User> selectedCorporateUsers) {
		this.selectedCorporateUsers = selectedCorporateUsers;
	}

}
