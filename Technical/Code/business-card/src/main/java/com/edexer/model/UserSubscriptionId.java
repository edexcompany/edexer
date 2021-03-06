package com.edexer.model;

// Generated May 2, 2015 10:45:02 AM by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * UserSubscriptionId generated by hbm2java
 */
@Embeddable
public class UserSubscriptionId implements java.io.Serializable {

	private int userId;
	private int subType;

	public UserSubscriptionId() {
	}

	public UserSubscriptionId(int userId, int subType) {
		this.userId = userId;
		this.subType = subType;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "sub_type", nullable = false)
	public int getSubType() {
		return this.subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserSubscriptionId))
			return false;
		UserSubscriptionId castOther = (UserSubscriptionId) other;

		return (this.getUserId() == castOther.getUserId())
				&& (this.getSubType() == castOther.getSubType());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserId();
		result = 37 * result + this.getSubType();
		return result;
	}

}
