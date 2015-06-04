package com.edexer.mbeans.staffmbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;

import org.primefaces.event.RowEditEvent;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.mbeans.Constants;
import com.edexer.model.Role;
import com.edexer.model.User;
import com.edexer.service.MailService;
import com.edexer.service.UserServiceManager;
import com.edexer.util.PasswordHash;
import com.edexer.util.TokenGenerator;
import com.edexer.util.UserUtil;

@ManagedBean
@ViewScoped
public class StaffUsersManagedBean implements Serializable {

	@ManagedProperty("#{userService}")
	private UserServiceManager userService;

	@ManagedProperty("#{mailService}")
	private MailService mailService;

	private List<User> usersList;
	private List<User> filteredUsersList;
	private User newUser;

	@PostConstruct
	void init() {
		getAllUsers();
		// filteredUsersList = new ArrayList<User>();
	}
	public void getAllUsers()
	{
		usersList = userService.getAllUsers();
		newUser = new User();
		newUser.setRole(new Role());
	}

	public void deleteUser(Integer id) {
		userService.delete(userService.getUserById(id));
		FacesMessage msg = new FacesMessage("User Edited", id + " deleted");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String addUser() {
		System.out.println("adding user");

		newUser.setPassword(TokenGenerator.generatePassword());
		int error = 0;
		try {
			String userEmail = newUser.getUserEmail();
			if (!userService.isUserExists(userEmail)) {
				userService.insertUser(newUser);
				error = 1;
				sendEmail();
				FacesMessage msg = new FacesMessage("User added");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return Constants.INDEX;
			} else {
				FacesMessage msg = new FacesMessage("this email is already exist !! write another email");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = null;
			if (error == 0)
				msg = new FacesMessage("Error occured while adding new user");
			else if (error == 1)
				msg = new FacesMessage(
						"User added but error occured while sending email");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return Constants.ERROR;
		}

	}

	private void sendEmail() throws Exception {
		String to = newUser.getUserEmail();
		String title = "FindOn account";
		String body = "Dear " + newUser.getFirstName()
				+ "/n/nYou have new account on FindOn.com, your password: "
				+ newUser.getPassword();
		getMailService().sendMail(null, newUser.getUserEmail(), "", body);
	}

	public void onRowEdit(RowEditEvent event) {
		User editedUser = (User) event.getObject();
		User dbUser = userService.getUser(editedUser.getUserEmail());
		boolean isPasswordChanged = !editedUser.getPassword().equals(
				dbUser.getPassword());
		if (isPasswordChanged) {
			try {
				String pass = PasswordHash.createHash(editedUser.getPassword());
				String[] params = pass.split(":");
				editedUser.setPassword(params[PasswordHash.PBKDF2_INDEX]);
				editedUser.setSalt(params[PasswordHash.SALT_INDEX]);
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.addErrorMessage("Error", "Error editing password");
				return;
			}
		}
		userService.update(editedUser);
		FacesMessage msg = new FacesMessage("User Edited",
				((User) event.getObject()).getFirstName() + " "
						+ ((User) event.getObject()).getLastName() + " updated");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled",
				((User) event.getObject()).getFirstName() + " "
						+ ((User) event.getObject()).getLastName()
						+ " not updated");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public List<User> getUsersList() {
		getAllUsers();
		return usersList;
	}

	public void setUsersList(List<User> usersList) {
		getAllUsers();
		this.usersList = usersList;
		
	}

	public UserServiceManager getUserService() {
		return userService;
	}

	public void setUserService(UserServiceManager userService) {
		this.userService = userService;
	}

	public List<User> getFilteredUsersList() {
		return filteredUsersList;
	}

	public void setFilteredUsersList(List<User> filteredUsersList) {
		this.filteredUsersList = filteredUsersList;
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
}
