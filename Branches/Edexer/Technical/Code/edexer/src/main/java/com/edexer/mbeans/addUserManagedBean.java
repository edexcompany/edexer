package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import com.edexer.model.Countries;
import com.edexer.model.Role;
import com.edexer.model.User;
import com.edexer.model.UserStatus;
import com.edexer.service.LookUpsServiceManager;
import com.edexer.service.UserServiceManager;

@ManagedBean
@SessionScoped
public class addUserManagedBean {
	@ManagedProperty("#{userService}")
	private UserServiceManager userService;
	@ManagedProperty("#{lookupsServiceManager}")
	private LookUpsServiceManager lookUpsService;

	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String mobile;
	private String phone;
	private String title;

	private UserStatus status;
	private Role role;
	private Role role2;
	
	private Map<String, String> statuses;
	private Map<String, String> roles;
	private List<SelectItem> statusList = new ArrayList<SelectItem>();

	@PostConstruct
	public void init() {
		statuses = new HashMap<String, String>();
		List<UserStatus> list = getLookUpsService().getUserStatusList();
		if (list.size() > 0) {
			for (UserStatus userStatus : list) {
				statuses.put(userStatus.getStatus(), userStatus
						.getUserStatusId().toString());
			}
		}
		roles = new HashMap<String, String>();
		List<Role> listRole = getLookUpsService().getRolesList();
		if (listRole.size() > 0) {
			for (Role role : listRole) {
				roles.put(role.getRoleName(), role.getRoleId().toString());
			}
		}
	}

	public Map<String, String> getStatuses() {
		return statuses;
	}

	public void setStatuses(Map<String, String> statuses) {
		this.statuses = statuses;
	}

	public void submit() {
		try {
			User user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPassword(password);
			user.setUserStatus(status);

			user.setRole(role);
			getUserService().insertUser(user);
			System.out.println("Suucessful");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<Role> getRoleList() {
		try {
			List<Role> list = getLookUpsService().getRolesList();
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public List<UserStatus> getUserStatusList() {
		try {
			List<UserStatus> list = getLookUpsService().getUserStatusList();
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public void clearFields() {
		firstName = "";
		lastName = "";
		password = "";
		email = "";
		role = new Role();
		status = new UserStatus();
	}

	public LookUpsServiceManager getLookUpsService() {
		return lookUpsService;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setLookUpsService(LookUpsServiceManager lookUpsService) {
		this.lookUpsService = lookUpsService;
	}

	public UserServiceManager getUserService() {
		return userService;
	}

	public void setUserService(UserServiceManager userService) {
		this.userService = userService;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, String> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, String> roles) {
		this.roles = roles;
	}

	public List<SelectItem> getStatusList() {
		List<UserStatus> list = new ArrayList<UserStatus>();
		list = getLookUpsService().getUserStatusList();
		for (UserStatus c : list) {
			statusList.add(new SelectItem(c.getUserStatusId(), c.getStatus()));
		}
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public Role getRole2() {
		return role2;
	}

	public void setRole2(Role role2) {
		this.role2 = role2;
	}

}
