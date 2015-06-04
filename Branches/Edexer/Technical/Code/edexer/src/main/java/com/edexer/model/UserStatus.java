package com.edexer.model;

// Generated May 24, 2015 10:17:18 AM by Hibernate Tools 3.6.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * UserStatus generated by hbm2java
 */
@Entity
@Table(name = "user_status", catalog = "edexer_edexer")
public class UserStatus implements java.io.Serializable {

	private Integer userStatusId;
	private String status;
	private String statusDesc;
	private Set users = new HashSet(0);

	public UserStatus() {
	}

	public UserStatus(String status) {
		this.status = status;
	}

	public UserStatus(String status, String statusDesc, Set users) {
		this.status = status;
		this.statusDesc = statusDesc;
		this.users = users;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_status_id", unique = true, nullable = false)
	public Integer getUserStatusId() {
		return this.userStatusId;
	}

	public void setUserStatusId(Integer userStatusId) {
		this.userStatusId = userStatusId;
	}

	@Column(name = "status", nullable = false, length = 50)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "status_desc", length = 50)
	public String getStatusDesc() {
		return this.statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userStatus")
	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

}