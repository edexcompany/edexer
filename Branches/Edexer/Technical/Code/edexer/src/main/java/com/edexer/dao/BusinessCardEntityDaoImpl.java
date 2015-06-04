package com.edexer.dao;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.mbeans.Constants;
import com.edexer.model.*;
import com.edexer.util.FilesUtil;

@Repository("businessCardEntityDao")
public class BusinessCardEntityDaoImpl extends
		GenericEntityDaoImpl<BusinessCard> {

	@Autowired
	TitleEntityDaoImpl titleDao;
	@Autowired
	AddressEntityDaoImpl addressDao;
	@Autowired
	AttachmentEntityDaoImpl attachementDao;
	@Autowired
	BcPermissionsEntityDaoImpl bcPersmissionsDao;
	@Autowired
	EmailEntityDaoImpl emailDao;
	@Autowired
	FaxEntityDaoImpl faxDao;
	@Autowired
	ImEntityDaoImpl imDao;
	@Autowired
	MobileEntityDaoImpl mobileDao;
	@Autowired
	PhoneEntityDaoImpl phoneDao;
	@Autowired
	SocialNetworkEntityDaoImpl socialDao;
	@Autowired
	TagsEntityDaoImpl tagsDao;
	@Autowired
	WebsiteEntityDaoImpl websiteDao;
	@Autowired
	BcPermissionsEntityDaoImpl permissionsDao;
	@Autowired
	UserSubscriptionEntityDaoImpl userSubDao;
	private static final Logger logger = Logger
			.getLogger(BusinessCardEntityDaoImpl.class);

	public List<BusinessCard> filter(UserSubscription userSubscription,
			List<Tags> tags, int start, int lenght, String firstName,
			String lastName, String title, String company, String sector,
			String country, String mobile, String enail) {
		// Session session = sessionFactory.getCurrentSession();
		// Query query = session
		// .createSQLQuery(
		// "CALL advancedFilter (:offsetCount,:statusId)")
		// IN page_size int
		// ,IN sub_type_param int
		// ,IN first_name_param varchar(50)
		// ,IN last_name_param varchar(50)
		// ,IN title_param varchar(45)
		// ,IN company_param varchar(100)
		// ,IN mobile_param varchar(20)
		// ,IN email_param varchar(150)
		// ,IN sector_name_param varchar(50)
		// ,IN tags_param varchar(50)
		// ,IN order_by_param int
		// .addEntity(UserSubscription.class)
		// .setParameter("userId", userId)
		// .setParameter("stateId", statusId);
		// List<UserSubscription> result = query.list();
		// if (result != null) {
		// // logger.info("Found user with email = " + userEmail +
		// // " in the db");
		// return result;
		// }
		// return null;
		return null;
	}

	public BusinessCard get(int bcId, boolean loadRelations) {
		BusinessCard bc = get(BusinessCard.class, bcId);
		if (bc == null)
			return null;
		if (!loadRelations)
			return bc;
		Hibernate.initialize(bc.getAttachments());
		Hibernate.initialize(bc.getBcPermissionses());
		Hibernate.initialize(bc.getEmails());
		Hibernate.initialize(bc.getFaxes());
		Hibernate.initialize(bc.getIms());
		Hibernate.initialize(bc.getMobiles());
		Hibernate.initialize(bc.getPhones());
		Hibernate.initialize(bc.getSocialNetworks());
		Hibernate.initialize(bc.getTitles());
		Hibernate.initialize(bc.getUserSubscription());
		Hibernate.initialize(bc.getWebsites());
		Hibernate.initialize(bc.getTagses());
		Hibernate.initialize(bc.getAddress());
		if (bc.getAddress() != null) {
			Hibernate.initialize(bc.getAddress().getCountries());
		}
		Iterator it = bc.getTitles().iterator();
		while (it.hasNext()) {
			Hibernate.initialize(((Title) it.next()).getSector());
		}
		it = bc.getSocialNetworks().iterator();
		while (it.hasNext()) {
			Hibernate.initialize(((SocialNetwork) it.next())
					.getSocialNetworksTypes());
		}
		return bc;
	}

	public List<BusinessCard> getBusinessCards() {
		Session session = sessionFactory.getCurrentSession();
		ArrayList<BusinessCard> list = null;
		Query q = session.createQuery("from BusinessCard");
		list = (ArrayList<BusinessCard>) q.list();
		// logger.info("Returning from getAdminNotes");
		return list;
	}

	@Override
	@Transactional
	public Object add(BusinessCard entity) throws HibernateException {
		try {
			return processBusinessCardAdding(entity, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		// int bcId = (Integer) sessionFactory.getCurrentSession().save(entity);
		//
		// Address a = entity.getAddress();
		// System.out.println("address created " + bcId);
		// a.setBusinessCardBusinessCardId(bcId);
		// a.setBusinessCard(entity);
		// addressDao.add(a);
		// Iterator it = entity.getTitles().iterator();
		// while (it.hasNext()) {
		// Title t = (Title) it.next();
		// t.setBusinessCard(entity);
		// titleDao.add(t);
		// }
		// // it = entity.getAttachments().iterator();
		// // while(it.hasNext()){
		// // Attachment t = (Attachment) it.next();
		// // t.setBusinessCard(entity);
		// // attachementDao.add(t);
		// // }
		//
		// it = entity.getEmails().iterator();
		// while (it.hasNext()) {
		// Email t = (Email) it.next();
		// t.setBusinessCard(entity);
		// t.getId().setBcId(bcId);
		// emailDao.add(t);
		// }
		// it = entity.getFaxes().iterator();
		// while (it.hasNext()) {
		// Fax t = (Fax) it.next();
		// t.setBusinessCard(entity);
		// t.getId().setBcId(bcId);
		// faxDao.add(t);
		// }
		// it = entity.getIms().iterator();
		// while (it.hasNext()) {
		// Im t = (Im) it.next();
		// t.setBusinessCard(entity);
		// t.getId().setBcId(bcId);
		// imDao.add(t);
		// }
		// it = entity.getMobiles().iterator();
		// while (it.hasNext()) {
		// Mobile t = (Mobile) it.next();
		// t.setBusinessCard(entity);
		// t.getId().setBcId(bcId);
		// mobileDao.add(t);
		// }
		// it = entity.getPhones().iterator();
		// while (it.hasNext()) {
		// Phone t = (Phone) it.next();
		// t.setBusinessCard(entity);
		// t.getId().setBcId(bcId);
		// phoneDao.add(t);
		// }
		// it = entity.getSocialNetworks().iterator();
		// while (it.hasNext()) {
		// SocialNetwork t = (SocialNetwork) it.next();
		// t.setBusinessCard(entity);
		// t.getId().setBcId(bcId);
		// socialDao.add(t);
		// }
		//
		// it = entity.getWebsites().iterator();
		// while (it.hasNext()) {
		// Website t = (Website) it.next();
		// t.setBusinessCard(entity);
		// t.getId().setBcId(bcId);
		// websiteDao.add(t);
		// }
		// it = entity.getBcPermissionses().iterator();
		// while (it.hasNext()) {
		// BcPermissions p = (BcPermissions) it.next();
		// p.setBusinessCard(entity);
		// permissionsDao.add(p);
		// }
		//
		// // permissionsDao.add(new BcPermissions(entity.getUserSubscription(),
		// // entity));
		//
		// return bcId;

	}

	private int processBusinessCardAdding(BusinessCard entity,
			UploadedFile file, List<UploadedFile> uploads)
			throws HibernateException, IOException {
		int bcId = (Integer) sessionFactory.getCurrentSession().save(entity);

		Address a = entity.getAddress();
		if (a != null) {
			a.setBusinessCardBusinessCardId(bcId);
			a.setBusinessCard(entity);
			addressDao.add(a);
		}
		Iterator it = entity.getTitles().iterator();
		while (it.hasNext()) {
			Title t = (Title) it.next();
			t.setBusinessCard(entity);
			titleDao.add(t);
		}

		it = entity.getEmails().iterator();
		while (it.hasNext()) {
			Email t = (Email) it.next();
			t.setBusinessCard(entity);
			t.getId().setBcId(bcId);
			emailDao.add(t);
		}
		it = entity.getFaxes().iterator();
		while (it.hasNext()) {
			Fax t = (Fax) it.next();
			t.setBusinessCard(entity);
			t.getId().setBcId(bcId);
			faxDao.add(t);
		}
		it = entity.getIms().iterator();
		while (it.hasNext()) {
			Im t = (Im) it.next();
			t.setBusinessCard(entity);
			t.getId().setBcId(bcId);
			imDao.add(t);
		}
		it = entity.getMobiles().iterator();
		while (it.hasNext()) {
			Mobile t = (Mobile) it.next();
			t.setBusinessCard(entity);
			t.getId().setBcId(bcId);
			mobileDao.add(t);
		}
		it = entity.getPhones().iterator();
		while (it.hasNext()) {
			Phone t = (Phone) it.next();
			t.setBusinessCard(entity);
			t.getId().setBcId(bcId);
			phoneDao.add(t);
		}
		it = entity.getSocialNetworks().iterator();
		while (it.hasNext()) {
			SocialNetwork t = (SocialNetwork) it.next();
			t.setBusinessCard(entity);
			t.getId().setBcId(bcId);
			socialDao.add(t);
		}

		it = entity.getWebsites().iterator();
		while (it.hasNext()) {
			Website t = (Website) it.next();
			t.setBusinessCard(entity);
			t.getId().setBcId(bcId);
			websiteDao.add(t);
		}
		it = entity.getBcPermissionses().iterator();
		while (it.hasNext()) {
			BcPermissions p = (BcPermissions) it.next();
			p.setBusinessCard(entity);
			permissionsDao.add(p);
		}

		if (file != null) {
			Attachment attachment = new Attachment(entity, file.getFileName());
			FilesUtil.uploadFile(file, bcId);
		}

		if (uploads != null && uploads.size() > 0) {
			entity.setAttachments(new HashSet(0));
			for (UploadedFile uploadedFile : uploads) {
				Attachment upload = new Attachment(entity,
						uploadedFile.getFileName());
				FilesUtil.uploadFile(uploadedFile, bcId);
				entity.getAttachments().add(upload);
			}
			it = entity.getAttachments().iterator();
			while (it.hasNext()) {
				Attachment t = (Attachment) it.next();
				t.setBusinessCard(entity);
				attachementDao.add(t);
			}
		}

		return bcId;
	}

	@Transactional
	public Object add(BusinessCard entity, UploadedFile file)
			throws HibernateException, IOException {
		return processBusinessCardAdding(entity, file, null);
	}

	@Transactional
	public Object add(BusinessCard entity, UploadedFile file,
			List<UploadedFile> uploads) throws HibernateException, IOException {
		return processBusinessCardAdding(entity, file, uploads);
	}

	public String validateBusinessCard(BusinessCard businessCard,
			boolean updateMode) {
		boolean valid = true;
		String error = "";
		Iterator it;
		if (!businessCard.getMobiles().isEmpty() && error.equals("")) {
			it = businessCard.getMobiles().iterator();
			while (it.hasNext()) {
				if (mobileDao.subscriptionHasMobile(((Mobile) it.next())
						.getId().getMobileNum(), businessCard
						.getUserSubscription(),
						(updateMode ? businessCard.getBusinessCardId() : 0))) {
					error = "m";
					break;
				}
			}
		}
		if (!businessCard.getPhones().isEmpty() && error.equals("")) {
			it = businessCard.getPhones().iterator();
			while (it.hasNext()) {
				if (phoneDao.subscriptionHasPhone(((Phone) it.next()).getId()
						.getPhoneNum(), businessCard.getUserSubscription(),
						(updateMode ? businessCard.getBusinessCardId() : 0))) {
					error = "p";
					break;
				}
			}
		}
		// if (!businessCard.getFaxes().isEmpty() && error.equals("")) {
		// it = businessCard.getFaxes().iterator();
		// while (it.hasNext()) {
		// if (faxDao.subscriptionHasFax(((Fax) it.next()).getId()
		// .getFaxNum(), businessCard.getUserSubscription())) {
		// error = "f";
		// break;
		// }
		// }
		// }
		if (!businessCard.getEmails().isEmpty() && error.equals("")) {
			it = businessCard.getEmails().iterator();
			while (it.hasNext()) {
				if (emailDao.subscriptionHasEmail(((Email) it.next()).getId().getEmailAddress(), businessCard.getUserSubscription(),
						(updateMode ? businessCard.getBusinessCardId() : 0))) {
					error = "e";
					break;
				}
			}
		}
		if (error.equals(""))
			return null;
		return error;

	}

	@Override
	// @Transactional
	public void delete(BusinessCard entity) throws HibernateException {
		try {

			deleteFromBusinessCardRelatedTables(entity);
			sessionFactory.getCurrentSession().delete(entity);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This is a helper method to delete all records related to a business card
	 * in many tables (e.g. email, mobile, fax, IM..)
	 * 
	 * @param bcCard
	 */
	@Transactional
	private void deleteFromBusinessCardRelatedTables(BusinessCard bcCard) {
		BusinessCard bcToBeDeleted = bcCard;
		// delete old card relations

		Iterator it = bcToBeDeleted.getEmails().iterator();
		while (it.hasNext()) {
			emailDao.delete((Email) it.next());
		}
		it = bcToBeDeleted.getFaxes().iterator();
		while (it.hasNext()) {
			faxDao.delete((Fax) it.next());
		}
		it = bcToBeDeleted.getIms().iterator();
		while (it.hasNext()) {
			imDao.delete((Im) it.next());
		}
		it = bcToBeDeleted.getMobiles().iterator();
		while (it.hasNext()) {
			mobileDao.delete((Mobile) it.next());
		}
		it = bcToBeDeleted.getPhones().iterator();
		while (it.hasNext()) {
			phoneDao.delete((Phone) it.next());
		}
		it = bcToBeDeleted.getSocialNetworks().iterator();
		while (it.hasNext()) {
			socialDao.delete((SocialNetwork) it.next());
		}
		it = bcToBeDeleted.getTitles().iterator();
		while (it.hasNext()) {
			titleDao.delete((Title) it.next());
		}
		it = bcToBeDeleted.getWebsites().iterator();
		while (it.hasNext()) {
			websiteDao.delete((Website) it.next());
		}
		Address address = bcToBeDeleted.getAddress();
		if (address != null) {
			addressDao.delete(address);
		}
		it = bcToBeDeleted.getBcPermissionses().iterator();
		while (it.hasNext()) {
			BcPermissions bcPerm = (BcPermissions) it.next();
			permissionsDao.delete(bcPerm);
		}
		it = bcToBeDeleted.getTagses().iterator();
		while (it.hasNext()) {
			tagsDao.delete((Tags) it.next());
		}
	}

	@Transactional
	public void update(BusinessCard entity, UploadedFile avatar,
			List<UploadedFile> uploads) throws HibernateException, IOException {

		BusinessCard oldCard = (BusinessCard) sessionFactory
				.getCurrentSession().load(BusinessCard.class,
						entity.getBusinessCardId());

		Iterator it = null;
		logger.debug("updating bc: " + entity.getBusinessCardId());
		// upate old card
		// sessionFactory.getCurrentSession().update(entity);
		// Address update
		addressDao.update(entity.getAddress());
		if (avatar != null) {
			try {
				logger.debug("avatar not null, upload file");
				FilesUtil.uploadFile(avatar, entity.getBusinessCardId());
			} catch (IOException e) {
				logger.error("Error while uploading avatar");
				e.printStackTrace();
			}
		}
		// get old card
		// BusinessCard oldCard = this.get(entity.getBusinessCardId(), true);
		// delete old card relations

		it = oldCard.getEmails().iterator();
		while (it.hasNext()) {
			logger.debug("deleting mail");
			Email e = (Email) it.next();
			// oldCard.getEmails().remove(e);
			it.remove();
			emailDao.delete(e);
		}
		it = oldCard.getFaxes().iterator();
		while (it.hasNext()) {
			logger.debug("deleting fax");
			Fax f = (Fax) it.next();
			// oldCard.getFaxes().remove(f);
			it.remove();
			faxDao.delete(f);
		}
		it = oldCard.getIms().iterator();
		while (it.hasNext()) {
			logger.debug("deleting im");
			Im im = (Im) it.next();
			// oldCard.getIms().remove(im);
			it.remove();
			imDao.delete(im);
		}
		it = oldCard.getMobiles().iterator();
		while (it.hasNext()) {
			logger.debug("deleting mobile");
			Mobile m = (Mobile) it.next();
			// oldCard.getMobiles().remove(m);
			it.remove();
			mobileDao.delete(m);
		}
		it = oldCard.getPhones().iterator();
		while (it.hasNext()) {
			logger.debug("deleting phone");
			Phone p = (Phone) it.next();
			// oldCard.getPhones().remove(p);
			it.remove();
			phoneDao.delete(p);
		}
		it = oldCard.getSocialNetworks().iterator();
		while (it.hasNext()) {
			logger.debug("deleting social");
			SocialNetwork sn = (SocialNetwork) it.next();
			// oldCard.getSocialNetworks().remove(sn);
			it.remove();
			socialDao.delete(sn);
		}
		it = oldCard.getTitles().iterator();
		while (it.hasNext()) {
			logger.debug("deleting title");
			Title t = (Title) it.next();
			// oldCard.getTitles().remove(t);
			it.remove();
			titleDao.delete(t);
		}
		it = oldCard.getWebsites().iterator();
		while (it.hasNext()) {
			logger.debug("deleting website");
			Website w = (Website) it.next();
			// oldCard.getWebsites().remove(w);
			it.remove();
			websiteDao.delete(w);
		}
		it = oldCard.getAttachments().iterator();
		while (it.hasNext()) {
			logger.debug("deleting attachements");
			Attachment att = (Attachment) it.next();
			// oldCard.getAttachments().remove(att);
			it.remove();
			attachementDao.delete(att);
		}
		it = oldCard.getBcPermissionses().iterator();
		while (it.hasNext()) {
			logger.debug("deleting attachements");
			BcPermissions att = (BcPermissions) it.next();
			// oldCard.getAttachments().remove(att);
			it.remove();
			bcPersmissionsDao.delete(att);
		}

		// insert new relations
		it = entity.getEmails().iterator();
		while (it.hasNext()) {
			Email e = (Email) it.next();
			e.getId().setBcId(entity.getBusinessCardId());
			logger.debug("adding email " + e.getId().getEmailAddress());
			emailDao.add(e);
		}
		it = entity.getFaxes().iterator();
		while (it.hasNext()) {
			Fax f = (Fax) it.next();
			f.getId().setBcId(entity.getBusinessCardId());
			logger.debug("adding fax " + f.getId().getFaxNum());
			faxDao.add(f);
		}
		it = entity.getIms().iterator();
		while (it.hasNext()) {
			Im im = (Im) it.next();
			im.getId().setBcId(entity.getBusinessCardId());
			logger.debug("adding im " + im.getId().getIdentifier());
			imDao.add(im);
		}
		it = entity.getMobiles().iterator();
		while (it.hasNext()) {
			Mobile m = (Mobile) it.next();
			m.getId().setBcId(entity.getBusinessCardId());
			logger.debug("adding mobile " + m.getId().getMobileNum());
			mobileDao.add(m);
		}
		it = entity.getPhones().iterator();
		while (it.hasNext()) {
			Phone p = (Phone) it.next();
			p.getId().setBcId(entity.getBusinessCardId());
			logger.debug("adding phone " + p.getId().getPhoneNum());
			phoneDao.add(p);
		}
		it = entity.getSocialNetworks().iterator();
		while (it.hasNext()) {
			SocialNetwork sn = (SocialNetwork) it.next();
			sn.getId().setBcId(entity.getBusinessCardId());
			logger.debug("adding social net " + sn.getId().getIdentifier());
			socialDao.add(sn);
		}
		it = entity.getTitles().iterator();
		while (it.hasNext()) {
			Title t = (Title) it.next();
			t.setBusinessCard(entity);
			logger.debug("adding title title" + t.getTitle());
			logger.debug("adding title comany" + t.getCompany());
			logger.debug("adding title dep" + t.getDepartment());
			//logger.debug("adding title sector id" + t.getSector().getSectorId());
			titleDao.add(t);
		}
		it = entity.getWebsites().iterator();
		while (it.hasNext()) {
			Website w = (Website) it.next();
			w.getId().setBcId(entity.getBusinessCardId());
			logger.debug("adding website" + w.getId().getWebsite());
			websiteDao.add(w);
		}
		it = entity.getAttachments().iterator();
		while (it.hasNext()) {
			Attachment w = (Attachment) it.next();
			w.setBusinessCard(entity);
			logger.debug("adding attachement" + w.getPath());
			attachementDao.add(w);
		}
		it = entity.getBcPermissionses().iterator();
		while (it.hasNext()) {
			BcPermissions w = (BcPermissions) it.next();
			w.setBusinessCard(entity);
			bcPersmissionsDao.add(w);
		}
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();

		if (uploads != null && uploads.size() > 0) {
			for (UploadedFile uploadedFile : uploads) {
				Attachment upload = new Attachment(entity,
						uploadedFile.getFileName());
				FilesUtil.uploadFile(uploadedFile, entity.getBusinessCardId());
				entity.getAttachments().add(upload);
				upload.setBusinessCard(entity);
				upload.setBusinessCard(entity);
				attachementDao.add(upload);
			}
			// it = entity.getAttachments().iterator();
			// while (it.hasNext()) {
			// Attachment t = (Attachment) it.next();
			// t.setBusinessCard(entity);
			// attachementDao.add(t);
			// }
		}

		sessionFactory.getCurrentSession().update(entity);

		// upate old card

		// TO_DO: Tags
		// TO_DO: Update permissions
		// Tags and permissions are removed in business layer
		// user entity.getTagses().remove(tag)

	}

	public List<BusinessCard> advancedFilter(int first, int pageSize,
			int sortField, Map<String, Object> filters, boolean findOnContacts,
			boolean personalContacts, boolean corpContacts) {
		int scope = calculateFilterScope(findOnContacts, personalContacts,
				corpContacts);
		Session session = sessionFactory.getCurrentSession();
		System.out.println(filters.get(FiltersKeys.FIRST_NAME_KEY));
		Query query = session
				.createSQLQuery(
						"CALL advancedFilter (:page_size ,:offsetCount"
								+ " ,:user_id_param ,:sub_type_param"
								+ " ,:first_name_param ,:last_name_param"
								+ " ,:title_param ,:company_param"
								+ " ,:mobile_param ,:email_param"
								+ " ,:sector_name_param ,:tags_param"
								+ " ,:order_by_param, :owner_id_param"
								+ " ,:keyword_param, :country_param"
								+ " ,:state_param, :city_param);")
				.addEntity(BusinessCard.class)
				.setParameter("page_size", pageSize)
				.setParameter("offsetCount", first)
				//TODO same for count
				.setParameter("user_id_param",(int) filters.get(FiltersKeys.USER_ID_KEY))
				.setParameter("sub_type_param", scope)
				.setParameter("first_name_param",
						(String) filters.get(FiltersKeys.FIRST_NAME_KEY))
				.setParameter("last_name_param",
						(String) filters.get(FiltersKeys.LAST_NAME_KEY))
				.setParameter("title_param",
						(String) filters.get(FiltersKeys.TITLE_KEY))
				.setParameter("company_param",
						(String) filters.get(FiltersKeys.COMPANY_KEY))
				.setParameter("mobile_param",
						(String) filters.get(FiltersKeys.MOBILE_KEY))
				.setParameter("email_param",
						(String) filters.get(FiltersKeys.EMAIL_KEY))
				.setParameter("sector_name_param",
						(String) filters.get(FiltersKeys.SECTOR_KEY))
				.setParameter("tags_param",
						(String) filters.get(FiltersKeys.TAGS_KEY))
				.setParameter("order_by_param", sortField)
				.setParameter("owner_id_param",
						(int) filters.get(FiltersKeys.OWNER_ID_KEY))
				.setParameter("keyword_param",
						(String) filters.get(FiltersKeys.KEYWORD_KEY))
				.setParameter("country_param",
						(String) filters.get(FiltersKeys.COUNTRY_KEY))
				.setParameter("state_param",
						(String) filters.get(FiltersKeys.STATE_KEY))
				.setParameter("city_param",
						(String) filters.get(FiltersKeys.CITY_KEY));
		List<BusinessCard> result = query.list();
		if (result != null) {
			System.out.println(result.size());
			// logger.info("Found user with email = " + userEmail +
			// " in the db");
			return result;
		}
		return null;

	}

	public int getAdvencedFilterRowCount(int first, int pageSize,
			Integer sortField, Map<String, Object> filters,
			boolean findOnContacts, boolean personalContacts,
			boolean corpContacts) {
		int scope = calculateFilterScope(findOnContacts, personalContacts,
				corpContacts);
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery(
						"CALL advancedFilterRowCount (:page_size ,:offsetCount"
								+ " ,:user_id_param ,:sub_type_param"
								+ " ,:first_name_param ,:last_name_param"
								+ " ,:title_param ,:company_param"
								+ " ,:mobile_param ,:email_param"
								+ " ,:sector_name_param ,:tags_param"
								+ " ,:order_by_param, :owner_id_param"
								+ " ,:keyword_param, :country_param"
								+ " ,:state_param, :city_param);")

				.setParameter("page_size", pageSize)
				.setParameter("offsetCount", first)
				.setParameter("user_id_param",
						(Integer) filters.get(FiltersKeys.USER_ID_KEY))
				.setParameter("sub_type_param", scope)
				.setParameter("first_name_param",
						(String) filters.get(FiltersKeys.FIRST_NAME_KEY))
				.setParameter("last_name_param",
						(String) filters.get(FiltersKeys.LAST_NAME_KEY))
				.setParameter("title_param",
						(String) filters.get(FiltersKeys.TITLE_KEY))
				.setParameter("company_param",
						(String) filters.get(FiltersKeys.COMPANY_KEY))
				.setParameter("mobile_param",
						(String) filters.get(FiltersKeys.MOBILE_KEY))
				.setParameter("email_param",
						(String) filters.get(FiltersKeys.EMAIL_KEY))
				.setParameter("sector_name_param",
						(String) filters.get(FiltersKeys.SECTOR_KEY))
				.setParameter("tags_param",
						(String) filters.get(FiltersKeys.TAGS_KEY))
				.setParameter("order_by_param", sortField)
				.setParameter("owner_id_param",
						(int) filters.get(FiltersKeys.OWNER_ID_KEY))
				.setParameter("keyword_param",
						(String) filters.get(FiltersKeys.KEYWORD_KEY))
				.setParameter("country_param",
						(String) filters.get(FiltersKeys.COUNTRY_KEY))
				.setParameter("state_param",
						(String) filters.get(FiltersKeys.STATE_KEY))
				.setParameter("city_param",
						(String) filters.get(FiltersKeys.CITY_KEY));
		List<BigInteger> result = query.list();
		if (result != null && result.size() != 0) {
			// logger.info("Found user with email = " + userEmail +
			// " in the db");
			return result.get(0).intValue();
		}
		return 0;
	}

	private int calculateFilterScope(boolean findOnContacts,
			boolean personalContacts, boolean corpContacts) {
		if (!findOnContacts && !personalContacts && corpContacts)
			return 1;
		if (!findOnContacts && personalContacts && !corpContacts)
			return 2;
		if (!findOnContacts && personalContacts && corpContacts)
			return 3;
		if (findOnContacts && !personalContacts && !corpContacts)
			return 4;
		if (findOnContacts && !personalContacts && corpContacts)
			return 5;
		if (findOnContacts && personalContacts && !corpContacts)
			return 6;
		return 7;
	}

	public BusinessCard getUserOwnCard(int userId) throws Exception {
		logger.info("Trying to get own card for user: " + userId);
		Session session = sessionFactory.getCurrentSession();
		BusinessCard bc = null;
		Query q = session
				.createQuery("from BusinessCard where creator = :creator and subscription_user_id is :subscription_user_id");
		q.setParameter("creator", userId);
		q.setParameter("subscription_user_id", null);

		List<BusinessCard> l = (ArrayList<BusinessCard>) q.list();
		if (l != null && l.size() != 0) {
			bc = (BusinessCard) l.get(0);
			logger.info("Found business card for user " + userId
					+ ", and business card id " + bc.getBusinessCardId());
		}
		logger.info("returning from getUserOwnCard ");

		return get(bc.getBusinessCardId(), true);

	}

}
