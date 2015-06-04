package com.edexer.service;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.logging.Logger;

import junit.framework.TestCase;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.dao.FiltersKeys;
import com.edexer.dao.MobileEntityDaoImpl;
import com.edexer.dao.TagsEntityDaoImpl;
import com.edexer.dao.UserEntityDaoImpl;
import com.edexer.model.Address;
import com.edexer.model.BusinessCard;
import com.edexer.model.Countries;
import com.edexer.model.Email;
import com.edexer.model.EmailId;
import com.edexer.model.Fax;
import com.edexer.model.FaxId;
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
import com.edexer.model.UserSubscriptionId;
import com.edexer.model.Website;
import com.edexer.model.WebsiteId;
import com.edexer.util.ExcelExporter;
import com.edexer.util.FiltersUtils;

import org.apache.log4j.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class BusinessCardServiceManagerImplTest extends TestCase {

	private static final Logger LOGGER = Logger
			.getLogger(BusinessCardServiceManagerImplTest.class.getName());
	@Autowired
	BusinessCardServiceManager bcService;
	@Autowired
	LookUpsServiceManager lookupService;
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	MobileEntityDaoImpl mobileDao;
	@Autowired
	UserEntityDaoImpl userdao;
	// @Autowired
	// TagsEntityDaoImpl tagsDao;

	BusinessCard bc;

	// @Test
	public void testExport() {
		System.out.println("testExport => start");
		System.out.println(Calendar.getInstance().getTime().toString());
		Map<String, Object> filters = new HashedMap();
		filters.put(FiltersKeys.COMPANY_KEY, "");
		filters.put(FiltersKeys.EMAIL_KEY, "");
		filters.put(FiltersKeys.FIRST_NAME_KEY, "");
		filters.put(FiltersKeys.LAST_NAME_KEY, "");
		filters.put(FiltersKeys.MOBILE_KEY, "");
		filters.put(FiltersKeys.SECTOR_KEY, "");
		filters.put(FiltersKeys.SUB_TYPE_KEY, 1);
		filters.put(FiltersKeys.TAGS_KEY, "1,2");
		filters.put(FiltersKeys.TITLE_KEY, "");
		filters.put(FiltersKeys.USER_ID_KEY, 1);
		filters = FiltersUtils.constructFilter(59, 7, "", "", "", "", "", "",
				"", "", FiltersKeys.ORDER_BY_ID_ASC, 0, "", "", "", "");
		List<BusinessCard> bcList = bcService.advancedFilter(59, 7,
				FiltersKeys.ORDER_BY_ID_ASC, filters, true, true, true);
		User user = userdao.get(User.class, 59);
		System.out.println("testExport => list size: " + bcList.size());
		try {
			bcService.exportBusinessCards(bcList, user);
			System.out.println("testExport => Exported");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("testExport => export failed ");
			e.printStackTrace();
		}
		System.out.println("testExport => end");
	}

	// @Test
	public void readWorkbook() {
		LOGGER.info("Mail sent Successfully to :");
		LOGGER.debug("Mail sent Successfully to :");
		LOGGER.error("Mail sent Successfully to :");
		User u = userdao.getUserByUserId(59);
		System.out.println("user");
		File f = new File(
				"E://Repositories//Edexer//Documentation//template.xlsx");
		bcService.uploadWorkbook(f, "xlsx", u, true);
	}

	// @Test
	public void testadvancedFilterRowCount() {
		System.out.println("Test Advenced Filter Row Count");
		Map<String, Object> filters = new HashedMap();
		filters.put(FiltersKeys.COMPANY_KEY, "");
		filters.put(FiltersKeys.EMAIL_KEY, "");
		filters.put(FiltersKeys.FIRST_NAME_KEY, "");
		filters.put(FiltersKeys.LAST_NAME_KEY, "");
		filters.put(FiltersKeys.MOBILE_KEY, "");
		filters.put(FiltersKeys.SECTOR_KEY, "");
		filters.put(FiltersKeys.SUB_TYPE_KEY, 1);
		filters.put(FiltersKeys.TAGS_KEY, "1,2");
		filters.put(FiltersKeys.TITLE_KEY, "");
		filters.put(FiltersKeys.USER_ID_KEY, 1);
		// System.out.println("Row Count: " +
		// bcService.getAdvancedFilterRowCount(0, 10,
		// FiltersKeys.ORDER_BY_ID_ASC, filters));
		System.out.println("Done !");
	}

	// @Test
	public void testFilter() {
		System.out.println("Test Filter");
		Map<String, Object> filters = new HashedMap();
		filters.put(FiltersKeys.COMPANY_KEY, "");
		filters.put(FiltersKeys.EMAIL_KEY, "");
		filters.put(FiltersKeys.FIRST_NAME_KEY, "");
		filters.put(FiltersKeys.LAST_NAME_KEY, "");
		filters.put(FiltersKeys.MOBILE_KEY, "");
		filters.put(FiltersKeys.SECTOR_KEY, "");
		filters.put(FiltersKeys.SUB_TYPE_KEY, 1);
		filters.put(FiltersKeys.TAGS_KEY, "1,2");
		filters.put(FiltersKeys.TITLE_KEY, "");
		filters.put(FiltersKeys.USER_ID_KEY, 1);
		// List<BusinessCard> bcList = bcService.advancedFilter(0, 5,
		// FiltersKeys.ORDER_BY_ID_ASC, filters);
		// System.out.println("Filtered: " + bcList.size());
		// System.out.println("Row Count: " +
		// bcService.getAdvancedFilterRowCount(0, 5,
		// FiltersKeys.ORDER_BY_ID_ASC, filters));
		System.out.println("Done !");
		// call advancedFilter(500,0,1,1,"","","","","","","","",81);
	}

	//@Transactional
	 //@Test
	public void getBusinessCard() {
		int BcId = 78;
		BusinessCard bc = bcService.get(BcId, true);

		System.out.println(bc.getUserSubscription().getId().getSubType());
		System.out.println(((Mobile) bc.getMobiles().iterator().next()).getId()
				.getMobileNum());
		System.out.println(bc.getBcFirstName());
		Iterator<Mobile> it = bc.getMobiles().iterator();
		while (it.hasNext()) {
			Mobile M = (Mobile) it.next();
			if (M.getId().getMobileNum().equals("46854163241")) {
				// boolean f = M.equals(M);
				System.out.println(bc.getMobiles().size());
				bc.getMobiles().remove(M);

				System.out.println(bc.getMobiles().size());
				Mobile loadedMobile = (Mobile) sessionFactory
						.getCurrentSession().get(Mobile.class, M.getId());
				sessionFactory.getCurrentSession().delete(loadedMobile);
				sessionFactory.getCurrentSession().flush();
			}
		}

		// bc.getMobiles().add(new Mobile(new
		// MobileId(bc.getBusinessCardId(),"546545451"),bc));
		// bc.setBcFirstName("new name");
		// bcService.updateBusinessCard(bc);
		// System.out.println("Updated");
	}

	// @Test
	public void addBusinessCard() {
		System.out.println("Add businessCard start");
		Set titles = new HashSet(0);
		Sector s = new Sector();
		s.setSectorId(1);
		titles.add(new Title(s, this.bc, "comp1", "dep1", "title1"));

		Set emails = new HashSet(0);
		emails.add(new Email(new EmailId(0, "saadsaleh88@gmail.com"), null));
		Set faxes = new HashSet(0);
		faxes.add(new Fax(new FaxId(0, "5454151"), null));
		Countries a = new Countries();
		a.setIdCountry(10);
		Address address = new Address(null, a, "streeeet1", "streeet2", "city",
				"state", "");

		Set bcPermissionses = new HashSet(0);

		Set phones = new HashSet(0);
		phones.add(new Phone(new PhoneId(0, "5454151"), null));

		Set socialNetworks = new HashSet(0);
		SocialNetworksTypes snt = new SocialNetworksTypes();
		snt.setSnTypeId(1);
		socialNetworks.add(new SocialNetwork(new SocialNetworkId(0, "5454151"),
				snt,null ));

		Set websites = new HashSet(0);
		websites.add(new Website(new WebsiteId("www.google.com", 0), null));
		Set attachments = new HashSet(0);
		Set tagses = new HashSet(0);

		// tagses.add(tagsDao.get(Tags.class, 1));

		Set mobiles = new HashSet(0);
		mobiles.add(new Mobile(new MobileId(0, "01112957751"), null));
		Set ims = new HashSet(0);
		UserSubscription us = new UserSubscription();
		us.setId(new UserSubscriptionId(1, 1));
		// bc = new BusinessCard(userSubscription, bcFirstName, middleName,
		// bcLastName, birthDate, notes, creator, titles, emails, faxes,
		// address, bcPermissionses, phones, socialNetworks, websites,
		// attachments, tagses, mobiles, ims)

		// new BusinessCard(userSubscription,
		// bcFirstName, middleName, bcLastName, birthDate, notes,
		// creator, mobiles, address, socialNetworks, faxes, tagses, emails,
		// attachments, websites, ims, titles, bcPermissionses, phones)

		// bc = new BusinessCard(us, "fName1", "mName1", "Mohamed", "29/9/1988",
		// "fewfewfewfewfew", 1, mobiles, address, socialNetworks, faxes,
		// tagses, emails, attachments, websites, ims, titles,
		// bcPermissionses, phones);
		// System.out.println("Add businessCard: bc created");

		// boolean b = bcService.validateBusinessCard(bc);
		int id = bcService.addBusinessCard(bc);
		System.out.println("Card Id: " + id);
		System.out.println("Add businessCard: added");

		// bc = bcService.get(id, true);
		bc.setBcFirstName("bla bla bla");
		// bcService.updateBusinessCard(bc);
		System.out.println("Updated");

	}

	@Test
	public void deletebc() {
		try {
			BusinessCard bc = bcService.get(8, true);
			bcService.deleteBusinessCard(bc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
