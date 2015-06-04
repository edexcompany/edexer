package com.edexer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edexer.dao.ActAsEntityDaoImpl;
import com.edexer.dao.CountriesEntityDaoImpl;
import com.edexer.dao.RoleEntityDaoImpl;
import com.edexer.dao.SectorEntityDaoImpl;
import com.edexer.dao.SocialNetworksTypesEntityDaoImpl;
import com.edexer.dao.SubscriptionEntityDaoImpl;
import com.edexer.dao.SubscriptionStatusEntityDaoImpl;
import com.edexer.dao.TagsEntityDaoImpl;
import com.edexer.dao.UserStatusEntityDaoImpl;
import com.edexer.model.ActAs;
import com.edexer.model.Countries;
import com.edexer.model.Role;
import com.edexer.model.Sector;
import com.edexer.model.SocialNetworksTypes;
import com.edexer.model.Subscription;
import com.edexer.model.SubscriptionStatus;
import com.edexer.model.UserStatus;

@Service("lookupsServiceManager")
public class LookUpsServiceManagerImpl implements LookUpsServiceManager {

	@Autowired
	ActAsEntityDaoImpl actAsDao;

	@Autowired
	SubscriptionStatusEntityDaoImpl subscriptionStatusDao;

	@Autowired
	SubscriptionEntityDaoImpl subscriptionTypeDao;

	@Autowired
	CountriesEntityDaoImpl countriesDao;

	@Autowired
	RoleEntityDaoImpl roleDao;

	@Autowired
	SocialNetworksTypesEntityDaoImpl socialNetworksTypesDao;

	@Autowired
	SectorEntityDaoImpl sectorDao;

	@Autowired
	UserStatusEntityDaoImpl userStatusDao;

	@Override
	public List<ActAs> getActAsList() {
		return actAsDao.getActAsList();
	}

	public List<UserStatus> getUserStatusList() {
		return userStatusDao.getUserStatusList();
	}

	@Override
	public List<SubscriptionStatus> getSubscriptionStatusList() {
		return subscriptionStatusDao.getSubscriptionStatusList();
	}

	@Override
	public ActAs getActAsByName(String actAsName) {
		return actAsDao.getActAsByName(actAsName);
	}

	@Override
	public SubscriptionStatus getSubscriptionStatusByName(String statusName) {
		return subscriptionStatusDao.getSubscriptionStatusByName(statusName);
	}

	@Override
	public List<Countries> getCountriesList() {
		return countriesDao.getCountriesList();
	}

	@Override
	public List<Role> getRolesList() {
		return roleDao.getRolesList();
	}

	@Override
	public List<SocialNetworksTypes> getSocialNetworksTypes() {
		return socialNetworksTypesDao.getSocialNetworkTypesList();
	}

	@Override
	public List<Sector> getSectorList() {
		return sectorDao.getSectorList();
	}

	@Override
	public Countries getCountry(int idCountry) {
		return countriesDao.get(Countries.class, idCountry);
	}

	@Override
	public Role getRole(int roleId) {
		return roleDao.get(Role.class, roleId);
	}

	@Override
	public Countries getCountryByName(String countryName) {
		return countriesDao.getCountryByName(countryName);
	}

	@Override
	public Sector getSector(int sectorId) {
		return sectorDao.get(Sector.class, sectorId);
	}

	@Override
	public Subscription getSubscriptionType(int subscriptionTypeId) {
		return subscriptionTypeDao.get(Subscription.class, subscriptionTypeId);
	}

	@Override
	public List<Subscription> listSubscriptionTypes() {
		return subscriptionTypeDao.getSubscriptionsList();
	}

	@Override
	public SubscriptionStatus getSubscriptionStatus(int subscriptionStatusId) {
		return subscriptionStatusDao.get(SubscriptionStatus.class,
				subscriptionStatusId);
	}

}
