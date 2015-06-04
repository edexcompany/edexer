package com.edexer.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.dao.BusinessCardEntityDaoImpl;
import com.edexer.dao.TitleEntityDaoImpl;
import com.edexer.mbeans.BusinessCardDetailsManagedBean;
import com.edexer.model.BcPermissions;
import com.edexer.model.BusinessCard;
import com.edexer.model.Email;
import com.edexer.model.Fax;
import com.edexer.model.Mobile;
import com.edexer.model.Phone;
import com.edexer.model.SocialNetwork;
import com.edexer.model.Title;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.model.Website;
import com.edexer.util.ExcelExporter;
import com.edexer.util.ExcelReader;

@Service("businessCardServiceManager")
public class BusinessCardServiceManagerImpl implements
		BusinessCardServiceManager, Serializable {

	private static final Logger logger = Logger
			.getLogger(BusinessCardServiceManagerImpl.class);

	@Autowired
	BusinessCardEntityDaoImpl bcDao;
	@Autowired
	TitleEntityDaoImpl titleDao;
	@Autowired
	ExcelHandlerManager excelService;

	@Autowired
	private UserSubscriptionServiceManager userSubscriptionService;

	ResourceBundle settingsBundle = ResourceBundle.getBundle("settings");
	private final static String CHANNEL = "/notify";

	@Override
	public Integer addBusinessCard(BusinessCard bc, UploadedFile file)
			throws HibernateException, IOException {
		return (Integer) bcDao.add(bc, file);
	}

	@Override
	public Integer addBusinessCard(BusinessCard bc, UploadedFile file,
			List<UploadedFile> uploads) throws HibernateException, IOException {
		return (Integer) bcDao.add(bc, file, uploads);
	}

	@Override
	public Integer addBusinessCard(BusinessCard bc) {
		return (Integer) bcDao.add(bc);
	}

	@Override
	public void updateBusinessCard(BusinessCard bc, UploadedFile file,
			List<UploadedFile> uploads) throws HibernateException, IOException {
		bcDao.update(bc, file, uploads);
	}

	@Override
	public String validateBusinessCardDuplication(BusinessCard businessCard,
			boolean updateMode) {
		return bcDao.validateBusinessCard(businessCard, updateMode);
	}

	@Override
	@Transactional
	public BusinessCard get(int bcId, boolean loadRelations) {
		return bcDao.get(bcId, loadRelations);
		// BusinessCard bc = bcDao.get(BusinessCard.class, bcId);
		// if (!loadRelations)
		// return bc;
		// Hibernate.initialize(bc.getAttachments());
		// Hibernate.initialize(bc.getBcPermissionses());
		// Hibernate.initialize(bc.getEmails());
		// Hibernate.initialize(bc.getFaxes());
		// Hibernate.initialize(bc.getIms());
		// Hibernate.initialize(bc.getMobiles());
		// Hibernate.initialize(bc.getPhones());
		// Hibernate.initialize(bc.getSocialNetworks());
		// Hibernate.initialize(bc.getTitles());
		// Hibernate.initialize(bc.getUserSubscription());
		// Hibernate.initialize(bc.getWebsites());
		// Hibernate.initialize(bc.getAddress());
		// Hibernate.initialize(bc.getAddress().getCountries());
		// Hibernate.initialize(bc.getTagses());
		// Iterator it = bc.getTitles().iterator();
		// while(it.hasNext()){
		// Hibernate.initialize(((Title)it.next()).getSector());
		// }
		// it = bc.getSocialNetworks().iterator();
		// while(it.hasNext()){
		// Hibernate.initialize(((SocialNetwork)it.next()).getSocialNetworksTypes());
		// }
		// return bc;
	}

	@Override
	public List<BusinessCard> getBusinessCards(int count) {
		return bcDao.getBusinessCards();
	}

	@Override
	public List<BusinessCard> advancedFilter(int first, int pageSize,
			int sortField, Map<String, Object> filters, boolean findOnContacts,
			boolean personalContacts, boolean corpContacts) {
		return bcDao.advancedFilter(first, pageSize, sortField, filters,
				findOnContacts, personalContacts, corpContacts);
	}

	@Override
	public int getAdvancedFilterRowCount(int first, int pageSize,
			int sortField, Map<String, Object> filters, boolean findOnContacts,
			boolean personalContacts, boolean corpContacts) {
		return bcDao.getAdvencedFilterRowCount(first, pageSize, sortField,
				filters, findOnContacts, personalContacts, corpContacts);
	}

	@Override
	public BusinessCard getUserOwnCard(int userId) {
		return bcDao.getUserOwnCard(userId);
	}

	@Override
	public boolean checkCardViewAuthority(User user, BusinessCard businessCard) {
		// TODO Auto-generated method stub
		UserSubscription personalUserSubscription = userSubscriptionService
				.getUserSubscriptionByType(
						user.getUserSubscriptionsForUserId(), 0);
		UserSubscription parentCorpUserSubscription = userSubscriptionService
				.getParentCorpSubscriptionByUser(user);
		if (/* case personal */personalUserSubscription != null
				&& personalUserSubscription.getId().getUserId() == businessCard
						.getUserSubscription().getId().getUserId()
				&& personalUserSubscription.getId().getSubType() == businessCard
						.getUserSubscription().getId().getSubType())
			return true;
		else if (/* case corp */parentCorpUserSubscription != null
				&& parentCorpUserSubscription.getId().getUserId() == businessCard
						.getUserSubscription().getId().getUserId()
				&& parentCorpUserSubscription.getId().getSubType() == businessCard
						.getUserSubscription().getId().getSubType())
			return true;
		return false;
	}

	public UserSubscriptionServiceManager getUserSubscriptionService() {
		return userSubscriptionService;
	}

	public void setUserSubscriptionService(
			UserSubscriptionServiceManager userSubscriptionService) {
		this.userSubscriptionService = userSubscriptionService;
	}

	@Override
	public int uploadWorkbook(final File file, final String fileExt,
			final User user, final boolean isPersonal) {
		try {
			new Thread() {
				public void run() {
					try {
						this.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					List<BusinessCard> list;
					try {
						list = excelService.readAndConvertToBusinessCard(0, 1,
								false, file, fileExt);
						UserSubscription userSubscription = userSubscriptionService
								.getUserSubscriptionByType(
										user.getUserSubscriptionsForUserId(),
										((isPersonal) ? 0
												: Integer.valueOf(settingsBundle
														.getString("SUBSCRIPTION_TYPE_CORP"))));
						if (userSubscription != null) {
							int failed = 0;
							StringBuffer details = new StringBuffer("");
							String summary = "Import completed";

							for (BusinessCard bc : list) {
								bc.setUserSubscription(userSubscription);
								bc.setCreator(user.getUserId());
								String error = validateBusinessCard(bc, false);
								if (error == null)
									addBusinessCard(bc);
								else {
									failed++;
									details.append(error + ". ");
								}

							}

							EventBus eventBus = EventBusFactory.getDefault()
									.eventBus();
							eventBus.publish(
									"/notify",
									new FacesMessage(summary + "\n\n" + failed
											+ " records failed out of "
											+ list.size(), details.toString()));
						}
					} catch (NullPointerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						EventBus eventBus = EventBusFactory.getDefault()
								.eventBus();
						eventBus.publish("/notify", new FacesMessage("Error",
								"Maximum number of records exceeded"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();
			// System.out
			// .println("Here U gonna send the Response to continue your processes");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String validateBusinessCard(BusinessCard bc, boolean updateMode) {
		ResourceBundle bcValidations = ResourceBundle
				.getBundle("businesscardvalidations");
		logger.info("Validating Business Card.");
		// if (bc.getUserSubscription() == null) {
		// logger.debug("User subscription not found");
		// return "User subscription not found";
		// }
		if (bc.getCreator() == 0) {
			logger.debug("Creator not found");
			return "Creator not found";
		}
		if (bc.getBcFirstName().length() > Integer.valueOf(bcValidations
				.getString("FIRST_NAME_MAX_LENGTH"))) {
			logger.debug("First name is too long");
			return bc.getBcFirstName() + "is too long as First name ";
		}
		if (bc.getBcLastName().length() > Integer.valueOf(bcValidations
				.getString("LAST_NAME_MAX_LENGTH"))) {
			logger.debug("Last name is too long");
			return bc.getBcLastName() + "is too long as Last name";
		}

		Iterator it = bc.getMobiles().iterator();
		while (it.hasNext()) {
			String num = ((Mobile) it.next()).getId().getMobileNum();
			System.out.println("num:" + num);
			System.out.println("regex:" + bcValidations.getString("PHONE_REGEX"));
			System.out.println(num.matches(bcValidations.getString("PHONE_REGEX")));
			if (!num.matches(bcValidations.getString("PHONE_REGEX"))) {
				logger.debug("Mobile number is not valid");
				return num + " is not valid as Mobile";
			}
		}

		it = bc.getFaxes().iterator();
		while (it.hasNext()) {
			String fax = ((Fax) it.next()).getId().getFaxNum();
			if (!fax.matches(bcValidations.getString("PHONE_REGEX"))) {
				logger.debug("Fax number is not valid");
				return fax + " is not valid as Fax number";
			}
		}

		it = bc.getPhones().iterator();
		while (it.hasNext()) {
			String phone = ((Phone) it.next()).getId().getPhoneNum();
			if (!phone.matches(bcValidations.getString("PHONE_REGEX"))) {
				logger.debug("Phone number is not valid");
				return phone + " is not valid as Phone number ";
			}
		}

		it = bc.getEmails().iterator();
		while (it.hasNext()) {
			String email = ((Email) it.next()).getId().getEmailAddress();
			if (!email.matches(bcValidations.getString("EMAIL_REGEX"))) {
				logger.debug("Email address is not valid");
				return email + " is not valid as Email address";
			}
		}

		it = bc.getWebsites().iterator();
		while (it.hasNext()) {
			String website = ((Website) it.next()).getId().getWebsite();
			if (!website.matches(bcValidations.getString("WEBSITE_REGEX"))) {
				logger.debug("Website address is not valid");
				return website + " is not valid as Website address";
			}
		}
		String errorMsg = "";
		String duplicationError = validateBusinessCardDuplication(bc,
				updateMode);
		if (duplicationError != null) {
			logger.debug("This business card is duplicated");
			if (duplicationError.equals("m")) {
				errorMsg = "Mobile found for existing user.";
			} else if (duplicationError.equals("p")) {
				errorMsg = "Phone found for existing user.";
			} else if (duplicationError.equals("f")) {
				errorMsg = "Fax found for existing user.";
			} else if (duplicationError.equals("e")) {
				errorMsg = "Email found for existing user.";
			}
			return "Duplicated business card: " + bc.getBcFirstName() + " "
					+ bc.getBcLastName() + ". " + errorMsg;
		}
		return null;
	}

	@Override
	public int addBusinessCardList(List<BusinessCard> list, User user,
			boolean isPersonal) {

		return 0;
	}

	@Override
	public DefaultStreamedContent exportBusinessCards(
			List<BusinessCard> bcList, User user) {
		List<BusinessCard> exportList = new ArrayList<BusinessCard>();
		for (BusinessCard bc : bcList) {
			if (checkExportAuthority(user, bc))
				exportList.add(bcDao.get(bc.getBusinessCardId(), true));
		}
		try {
			return ExcelExporter.exportBusinessCardListToExcel(exportList);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean checkExportAuthority(User user, BusinessCard bc) {
		if (getBusinessCardType(bc) == BusinessCardTypes.FindOn) {
			// TO DO: check if this bc available to be exported (public)
			return false;
		} else if (getBusinessCardType(bc) == BusinessCardTypes.Personal) {
			if (bc.getUserSubscription().getId().getUserId() == user
					.getUserId())
				return true;
			else
				return false;
		} else if (getBusinessCardType(bc) == BusinessCardTypes.Corporate) {
			UserSubscription ownerUserSubscription = userSubscriptionService
					.getParentCorpSubscriptionByUser(user);
			if (ownerUserSubscription != bc.getUserSubscription())
				return false;
			UserSubscription userCorpSubscription = userSubscriptionService
					.getUserSubscriptionByType(user
							.getUserSubscriptionsForUserId(), Integer
							.valueOf(settingsBundle
									.getString("SUBSCRIPTION_TYPE_CORP")));
			if (userCorpSubscription.getIsAdmin() == "Y")
				// case admin
				return true;
			if (user.getUserId() == ownerUserSubscription.getId().getUserId())
				// case owner
				return true;
			if ((userCorpSubscription.getCanExport() == "Y")
					&& (checkCardViewAuthority(user, bc)))
				return true;
			return false;
		} else
			return false;
	}

	private boolean checkViewAuthority(User user, BusinessCard bc) {
		Iterator it = bc.getBcPermissionses().iterator();
		while (it.hasNext()) {
			BcPermissions permission = (BcPermissions) it.next();
			if (permission.getUserSubscription().getId().getUserId() == user
					.getUserId())
				return true;
		}
		return false;
	}

	@Override
	public BusinessCardTypes getBusinessCardType(BusinessCard bc) {
		if (bc.getUserSubscription() == null)
			return BusinessCardTypes.FindOn;
		if (bc.getUserSubscription().getId().getSubType() == Integer
				.valueOf(settingsBundle.getString("SUBSCRIPTION_TYPE_CORP")))
			return BusinessCardTypes.Corporate;
		if (bc.getUserSubscription().getId().getSubType() == Integer
				.valueOf(settingsBundle.getString("SUBSCRIPTION_TYPE_FREE"))
				|| bc.getUserSubscription().getId().getSubType() == Integer
						.valueOf(settingsBundle
								.getString("SUBSCRIPTION_TYPE_PRO")))
			return BusinessCardTypes.Personal;
		return null;
	}

	@Override
	public String deleteBusinessCard(BusinessCard bc) {
		try {
			if (bc != null) {
				bcDao.delete(bc);
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	@Override
	public void DeleteBusinessCard(int bcId) {
		// TODO Auto-generated method stub

	}

}
