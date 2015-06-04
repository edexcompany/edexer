package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.dao.FiltersKeys;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;
import com.edexer.util.FiltersUtils;

@ManagedBean
@ViewScoped
public class BusinessCardLazyDataModel extends LazyDataModel<BusinessCard> {

	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubscriptionService;
	
	@ManagedProperty("#{sessionManagedBean}")
	private SessionManagedBean sessionBean;
	

	private List<BusinessCard> datasource;
	private Map<String, Object> filters;
	private int defaultPageSize = 10;
	ResourceBundle settingsBundle = ResourceBundle.getBundle("/settings");

	public BusinessCardLazyDataModel() {
		super();

	}

	@PostConstruct
	public void init() {
		// initialize filters
		// Get user
		User user = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);
		UserSubscription corpSubscription = userSubscriptionService
				.getUserSubscriptionByType(
						user.getUserSubscriptionsForUserId(), Integer
								.parseInt(settingsBundle
										.getString("SUBSCRIPTION_TYPE_CORP")));
		int ownerId = (corpSubscription == null ? 0 : corpSubscription
				.getUserBySubscriptionOwner().getUserId());

		filters = FiltersUtils.constructFilter(user.getUserId(), 7, "", "", "",
				"", "", "", "", "", FiltersKeys.ORDER_BY_ID_ASC, ownerId, sessionBean.getSearchKeyWord(),
				"", "", "");
		filters.put(FiltersKeys.FIND_ON_KEY, false);
		filters.put(FiltersKeys.PERSONAL_KEY, true);
		filters.put(FiltersKeys.CORP_KEY, true);

		// this.setRowCount(bcService.getAdvancedFilterRowCount(0,10,
		// (int)this.filters.get(FiltersKeys.ORDER_BY_KEY), this.filters));
		// setPageSize(defaultPageSize);
		// setDatasource(bcService.advancedFilter(0, defaultPageSize, 81,
		// this.filters));
	}

	@Override
	public BusinessCard getRowData(String rowKey) {
		for (BusinessCard bc : getDatasource()) {
			if (bc.getBusinessCardId().equals(Integer.valueOf(rowKey)))
				return bc;
		}
		return null;
	}

	@Override
	public Object getRowKey(BusinessCard businessCard) {
		return businessCard.getBusinessCardId();
	}

	@Override
	public List<BusinessCard> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {

		setPageSize(pageSize);
		List<BusinessCard> data = new ArrayList<BusinessCard>();
		data = bcService.advancedFilter(first, pageSize,
				(int) this.filters.get(FiltersKeys.ORDER_BY_KEY), this.filters,
				(boolean) this.filters.get(FiltersKeys.FIND_ON_KEY),
				(boolean) this.filters.get(FiltersKeys.PERSONAL_KEY),
				(boolean) this.filters.get(FiltersKeys.CORP_KEY));
		for (int i = 0; i < data.size(); i++) {
			data.set(i, bcService.get(data.get(i).getBusinessCardId(), true));
		}
		// filter
		// for (BusinessCard businessCard : datasource) {
		// boolean match = true;
		//
		// if (filters != null) {
		// for (Iterator<String> it = filters.keySet().iterator(); it
		// .hasNext();) {
		// try {
		// String filterProperty = it.next();
		// Object filterValue = filters.get(filterProperty);
		// String fieldValue = String.valueOf(businessCard.getClass()
		// .getField(filterProperty).get(businessCard));
		//
		// if (filterValue == null
		// || fieldValue
		// .startsWith(filterValue.toString())) {
		// match = true;
		// } else {
		// match = false;
		// break;
		// }
		// } catch (Exception e) {
		// match = false;
		// }
		// }
		// }
		//
		// if (match) {
		// data.add(businessCard);
		// }
		// }

		// sort
		// if (sortField != null) {
		// Collections.sort(data, new LazySorter(sortField, sortOrder));
		// }

		// rowCount
		// int dataSize = data.size();
		this.setRowCount(bcService.getAdvancedFilterRowCount(first, pageSize,
				(int) this.filters.get(FiltersKeys.ORDER_BY_KEY), this.filters,
				(boolean) this.filters.get(FiltersKeys.FIND_ON_KEY),
				(boolean) this.filters.get(FiltersKeys.PERSONAL_KEY),
				(boolean) this.filters.get(FiltersKeys.CORP_KEY)));

		// paginate
		// if (dataSize > pageSize) {
		// try {
		// return data.subList(first, first + pageSize);
		// } catch (IndexOutOfBoundsException e) {
		// return data.subList(first, first + (dataSize % pageSize));
		// }
		// } else {
		// return data;
		// }
		return data;
	}

	@Override
	public List<BusinessCard> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		throw new UnsupportedOperationException(
				"Lazy loading is not implemented.");
	}

	public BusinessCardServiceManager getBcService() {
		return bcService;
	}

	public void setBcService(BusinessCardServiceManager bcService) {
		this.bcService = bcService;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public UserSubscriptionServiceManager getUserSubscriptionService() {
		return userSubscriptionService;
	}

	public void setUserSubscriptionService(
			UserSubscriptionServiceManager userSubscriptionService) {
		this.userSubscriptionService = userSubscriptionService;
	}

	public List<BusinessCard> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<BusinessCard> datasource) {
		this.datasource = datasource;
	}

	public SessionManagedBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionManagedBean sessionBean) {
		this.sessionBean = sessionBean;
	}

}
