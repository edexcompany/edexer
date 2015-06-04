package com.edexer.service;

import java.util.List;

import com.edexer.model.ActAs;
import com.edexer.model.Countries;
import com.edexer.model.Role;
import com.edexer.model.Sector;
import com.edexer.model.SocialNetworksTypes;
import com.edexer.model.Subscription;
import com.edexer.model.SubscriptionStatus;
import com.edexer.model.UserStatus;

public interface LookUpsServiceManager {
	public List<ActAs> getActAsList();
	public List<UserStatus> getUserStatusList() ;
	public List<SubscriptionStatus> getSubscriptionStatusList();

	public ActAs getActAsByName(String actAsName);

	public SubscriptionStatus getSubscriptionStatusByName(String statusName);

	public List<Countries> getCountriesList();

	public List<SocialNetworksTypes> getSocialNetworksTypes();

	public List<Sector> getSectorList();

	public Countries getCountry(int idCountry);

	public Role getRole(int roleId);

	public Countries getCountryByName(String countryName);

	public Sector getSector(int sectorId);

	public List<Role> getRolesList();

	public Subscription getSubscriptionType(int subscriptionTypeId);

	public SubscriptionStatus getSubscriptionStatus(int subscriptionStatusId);

	public List<Subscription> listSubscriptionTypes();

}
