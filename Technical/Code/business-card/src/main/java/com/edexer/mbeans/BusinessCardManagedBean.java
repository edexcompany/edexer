package com.edexer.mbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
//import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.extensions.model.dynaform.AbstractDynaFormElement;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.model.UploadedFile;

//import antlr.StringUtils;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.LookUpsServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;
import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.Address;
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

@ManagedBean
@ViewScoped
public class BusinessCardManagedBean implements Serializable {

	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;

	@ManagedProperty("#{lookupsServiceManager}")
	private LookUpsServiceManager lookupService;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubscriptionService;

	private AutoComplete tagsAutocompleteControl;
	private SelectOneRadio subscriptionRadio;

	private List<SelectItem> countries = new ArrayList<SelectItem>();
	private List<SelectItem> ims = new ArrayList<SelectItem>();
	private List<SelectItem> socialNetworks = new ArrayList<SelectItem>();
	private List<SelectItem> sectors = new ArrayList<SelectItem>();
	private List<Tags> selectedTags;
	private UploadedFile file;
	private String subscriptionType;
	private Map<String, String> subscriptionTypeMap;
	private boolean personal;
	private BusinessCard businessCard;
	private DynaFormModel basicModel;
	private DynaFormModel mobileModel;
	private DynaFormModel telephoneModel;
	private DynaFormModel faxModel;
	private DynaFormModel emailModel;
	private DynaFormModel titleModel;
	private DynaFormModel websiteModel;
	private DynaFormModel addressModel;
	private DynaFormModel imModel;
	private DynaFormModel socialModel;
	private DynaFormModel noteModel;
	private DynaFormModel subscriptionModel;

	DynaFormRow row;
	String fNameKey = "FNAME_KEY";
	String lNameKey = "LNAME_KEY";

	@PostConstruct
	protected void initialize() {
		// User user = ((User) ((HttpServletRequest)
		// javax.faces.context.FacesContext
		// .getCurrentInstance().getExternalContext().getRequest())
		// .getSession().getAttribute("user"));

		setSubscriptionTypeMap(new HashMap<String, String>());
		getSubscriptionTypeMap().put("Personal", "P");
		// If user has corporate subscription then put corporate as a choice
		if (userSubscriptionService.getUserSubscription(1, 2/*
															 * user.getUserId(),
															 * subscriptionType
															 * .corp
															 */) != null)
			getSubscriptionTypeMap().put("Corporate", "C");

		intializeModels();

		for (SocialNetworksTypes item : lookupService.getSocialNetworksTypes()) {
			socialNetworks.add(new SelectItem(item.getSnTypeId(), item
					.getName()));
		}

		for (Sector s : lookupService.getSectorList()) {
			sectors.add(new SelectItem(s.getSectorId(), s.getSectorName()));
		}

		drawForm();
	}

	private void drawForm() {
		addRow(basicModel, "First Name", "fName", false, "name", fNameKey, null);
		addRow(basicModel, "Last Name", "lName", false, "name", lNameKey, null);
		addRow(mobileModel, "Mobile", "mobile", false, "mobile", null, null);
		row.addControl(new DynaFormProperty("btn", false), "addMobile");
		addRow(telephoneModel, "Telephone", "telephone", false, "telephone",
				null, null);
		row.addControl(new DynaFormProperty("btn", false), "addTelephone");
		addRow(faxModel, "Fax", "fax", false, "fax", null, null);
		row.addControl(new DynaFormProperty("btn", false), "addFax");
		addRow(emailModel, "Email", "email", false, "email", null, null);
		row.addControl(new DynaFormProperty("btn", false), "addEmail");
		addRow(titleModel, "Company", "company", false, "company", null, null);
		addRow(titleModel, "Department", "department", false, "department",
				null, null);
		addRow(titleModel, "Sector", "sector", false, "sector", null, null);
		addRow(titleModel, "Title", "title", false, "title", null, null);
		row.addControl(new DynaFormProperty("btn", false), "addTitle");

		addRow(websiteModel, "Website", "website", false, "website", null, null);
		row.addControl(new DynaFormProperty("btn", false), "addWebsite");

		addRow(addressModel, "Street 1", "street", false, "street",
				"STREET1_KEY", null);
		addRow(addressModel, "Street 2", "street2", false, "street",
				"STREET2_KEY", null);
		addRow(addressModel, "City", "ciity", false, "citystate", "CITY_KEY",
				null);
		addRow(addressModel, "State", "state", false, "citystate", "STATE_KEY",
				null);
		addRow(addressModel, "Zip", "zip", false, "zip", "ZIP_KEY", null);
		addRow(addressModel, "Country", "country", false, "country",
				"COUNTRY_KEY", null);

		addRow(imModel, "Im", "imId", false, "imId", null, null);
		row.addControl(new DynaFormProperty("imType", false), "imType");
		row.addControl(new DynaFormProperty("btn", false), "addIm");

		addRow(socialModel, "Social Network", "socialId", false, "socialId",
				null, null);
		row.addControl(new DynaFormProperty("socialType", false), "socialType");
		row.addControl(new DynaFormProperty("btn", false), "addSocial");

		addRow(noteModel, "Note", "textarea", false, "textarea", null, null);
		addRow(subscriptionModel, "Subscription", "subscription", true,
				"subscription", null, null);
		addRow(subscriptionModel, "Tags", "tags", true, "tags", null, null);

	}

	public void setAutocompleteMethod() {
		System.out.println("Testing...");
		System.out.println("Select value: " + subscriptionType);
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ExpressionFactory f = app.getExpressionFactory();
		MethodExpression expr;
		if (subscriptionType.equals("C"))
			expr = f.createMethodExpression(context.getELContext(),
					"#{tagsManagedBean.getCorporateTagsList}", List.class,
					new Class[] { String.class });
		else
			expr = f.createMethodExpression(context.getELContext(),
					"#{tagsManagedBean.getPersonalTagsList}", List.class,
					new Class[] { String.class });

		tagsAutocompleteControl.setCompleteMethod(expr);
	}

	public void handleTagSelect(SelectEvent event) {
		Object tag = event.getObject();
		selectedTags.add((Tags) tag);
	}

	public void handleTagUnselect(UnselectEvent event) {
		Object tag = event.getObject();
		selectedTags.remove((Tags) tag);
	}

	private void intializeModels() {
		basicModel = new DynaFormModel();
		mobileModel = new DynaFormModel();
		telephoneModel = new DynaFormModel();
		faxModel = new DynaFormModel();
		emailModel = new DynaFormModel();
		titleModel = new DynaFormModel();
		websiteModel = new DynaFormModel();
		addressModel = new DynaFormModel();
		imModel = new DynaFormModel();
		socialModel = new DynaFormModel();
		noteModel = new DynaFormModel();
		subscriptionModel = new DynaFormModel();

	}

	private void addRow(DynaFormModel model, String label, String controlName,
			boolean required, String type, String key,
			List<DynaFormControl> list) {
		row = model.createExtendedRow();
		DynaFormLabel lbl = row.addLabel(label);
		DynaFormControl control = row.addControl(new DynaFormProperty(
				controlName, required), type);
		lbl.setForControl(control);
		if (key != null)
			control.setKey(key);
		if (list != null)
			list.add(control);
	}

	private String getControlValue(DynaFormControl control) {
		return (String) ((DynaFormProperty) control.getData()).getValue();
	}

	public String addBusinessCard() {
		String formSubscriptionValue;
		UserSubscription userSubscription = new UserSubscription();
		businessCard = new BusinessCard();
		User user = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);
		businessCard.setCreator(user.getUserId());

		for (DynaFormControl c : basicModel.getControls()) {
			if (c.getKey() == fNameKey)
				businessCard.setBcFirstName(getControlValue(c));
			if (c.getKey() == lNameKey)
				businessCard.setBcLastName(getControlValue(c));
		}

		for (DynaFormControl c : mobileModel.getControls()) {
			if (getControlValue(c) != null && getControlValue(c) != "") {
				businessCard.getMobiles().add(
						new Mobile(new MobileId(0, getControlValue(c)),
								businessCard));
			}
		}

		for (DynaFormControl c : telephoneModel.getControls()) {
			if (getControlValue(c) != null && getControlValue(c) != "") {
				businessCard.getPhones().add(
						new Phone(new PhoneId(0, getControlValue(c)),
								businessCard));
			}
		}

		for (DynaFormControl c : websiteModel.getControls()) {
			if (getControlValue(c) != null && getControlValue(c) != "") {
				businessCard.getWebsites().add(
						new Website(new WebsiteId(getControlValue(c), 0),
								businessCard));
			}
		}

		businessCard.setAddress(new Address());
		for (DynaFormControl address : addressModel.getControls()) {
			if (address.getKey() == "STREET1_KEY")
				businessCard.getAddress().setStreet1(getControlValue(address));
			else if (address.getKey() == "STREET2_KEY")
				businessCard.getAddress().setStreet2(getControlValue(address));
			else if (address.getKey() == "CITY_KEY")
				businessCard.getAddress().setCity(getControlValue(address));
			else if (address.getKey() == "STATE_KEY")
				businessCard.getAddress().setState(getControlValue(address));
			else if (address.getKey() == "ZIP_KEY")
				businessCard.getAddress().setZip(getControlValue(address));
			else if (address.getKey() == "COUNTRY_KEY") {
				Countries country = new Countries();
				country.setIdCountry(Integer.parseInt(getControlValue(address)));
				businessCard.getAddress().setCountries(country);
			}
			System.out.println(address.getKey() + ": "
					+ getControlValue(address));
		}
		for (DynaFormControl c : emailModel.getControls()) {
			if (getControlValue(c) != null && getControlValue(c) != "") {
				businessCard.getEmails().add(
						new Email(new EmailId(0, getControlValue(c)),
								businessCard));

			}
		}
		for (DynaFormControl c : faxModel.getControls()) {
			if (getControlValue(c) != null && getControlValue(c) != "") {
				businessCard.getFaxes()
						.add(new Fax(new FaxId(0, getControlValue(c)),
								businessCard));

			}
		}

		for (DynaFormRow row : imModel.getExtendedRows()) {
			List<AbstractDynaFormElement> elementsList = row.getElements();
			String identifier = (String) ((DynaFormProperty) (((DynaFormControl) elementsList
					.get(1)).getData())).getValue();
			if (identifier != "" && identifier != null) {
				String typeString = (String) ((DynaFormProperty) (((DynaFormControl) elementsList
						.get(2)).getData())).getValue();
				// SocialNetworksTypes snt = new SocialNetworksTypes();
				// snt.setSnTypeId(Integer.parseInt(sntString));
				businessCard.getIms().add(
						new Im(new ImId(identifier, 0), businessCard,
								typeString));
			}
		}

		for (DynaFormRow row : socialModel.getExtendedRows()) {
			List<AbstractDynaFormElement> elementsList = row.getElements();
			String identifier = (String) ((DynaFormProperty) (((DynaFormControl) elementsList
					.get(1)).getData())).getValue();
			if (identifier != "" && identifier != null) {
				String sntString = (String) ((DynaFormProperty) (((DynaFormControl) elementsList
						.get(2)).getData())).getValue();
				SocialNetworksTypes snt = new SocialNetworksTypes();
				snt.setSnTypeId(Integer.parseInt(sntString));
				businessCard.getSocialNetworks().add(
						new SocialNetwork(new SocialNetworkId(0, identifier),
								businessCard,snt));
			}
		}

		Title title = new Title();
		List<AbstractDynaFormElement> elementsList;
		int counter = 0;
		for (int i = 0; i < titleModel.getExtendedRows().size(); i++) {
			elementsList = titleModel.getExtendedRows().get(i).getElements();
			DynaFormControl con = ((DynaFormControl) elementsList.get(1));
			if (con.getType() == "company")
				title.setCompany(getControlValue(con));
			else if (con.getType() == "department")
				title.setDepartment(getControlValue(con));
			else if (con.getType() == "sector") {
				Sector sector = new Sector();
				sector.setSectorId(Integer.parseInt(getControlValue(con)));
				title.setSector(sector);
			} else if (con.getType() == "title")
				title.setTitle(getControlValue(con));
			if (counter++ == 3) {
				counter = 0;
				businessCard.getTitles().add(title);
				title = new Title();
			}
		}

		for (DynaFormControl c : noteModel.getControls()) {
			if (c.getType() == "textarea") {
				businessCard.setNotes(getControlValue(c));
			}
		}

		if (subscriptionType.equals("C"))
			businessCard.setUserSubscription(userSubscriptionService
					.getUserSubscription(user, false));
		else
			businessCard.setUserSubscription(userSubscriptionService
					.getUserSubscription(user, true));

		// selectedTags = new ArrayList<Tags>();
		// Tags t = new Tags();
		// t.setTagId(1);
		// selectedTags.add(t);
		if (selectedTags != null) {
			for (Tags tag : selectedTags) {
				businessCard.getTagses().add(tag);
			}
		}

		String validation = validateBusinessCard(businessCard, true);
		if (validation.equals("")) {
			try {
				if (file != null)
					bcService.addBusinessCard(businessCard, file);
				else
					bcService.addBusinessCard(businessCard);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Constants.INDEX;
		} else {
			String errorMsg = "";
			String correctionMsg = "Update existing user";
			if (validation.equals("m")) {
				errorMsg = "Mobile found for existing user";
			} else if (validation.equals("p")) {
				errorMsg = "Phone found for existing user";
			} else if (validation.equals("f")) {
				errorMsg = "Fax found for existing user";
			} else if (validation.equals("e")) {
				errorMsg = "Email found for existing user";
			}
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN,
									"User name already exists!",
									"Try another userName"));
			return Constants.INDEX;
		}

		// attachements,

	}

	private String validateBusinessCard(BusinessCard businessCard2,
			boolean updateMode) {
		return bcService.validateBusinessCardDuplication(businessCard2,
				updateMode);
	}

	public void handleFileUpload(FileUploadEvent event) {
		// FacesMessage message = new FacesMessage("Succesful",
		// event.getFile().getFileName() + " is uploaded.");
		// FacesContext.getCurrentInstance().addMessage(null, message);
		file = event.getFile();
	}

	public void addMobileField() {
		addRow(mobileModel, "Mobile", "mobile", false, "mobile", null, null);
	}

	public void addTelephoneField() {
		addRow(telephoneModel, "Telephone", "telephone", false, "telephone",
				null, null);
	}

	public void addFaxField() {
		addRow(faxModel, "Fax", "fax", false, "fax", null, null);
	}

	public void addEmailField() {
		addRow(emailModel, "Email", "email", false, "email", null, null);
	}

	public void addTitleField() {
		addRow(titleModel, "Company", "company", false, "company", null, null);
		addRow(titleModel, "Department", "department", false, "department",
				null, null);
		addRow(titleModel, "Sector", "sector", false, "sector", null, null);
		addRow(titleModel, "Title", "title", false, "title", null, null);
	}

	public void addWebsiteField() {
		addRow(websiteModel, "Website", "website", false, "website", null, null);
	}

	public void addImField() {
		addRow(imModel, "Im", "imId", false, "imId", null, null);
		row.addControl(new DynaFormProperty("imType", false), "imType");
	}

	public void addSocialField() {
		addRow(socialModel, "Social Network", "socialId", false, "socialId",
				null, null);
		row.addControl(new DynaFormProperty("socialType", false), "socialType");
	}

	public BusinessCardManagedBean() {
	}

	public BusinessCardServiceManager getBcService() {
		return bcService;
	}

	public void setBcService(BusinessCardServiceManager bcService) {
		this.bcService = bcService;
	}

	public BusinessCard getBusinessCard() {
		return businessCard;
	}

	public void setBusinessCard(BusinessCard businessCard) {
		this.businessCard = businessCard;
	}

	public DynaFormModel getBasicModel() {
		return basicModel;
	}

	public void setBasicModel(DynaFormModel model) {
		this.basicModel = model;
	}

	public DynaFormModel getMobileModel() {
		return mobileModel;
	}

	public void setMobileModel(DynaFormModel model2) {
		this.mobileModel = model2;
	}

	public DynaFormModel getTelephoneModel() {
		return telephoneModel;
	}

	public void setTelephoneModel(DynaFormModel telephoneModel) {
		this.telephoneModel = telephoneModel;
	}

	public DynaFormModel getFaxModel() {
		return faxModel;
	}

	public void setFaxModel(DynaFormModel faxModel) {
		this.faxModel = faxModel;
	}

	public DynaFormModel getEmailModel() {
		return emailModel;
	}

	public void setEmailModel(DynaFormModel emailModel) {
		this.emailModel = emailModel;
	}

	public DynaFormModel getTitleModel() {
		return titleModel;
	}

	public void setTitleModel(DynaFormModel titleModel) {
		this.titleModel = titleModel;
	}

	public DynaFormModel getWebsiteModel() {
		return websiteModel;
	}

	public void setWebsiteModel(DynaFormModel websiteModel) {
		this.websiteModel = websiteModel;
	}

	public DynaFormModel getAddressModel() {
		return addressModel;
	}

	public void setAddressModel(DynaFormModel address1Model) {
		this.addressModel = address1Model;
	}

	public DynaFormModel getImModel() {
		return imModel;
	}

	public void setImModel(DynaFormModel imModel) {
		this.imModel = imModel;
	}

	public DynaFormModel getSocialModel() {
		return socialModel;
	}

	public void setSocialModel(DynaFormModel socialModel) {
		this.socialModel = socialModel;
	}

	public DynaFormModel getNoteModel() {
		return noteModel;
	}

	public void setNoteModel(DynaFormModel noteModel) {
		this.noteModel = noteModel;
	}

	public List<SelectItem> getCountries() {
		List<Countries> list = new ArrayList<Countries>();
		list = lookupService.getCountriesList();
		for (Countries c : list) {
			countries.add(new SelectItem(c.getIdCountry(), c.getCountryName()));
		}
		return countries;
	}

	public void setCountries(List<SelectItem> countries) {
		this.countries = countries;
	}

	public LookUpsServiceManager getLookupService() {
		return lookupService;
	}

	public void setLookupService(LookUpsServiceManager lookupService) {
		this.lookupService = lookupService;
	}

	public List<SelectItem> getIms() {

		// ims = lookupService.get
		if (ims.isEmpty()) {
			ims.add(new SelectItem("mcq", "MCQ"));
			ims.add(new SelectItem("msn", "MSN"));
		}
		return ims;
	}

	public void setIms(List<SelectItem> ims) {
		this.ims = ims;
	}

	public List<SelectItem> getSocialNetworks() {

		return socialNetworks;
	}

	public void setSocialNetworks(List<SelectItem> socialNetworks) {
		this.socialNetworks = socialNetworks;
	}

	public List<SelectItem> getSectors() {

		return sectors;
	}

	public void setSectors(List<SelectItem> sectors) {
		this.sectors = sectors;
	}

	public UserSubscriptionServiceManager getUserSubscriptionService() {
		return userSubscriptionService;
	}

	public void setUserSubscriptionService(
			UserSubscriptionServiceManager userSubscriptionService) {
		this.userSubscriptionService = userSubscriptionService;
	}

	public List<Tags> getSelectedTags() {
		return selectedTags;
	}

	public void setSelectedTags(List<Tags> selectedTags) {
		this.selectedTags = selectedTags;
	}

	public AutoComplete getTagsAutocompleteControl() {
		return tagsAutocompleteControl;
	}

	public void setTagsAutocompleteControl(AutoComplete tagsAutocompleteControl) {
		this.tagsAutocompleteControl = tagsAutocompleteControl;
	}

	public SelectOneRadio getSubscriptionRadio() {
		return subscriptionRadio;
	}

	public void setSubscriptionRadio(SelectOneRadio subscriptionRadio) {
		this.subscriptionRadio = subscriptionRadio;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public DynaFormModel getSubscriptionModel() {
		return subscriptionModel;
	}

	public void setSubscriptionModel(DynaFormModel subscriptionModel) {
		this.subscriptionModel = subscriptionModel;
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

}