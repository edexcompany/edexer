package com.edexer.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edexer.dao.BcPermissionsEntityDaoImpl;
import com.edexer.dao.UserEntityDaoImpl;
import com.edexer.model.BcPermissions;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;

@Service("bcPermissionServiceManager")
public class BcPermissionManagerImpl implements BcPermissionManager {

	@Autowired
	BcPermissionsEntityDaoImpl bcPermissionsDao;

	@Override
	public BcPermissions getPermission(int id) {
		return bcPermissionsDao.get(id, true);
	}
	@Autowired
	UserEntityDaoImpl userDao;

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		List<User> userList = new ArrayList<User>();
		try {
			userList = userDao.getAllUserList();
			return userList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	@Override
	public void addPermission(List<BcPermissions> bcPermList) {
		// TODO Auto-generated method stub
		try {
			if (bcPermList != null) {
				for (BcPermissions bcPerm : bcPermList) {
					int bcid = bcPerm.getBusinessCard().getBusinessCardId();
					int userid = bcPerm.getUserSubscription().getId()
							.getUserId();
					int subtype = bcPerm.getUserSubscription().getId()
							.getSubType();
					if (!bcPermissionsDao.chkIfPermIsExistOrNot(bcid, userid,
							subtype)) {
						bcPermissionsDao.add(bcPerm);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String addPermission(BcPermissions bcPerm) {
		// TODO Auto-generated method stub
		try {
			if (bcPerm != null) {
				int bcid = bcPerm.getBusinessCard().getBusinessCardId();
				int userid = bcPerm.getUserSubscription().getId().getUserId();
				int subtype = bcPerm.getUserSubscription().getId().getSubType();
				if (!bcPermissionsDao.chkIfPermIsExistOrNot(bcid, userid,
						subtype)) {
					bcPermissionsDao.add(bcPerm);
				}
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	@Override
	public void deletePermission(List<BcPermissions> bcPermList) {
		// TODO Auto-generated method stub
		try {
			if (bcPermList != null) {
				for (BcPermissions bcPerm : bcPermList) {
					BcPermissions permission = new BcPermissions();
					if (bcPerm != null)
						permission = getBcPermissionByBCANDUserSub(bcPerm
								.getBusinessCard().getBusinessCardId(), bcPerm
								.getUserSubscription().getId().getUserId(),
								bcPerm.getUserSubscription().getId()
										.getSubType());
					if (permission != null)
						bcPermissionsDao.delete(permission);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String deletePermission(BcPermissions bcPerm) {
		// TODO Auto-generated method stub
		try {
			if (bcPerm != null) {
				BcPermissions permission = new BcPermissions();
				permission = getBcPermissionByBCANDUserSub(bcPerm
						.getBusinessCard().getBusinessCardId(), bcPerm
						.getUserSubscription().getId().getUserId(), bcPerm
						.getUserSubscription().getId().getSubType());
				if (permission != null)
					bcPermissionsDao.delete(permission);

			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	@Override
	public List<User> getUserWithNoPermOnBC(List<BusinessCard> bcList) {
		// TODO Auto-generated method stub
		List<User> finalUsers = new ArrayList<User>();
		try {
			for (BusinessCard bc : bcList) {
				List<UserSubscription> subList = bcPermissionsDao
						.getUsersHasNoPermForThisBC(bc.getBusinessCardId());
				if (subList != null && subList.size() > 0) {
					for (UserSubscription sub : subList) {
						User user = userDao.get(User.class, sub.getId()
								.getUserId());
						if (!finalUsers.contains(user)) {
							finalUsers.add(user);
						}
					}
				}
			}
			return finalUsers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<User> getUserWithPermissionOnBC(List<BusinessCard> bcList) {
		List<User> finalUsers = new ArrayList<User>();
		try {
			for (BusinessCard bc : bcList) {
				List<UserSubscription> subList = bcPermissionsDao
						.getUsersHasPermForThisBC(bc.getBusinessCardId());
				if (subList != null && subList.size() > 0) {
					for (UserSubscription sub : subList) {
						User user = userDao.get(User.class, sub.getId()
								.getUserId());
						if (!finalUsers.contains(user)) {
							finalUsers.add(user);
						}
					}
				}
			}
			return finalUsers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	@Override
	public BcPermissions getBcPermissionByBCANDUserSub(int bcid, int userid,
			int subtype) {
		// TODO Auto-generated method stub
		try {
			BcPermissions perm = bcPermissionsDao.getPermByBCANDUserSub(bcid,
					userid, subtype);
			return perm;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
