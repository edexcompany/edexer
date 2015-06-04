package com.edexer.service;

import java.util.List;

import com.edexer.model.BcPermissions;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;

public interface BcPermissionManager {

	public BcPermissions getPermission(int id);
	public List<User> getUsers();
	public void addPermission(List<BcPermissions> bcPermList);
	public String addPermission(BcPermissions bcPerm);
	public void deletePermission(List<BcPermissions> bcPermList);
	public String deletePermission(BcPermissions bcPerm);
	public List<User> getUserWithNoPermOnBC(List<BusinessCard> bcList);
	public List<User> getUserWithPermissionOnBC(List<BusinessCard> bcList);
	public BcPermissions getBcPermissionByBCANDUserSub(int bcid,int userid,int subtype);
}
