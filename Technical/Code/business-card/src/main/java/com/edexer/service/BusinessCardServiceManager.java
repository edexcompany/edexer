package com.edexer.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

import com.edexer.model.BusinessCard;
import com.edexer.model.User;

import java.io.File;

public interface BusinessCardServiceManager {
	public enum BusinessCardTypes {
		FindOn, Personal, Corporate
	}

	public Integer addBusinessCard(BusinessCard bc);

	public Integer addBusinessCard(BusinessCard bc, UploadedFile avatar)
			throws HibernateException, IOException;

	public Integer addBusinessCard(BusinessCard bc, UploadedFile avatar,List<UploadedFile> uploads)
			throws HibernateException, IOException;
	public void updateBusinessCard(BusinessCard bc, UploadedFile file, List<UploadedFile> uploads) throws HibernateException, IOException;

	public String validateBusinessCardDuplication(BusinessCard businessCard, boolean updateMode);

	public BusinessCard get(int bcId, boolean loadRelations);

	public List<BusinessCard> getBusinessCards(int count);

	public List<BusinessCard> advancedFilter(int first, int pageSize,
			int sortField, Map<String, Object> filters, boolean findOnContacts,
			boolean personalContacts, boolean corpContacts);

	public int getAdvancedFilterRowCount(int first, int pageSize,
			int sortField, Map<String, Object> filters, boolean findOnContacts,
			boolean personalContacts, boolean corpContacts);

	public BusinessCard getUserOwnCard(int userId);

	public boolean checkCardViewAuthority(User user, BusinessCard businessCard);

	public int uploadWorkbook(File file, String fileExt, User user,
			boolean isPersonal);

	public int addBusinessCardList(List<BusinessCard> list, User user,
			boolean isPersonal);

	public String validateBusinessCard(BusinessCard bc, boolean updateMode);

	public DefaultStreamedContent exportBusinessCards(
			List<BusinessCard> bcList, User user);

	public BusinessCardTypes getBusinessCardType(BusinessCard bc);
	public String deleteBusinessCard(BusinessCard bc);

	public void DeleteBusinessCard(int bcId);
}
