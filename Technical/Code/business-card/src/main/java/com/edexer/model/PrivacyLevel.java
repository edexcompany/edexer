package com.edexer.model;

// Generated Apr 19, 2015 8:37:20 PM by Hibernate Tools 3.6.0

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
 * PrivacyLevel generated by hbm2java
 */
@Entity
@Table(name = "privacy_level", catalog = "edexer")
public class PrivacyLevel implements java.io.Serializable {

	private Integer privacyId;
	private String privacyName;
	private String desc;
	private Set users = new HashSet(0);

	public PrivacyLevel() {
	}

	public PrivacyLevel(String privacyName, String desc, Set users) {
		this.privacyName = privacyName;
		this.desc = desc;
		this.users = users;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "privacy_id", unique = true, nullable = false)
	public Integer getPrivacyId() {
		return this.privacyId;
	}

	public void setPrivacyId(Integer privacyId) {
		this.privacyId = privacyId;
	}

	@Column(name = "privacy_name", length = 50)
	public String getPrivacyName() {
		return this.privacyName;
	}

	public void setPrivacyName(String privacyName) {
		this.privacyName = privacyName;
	}

	@Column(name = "desc", length = 50)
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "privacyLevel")
	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

}
