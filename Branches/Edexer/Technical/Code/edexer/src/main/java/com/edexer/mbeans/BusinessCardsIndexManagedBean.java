package com.edexer.mbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang3.StringEscapeUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.dao.FiltersKeys;
import com.edexer.model.BusinessCard;
import com.edexer.model.Email;
import com.edexer.model.Mobile;
import com.edexer.model.Tags;
import com.edexer.model.Title;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;
import com.edexer.util.FiltersUtils;

@ManagedBean
@ViewScoped
public class BusinessCardsIndexManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3287118779806389602L;

	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;

	@ManagedProperty("#{businessCardLazyDataModel}")
	private BusinessCardLazyDataModel bcLazyModel;

	@ManagedProperty("#{sessionManagedBean}")
	private SessionManagedBean sessionBean;

	@ManagedProperty("#{tagsManagedBean}")
	private TagsManagedBean tagsManagedBean;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubscriptionService;

	private Boolean disapleBtns;
	private Boolean enabletoolTips;
	private BusinessCard selectedBusinessCard;
	private BusinessCard deleteSelectBusinessCard;
	ResourceBundle settingsBundle = ResourceBundle.getBundle("/settings");
	private String filterFirstName;
	private String filterLlastName;
	private String filterTitle;
	private String filterCompany;
	private String filterEmail;
	private String filterSector;
	private String filterMobile;
	private String filterKeyword;
	private String filterCountry;
	private String filterState;
	private String filterCity;
	private boolean filterFindOnContactsRetrieve;
	private boolean filterPersonalContactsRetrieve;
	private boolean filterCorpContactsRetrieve;
	private List<Tags> selectedTags;
	private List<String> selectedSubscriptions;
	private String filterSelectByField;
	private String filterSelectByDir;
	private int rowCount;
	Map<String, Object> tempFilter;
	private User userFromSession;
	private int corpSubscriptionOwnerId;
	private List<BusinessCard> selectedCardList;
	private boolean SelectionStat;
	private boolean switchTableGridView;
	private final static String CHANNEL = "/notify";
	private List<BusinessCard> listBCards;
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

		userFromSession = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);
		UserSubscription corpSubscription = userSubscriptionService
				.getUserSubscriptionByType(userFromSession
						.getUserSubscriptionsForUserId(), Integer
						.parseInt(settingsBundle
								.getString("SUBSCRIPTION_TYPE_CORP")));
		//int corpSubscriptionOwnerId = Constants.CORP_OWNER;
		 corpSubscriptionOwnerId = (corpSubscription == null ? 0
				: corpSubscription.getUserBySubscriptionOwner().getUserId());
		selectedTags = new ArrayList<Tags>();
		selectedSubscriptions = new ArrayList<String>();
		selectedSubscriptions.add("p");
		selectedSubscriptions.add("c");

		tempFilter = FiltersUtils.constructFilter(userFromSession.getUserId(),
				7, "", "", "", "", "", "", "", "", FiltersKeys.ORDER_BY_ID_ASC,
				Constants.CORP_OWNER, sessionBean.getSearchKeyWord(), "", "",
				"");
		tempFilter.put(FiltersKeys.FIND_ON_KEY, false);
		tempFilter.put(FiltersKeys.PERSONAL_KEY, true);
		tempFilter.put(FiltersKeys.CORP_KEY, true);

		// tempFilter = FiltersUtils.constructFilter(((User) HttpSessionUtil
		// .getSession().getAttribute(Constants.USER)).getUserId(), 7, "",
		// "", "", "", "", "", "", "", FiltersKeys.ORDER_BY_ID_ASC,
		// corpSubscriptionOwnerId, (filterKeyword == null) ? ""
		// : filterKeyword, "", "", "");
		rowCount = bcService.getAdvancedFilterRowCount(0, 10,
				(int) tempFilter.get(FiltersKeys.ORDER_BY_KEY), tempFilter,
				filterFindOnContactsRetrieve, filterPersonalContactsRetrieve,
				filterCorpContactsRetrieve);

		// selectedCardList = new ArrayList<BusinessCard>();
		selectedCardList = (List<BusinessCard>) HttpSessionUtil
				.getSession()
				.getAttribute(Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);
		if (selectedCardList == null) {
			HttpSessionUtil.getSession().setAttribute(
					Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY,
					new ArrayList<BusinessCard>());
			selectedCardList = (List<BusinessCard>) HttpSessionUtil
					.getSession().getAttribute(
							Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);
		}
		disableBtns();
		enableToolTips();
		// set scope = 7 by default at page load
		filterFindOnContactsRetrieve = false;
		filterPersonalContactsRetrieve = true;
		filterCorpContactsRetrieve = true;
		sessionBean.setSearchKeyWord("");
	}

	// public void send() {
	// EventBus eventBus = EventBusFactory.getDefault().eventBus();
	// eventBus.publish(CHANNEL,
	// new FacesMessage(StringEscapeUtils.escapeHtml3("wgetry"),
	// StringEscapeUtils.escapeHtml3("trhutrkl")));
	// }
	  public void deleteBulkBusinessCard() {
			try {
				listBCards = (List<BusinessCard>) HttpSessionUtil.getSession()
						.getAttribute(
								Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);
				if (listBCards.size() > 0) {
					for (BusinessCard bc : listBCards) {
						int bcId = bc.getBusinessCardId();
						deleteBusinessCard(bcId);
					}
					rowCount = bcService.getAdvancedFilterRowCount(
							0,
							10,
							(int) bcLazyModel.getFilters().get(
									FiltersKeys.ORDER_BY_KEY),
							bcLazyModel.getFilters(), filterFindOnContactsRetrieve,
							filterPersonalContactsRetrieve,
							filterCorpContactsRetrieve);
					selectedCardList = new ArrayList<BusinessCard>();
					HttpSessionUtil.getSession().setAttribute(
							Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY,
							selectedCardList);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	  public void disableBtns() {
			if (((List<BusinessCard>) HttpSessionUtil.getSession().getAttribute(
					Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY)).size() > 0)
				disapleBtns=false;	
			else
				disapleBtns=true;
		}
	public void enableToolTips() {
		if (((List<BusinessCard>) HttpSessionUtil.getSession().getAttribute(
				Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY)).size() > 0)
			enabletoolTips = false;
		else
			enabletoolTips = true;
	}

	public void toggleView(boolean showTable) {
		switchTableGridView = showTable;
	}

	public void checkAll() {
		try {
			SortOrder sort = SortOrder.ASCENDING;
			List<BusinessCard> list = new ArrayList<BusinessCard>();
			Map<String, Object> filters = bcLazyModel.getFilters();
			list = bcService.advancedFilter(0, 100000,
					FiltersKeys.ORDER_BY_ID_ASC, filters, false,
					(boolean) filters.get(FiltersKeys.PERSONAL_KEY),
					(boolean) filters.get(FiltersKeys.CORP_KEY));
			// selectedCardList = new ArrayList<BusinessCard>();
			// loadSelectedBusinessCardListener(list.get(1).getBusinessCardId().toString());
			selectedCardList = list;
			HttpSessionUtil.getSession().setAttribute(
					Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY,
					selectedCardList);
			selectedCardList = (List<BusinessCard>) HttpSessionUtil
					.getSession().getAttribute(
							Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);
			disableBtns();
			enableToolTips();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void nonCheckAll() {
		try {
			selectedCardList = new ArrayList<BusinessCard>();
			HttpSessionUtil.getSession().setAttribute(
					Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY,
					selectedCardList);
			disableBtns();
			enableToolTips();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void goToAddCard() {
		String baseUrl = UtilitiesManagesBean.baseUrl;
		// String baseUrl = "/" + settingsBundle.getString("APPLICATION_ROOT");
		String url = baseUrl + "/user/addbusinesscard.xhtml";
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteBusinessCard(int bcId) {
		BusinessCard bc = bcService.get(bcId, true);

		try {
			if (bc != null) {
				bcService.deleteBusinessCard(bc);
				rowCount = bcService.getAdvancedFilterRowCount(
						0,
						10,
						(int) bcLazyModel.getFilters().get(
								FiltersKeys.ORDER_BY_KEY),
						bcLazyModel.getFilters(), filterFindOnContactsRetrieve,
						filterPersonalContactsRetrieve,
						filterCorpContactsRetrieve);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public BusinessCardsIndexManagedBean() {

	}

	public void updteFilterAction() {
		String filterTags = "";
		int orderByToken = 81;
		if (selectedTags != null) {
			for (Tags tag : selectedTags) {
				filterTags += tag.getTagId() + ",";
			}
		}
		if (filterSelectByField != "") {
			orderByToken = Integer.valueOf(filterSelectByField
					+ filterSelectByDir);
		}
		bcLazyModel.getFilters().put(FiltersKeys.FIRST_NAME_KEY,
				filterFirstName);
		bcLazyModel.getFilters().put(FiltersKeys.COMPANY_KEY, filterCompany);
		bcLazyModel.getFilters().put(FiltersKeys.EMAIL_KEY, filterEmail);
		bcLazyModel.getFilters()
				.put(FiltersKeys.LAST_NAME_KEY, filterLlastName);
		bcLazyModel.getFilters().put(FiltersKeys.MOBILE_KEY, filterMobile);
		bcLazyModel.getFilters().put(FiltersKeys.SECTOR_KEY, filterSector);
		bcLazyModel.getFilters().put(FiltersKeys.ORDER_BY_KEY, orderByToken);
		bcLazyModel.getFilters().put(FiltersKeys.TAGS_KEY, filterTags);
		bcLazyModel.getFilters().put(FiltersKeys.TITLE_KEY, filterTitle);

		// if (selectedSubscriptions.size() == 2
		// || selectedSubscriptions.size() == 0) {
		// bcLazyModel.getFilters().put(FiltersKeys.SUB_TYPE_KEY, -1);
		// } else {
		// if (selectedSubscriptions.get(0).equals("p")) {
		// bcLazyModel.getFilters().put(FiltersKeys.SUB_TYPE_KEY, 1);
		// } else {
		// bcLazyModel.getFilters().put(FiltersKeys.SUB_TYPE_KEY, 2);
		// }
		// }
		// for setting subscription
		bcLazyModel.getFilters().put(FiltersKeys.FIND_ON_KEY,
				filterFindOnContactsRetrieve);
		bcLazyModel.getFilters().put(FiltersKeys.PERSONAL_KEY,
				filterPersonalContactsRetrieve);
		bcLazyModel.getFilters().put(FiltersKeys.CORP_KEY,
				filterCorpContactsRetrieve);
		bcLazyModel.getFilters().put(FiltersKeys.OWNER_ID_KEY,
				corpSubscriptionOwnerId);
		bcLazyModel.getFilters().put(FiltersKeys.KEYWORD_KEY, filterKeyword);
		bcLazyModel.getFilters().put(FiltersKeys.COUNTRY_KEY, filterCountry);
		bcLazyModel.getFilters().put(FiltersKeys.STATE_KEY, filterState);
		bcLazyModel.getFilters().put(FiltersKeys.CITY_KEY, filterCity);
		rowCount = bcService.getAdvancedFilterRowCount(0, 10, (int) bcLazyModel
				.getFilters().get(FiltersKeys.ORDER_BY_KEY), bcLazyModel
				.getFilters(), filterFindOnContactsRetrieve,
				filterPersonalContactsRetrieve, filterCorpContactsRetrieve);
	}

	public void resetFilterAction() {
		String filterTags = "";
		int orderByToken = 81;
		filterFirstName = "";
		filterCompany = "";
		filterEmail = "";
		filterLlastName = "";
		filterMobile = "";
		filterSector = "";
		filterTitle = "";
		filterFindOnContactsRetrieve = false;
		filterPersonalContactsRetrieve = true;
		filterCorpContactsRetrieve = true;
		corpSubscriptionOwnerId = userSubscriptionService
				.getSubscriptionOwnerIdForSessionUser();
		filterKeyword = "";
		filterCountry = "";
		filterState = "";
		filterCity = "";

		bcLazyModel.getFilters().put(FiltersKeys.FIRST_NAME_KEY,
				filterFirstName);
		bcLazyModel.getFilters().put(FiltersKeys.COMPANY_KEY, filterCompany);
		bcLazyModel.getFilters().put(FiltersKeys.EMAIL_KEY, filterEmail);
		bcLazyModel.getFilters()
				.put(FiltersKeys.LAST_NAME_KEY, filterLlastName);
		bcLazyModel.getFilters().put(FiltersKeys.MOBILE_KEY, filterMobile);
		bcLazyModel.getFilters().put(FiltersKeys.SECTOR_KEY, filterSector);
		bcLazyModel.getFilters().put(FiltersKeys.ORDER_BY_KEY, orderByToken);
		bcLazyModel.getFilters().put(FiltersKeys.TAGS_KEY, filterTags);
		bcLazyModel.getFilters().put(FiltersKeys.TITLE_KEY, filterTitle);

		// if (selectedSubscriptions.size() == 2
		// || selectedSubscriptions.size() == 0) {
		// bcLazyModel.getFilters().put(FiltersKeys.SUB_TYPE_KEY, -1);
		// } else {
		// if (selectedSubscriptions.get(0).equals("p")) {
		// bcLazyModel.getFilters().put(FiltersKeys.SUB_TYPE_KEY, 1);
		// } else {
		// bcLazyModel.getFilters().put(FiltersKeys.SUB_TYPE_KEY, 2);
		// }
		// }
		// for setting subscription
		bcLazyModel.getFilters().put(FiltersKeys.FIND_ON_KEY,
				filterFindOnContactsRetrieve);
		bcLazyModel.getFilters().put(FiltersKeys.PERSONAL_KEY,
				filterPersonalContactsRetrieve);
		bcLazyModel.getFilters().put(FiltersKeys.CORP_KEY,
				filterCorpContactsRetrieve);
		bcLazyModel.getFilters().put(FiltersKeys.OWNER_ID_KEY,
				corpSubscriptionOwnerId);
		bcLazyModel.getFilters().put(FiltersKeys.KEYWORD_KEY, filterKeyword);
		bcLazyModel.getFilters().put(FiltersKeys.COUNTRY_KEY, filterCountry);
		bcLazyModel.getFilters().put(FiltersKeys.STATE_KEY, filterState);
		bcLazyModel.getFilters().put(FiltersKeys.CITY_KEY, filterCity);
		rowCount = bcService.getAdvancedFilterRowCount(0, 10, (int) bcLazyModel
				.getFilters().get(FiltersKeys.ORDER_BY_KEY), bcLazyModel
				.getFilters(), filterFindOnContactsRetrieve,
				filterPersonalContactsRetrieve, filterCorpContactsRetrieve);
	}

	public void businessCardSelectedListner() {
		selectedCardList.add(selectedBusinessCard);
	}

	public void businessCardUnselectedListner() {
		// boolean val = (boolean)event.getNewValue();
		// selectedCardList.remove(selectedBusinessCard);

		// System.out.println(val);

	}

	public void loadSelectedBusinessCardListener(String bcId) {
		int idFromCheckBox = Integer.valueOf(bcId);
		if (SelectionStat) {
			this.selectedBusinessCard = bcService.get(idFromCheckBox, false);
			selectedCardList.add(selectedBusinessCard);
		} else {
			for (Iterator<BusinessCard> iter = selectedCardList.listIterator(); iter
					.hasNext();) {
				BusinessCard bc = iter.next();
				if (bc.getBusinessCardId() == idFromCheckBox) {
					iter.remove();
				}
			}
		}
		disableBtns();
		enableToolTips();
	}

	public String checkCardSelectionState(String id) {
		int bcId = Integer.valueOf(id);
		for (BusinessCard bc : selectedCardList) {
			if (bc.getBusinessCardId() == bcId)
				return "1";
		}
		return "0";
	}

	public String setToList(int bcId, int item) {
		BusinessCard bc = bcService.get(bcId, true);
		switch (item) {
		case 1:
			// case mobile
			if (bc.getMobiles().iterator().hasNext())
				return ((Mobile) bc.getMobiles().iterator().next()).getId()
						.getMobileNum();
			break;

		case 2:
			// case title
			if (bc.getTitles().iterator().hasNext())
				return ((Title) bc.getTitles().iterator().next()).getTitle();
			break;
		case 3:
			// case company
			if (bc.getTitles().iterator().hasNext())
				return ((Title) bc.getTitles().iterator().next()).getCompany();
			break;
		case 4:
			if (bc.getEmails().iterator().hasNext())
				return ((Email) bc.getEmails().iterator().next()).getId()
						.getEmailAddress();
			break;
		default:
			break;
		}
		return "";
	}

	public List<Tags> getTagsList(String query) {
		return getTagsManagedBean().getTagsListByUser(query);
	}

	public void handleTagSelect(SelectEvent event) {
		Object tag = event.getObject();
		selectedTags.add((Tags) tag);
	}

	public void handleTagUnselect(UnselectEvent event) {
		if (selectedTags != null) {
			Object tag = event.getObject();
			selectedTags.remove((Tags) tag);
		}
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Card Selected",
				((BusinessCard) event.getObject()).getBusinessCardId()
						.toString());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public BusinessCardLazyDataModel getBcLazyModel() {
		return bcLazyModel;
	}

	public void setBcLazyModel(BusinessCardLazyDataModel bcLazyModel) {
		this.bcLazyModel = bcLazyModel;
	}

	public BusinessCard getSelectedBusinessCard() {
		return selectedBusinessCard;
	}

	public void setSelectedBusinessCard(BusinessCard selectedBusinessCard) {
		this.selectedBusinessCard = selectedBusinessCard;
	}

	public BusinessCardServiceManager getBcService() {
		return bcService;
	}

	public void setBcService(BusinessCardServiceManager bcService) {
		this.bcService = bcService;
	}

	public String getFilterFirstName() {
		return filterFirstName;
	}

	public void setFilterFirstName(String filterFirstName) {
		this.filterFirstName = filterFirstName;
	}

	public String getFilterLlastName() {
		return filterLlastName;
	}

	public void setFilterLlastName(String filterLlastName) {
		this.filterLlastName = filterLlastName;
	}

	public String getFilterTitle() {
		return filterTitle;
	}

	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}

	public String getFilterCompany() {
		return filterCompany;
	}

	public void setFilterCompany(String filterCompany) {
		this.filterCompany = filterCompany;
	}

	public String getFilterEmail() {
		return filterEmail;
	}

	public void setFilterEmail(String filterEmail) {
		this.filterEmail = filterEmail;
	}

	public String getFilterSector() {
		return filterSector;
	}

	public void setFilterSector(String filterSector) {
		this.filterSector = filterSector;
	}

	public String getFilterMobile() {
		return filterMobile;
	}

	public void setFilterMobile(String filterMobile) {
		this.filterMobile = filterMobile;
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

	public List<String> getSelectedSubscriptions() {
		return selectedSubscriptions;
	}

	public void setSelectedSubscriptions(List<String> selectedSubscriptions) {
		this.selectedSubscriptions = selectedSubscriptions;
	}

	public String getFilterSelectByField() {
		return filterSelectByField;
	}

	public void setFilterSelectByField(String filterSelectByField) {
		this.filterSelectByField = filterSelectByField;
	}

	public String getFilterSelectByDir() {
		return filterSelectByDir;
	}

	public void setFilterSelectByDir(String filterSelectByDir) {
		this.filterSelectByDir = filterSelectByDir;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public List<BusinessCard> getSelectedCardList() {
		return selectedCardList;
	}

	public void setSelectedCardList(List<BusinessCard> selectedCardList) {
		this.selectedCardList = selectedCardList;
	}

	public boolean isSelectionStat() {
		return SelectionStat;
	}

	public void setSelectionStat(boolean selectionStat) {
		SelectionStat = selectionStat;
	}

	public UserSubscriptionServiceManager getUserSubscriptionService() {
		return userSubscriptionService;
	}

	public void setUserSubscriptionService(
			UserSubscriptionServiceManager userSubscriptionService) {
		this.userSubscriptionService = userSubscriptionService;
	}

	public boolean isFilterFindOnContactsRetrieve() {
		return filterFindOnContactsRetrieve;
	}

	public void setFilterFindOnContactsRetrieve(
			boolean filterFindOnContactsRetrieve) {
		this.filterFindOnContactsRetrieve = filterFindOnContactsRetrieve;
	}

	public boolean isFilterPersonalContactsRetrieve() {
		return filterPersonalContactsRetrieve;
	}

	public void setFilterPersonalContactsRetrieve(
			boolean filterPersonalContactsRetrieve) {
		this.filterPersonalContactsRetrieve = filterPersonalContactsRetrieve;
	}

	public boolean isFilterCorpContactsRetrieve() {
		return filterCorpContactsRetrieve;
	}

	public void setFilterCorpContactsRetrieve(boolean filterCorpContactsRetrieve) {
		this.filterCorpContactsRetrieve = filterCorpContactsRetrieve;
	}

	public String getFilterKeyword() {
		return filterKeyword;
	}

	public void setFilterKeyword(String filterKeyword) {
		this.filterKeyword = filterKeyword;
	}

	public String getFilterCountry() {
		return filterCountry;
	}

	public void setFilterCountry(String filterCountry) {
		this.filterCountry = filterCountry;
	}

	public String getFilterState() {
		return filterState;
	}

	public void setFilterState(String filterState) {
		this.filterState = filterState;
	}

	public String getFilterCity() {
		return filterCity;
	}

	public void setFilterCity(String filterCity) {
		this.filterCity = filterCity;
	}

	public SessionManagedBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionManagedBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public BusinessCard getDeleteSelectBusinessCard() {
		return deleteSelectBusinessCard;
	}

	public void setDeleteSelectBusinessCard(
			BusinessCard deleteSelectBusinessCard) {
		this.deleteSelectBusinessCard = deleteSelectBusinessCard;
	}

	public boolean isSwitchTableGridView() {
		return switchTableGridView;
	}

	public void setSwitchTableGridView(boolean switchTableGridView) {
		this.switchTableGridView = switchTableGridView;
	}

	public Boolean getDisapleBtns() {
		return disapleBtns;
	}

	public void setDisapleBtns(Boolean disapleBtns) {
		this.disapleBtns = disapleBtns;
	}

	public Boolean getEnabletoolTips() {
		return enabletoolTips;
	}

	public void setEnabletoolTips(Boolean enabletoolTips) {
		this.enabletoolTips = enabletoolTips;
	}

}
