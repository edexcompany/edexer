package com.edexer.model;

// Generated May 24, 2015 10:17:18 AM by Hibernate Tools 3.6.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PasswordReset generated by hbm2java
 */
@Entity
@Table(name = "password_reset", catalog = "edexer_edexer")
public class PasswordReset implements java.io.Serializable {

	private Integer id;
	private User user;
	private String token;
	private Date creationTime;
	private String used;

	public PasswordReset() {
	}

	public PasswordReset(User user, Date creationTime) {
		this.user = user;
		this.creationTime = creationTime;
	}

	public PasswordReset(User user, String token, Date creationTime, String used) {
		this.user = user;
		this.token = token;
		this.creationTime = creationTime;
		this.used = used;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "token", length = 100)
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_time", nullable = false, length = 19)
	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Column(name = "used", length = 45)
	public String getUsed() {
		return this.used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

}
