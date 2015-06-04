package com.edexer.model;

// Generated May 2, 2015 10:45:02 AM by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EmailId generated by hbm2java
 */
@Embeddable
public class EmailId implements java.io.Serializable {

	private int bcId;
	private String emailAddress;

	public EmailId() {
	}

	public EmailId(int bcId, String emailAddress) {
		this.bcId = bcId;
		this.emailAddress = emailAddress;
	}

	@Column(name = "bc_id", nullable = false)
	public int getBcId() {
		return this.bcId;
	}

	public void setBcId(int bcId) {
		this.bcId = bcId;
	}

	@Column(name = "email_address", nullable = false, length = 150)
	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EmailId))
			return false;
		EmailId castOther = (EmailId) other;

		return (this.getBcId() == castOther.getBcId())
				&& ((this.getEmailAddress() == castOther.getEmailAddress()) || (this
						.getEmailAddress() != null
						&& castOther.getEmailAddress() != null && this
						.getEmailAddress().equals(castOther.getEmailAddress())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getBcId();
		result = 37
				* result
				+ (getEmailAddress() == null ? 0 : this.getEmailAddress()
						.hashCode());
		return result;
	}

}
