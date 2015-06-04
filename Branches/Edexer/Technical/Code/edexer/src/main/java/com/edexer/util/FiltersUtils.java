package com.edexer.util;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.edexer.dao.FiltersKeys;

public class FiltersUtils {
	public static Map<String, Object> constructFilter(int userId, int subId,
			String fName, String lName, String tags, String title,
			String company, String mail, String sector, String mobile,
			int orderBy, int owner, String keyword, String country,
			String state, String city) {
		Map<String, Object> filters = new HashedMap();
		filters.put(FiltersKeys.FIRST_NAME_KEY, fName);
		filters.put(FiltersKeys.LAST_NAME_KEY, lName);
		filters.put(FiltersKeys.TAGS_KEY, tags);
		filters.put(FiltersKeys.TITLE_KEY, title);
		filters.put(FiltersKeys.SUB_TYPE_KEY, subId);
		filters.put(FiltersKeys.USER_ID_KEY, userId);
		filters.put(FiltersKeys.COMPANY_KEY, company);
		filters.put(FiltersKeys.EMAIL_KEY, mail);
		filters.put(FiltersKeys.MOBILE_KEY, mobile);
		filters.put(FiltersKeys.SECTOR_KEY, sector);
		filters.put(FiltersKeys.ORDER_BY_KEY, orderBy);
		filters.put(FiltersKeys.OWNER_ID_KEY, owner);
		filters.put(FiltersKeys.KEYWORD_KEY, keyword);
		filters.put(FiltersKeys.COUNTRY_KEY, country);
		filters.put(FiltersKeys.STATE_KEY, state);
		filters.put(FiltersKeys.CITY_KEY, city);
		return filters;
	}
}
