package com.edexer.model;

// Generated May 24, 2015 10:17:18 AM by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SocialNetworkId generated by hbm2java
 */
@Embeddable
public class SocialNetworkId implements java.io.Serializable {

	private int bcId;
	private String identifier;

	public SocialNetworkId() {
	}

	public SocialNetworkId(int bcId, String identifier) {
		this.bcId = bcId;
		this.identifier = identifier;
	}

	@Column(name = "bc_id", nullable = false)
	public int getBcId() {
		return this.bcId;
	}

	public void setBcId(int bcId) {
		this.bcId = bcId;
	}

	@Column(name = "identifier", nullable = false, length = 50)
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SocialNetworkId))
			return false;
		SocialNetworkId castOther = (SocialNetworkId) other;

		return (this.getBcId() == castOther.getBcId())
				&& ((this.getIdentifier() == castOther.getIdentifier()) || (this
						.getIdentifier() != null
						&& castOther.getIdentifier() != null && this
						.getIdentifier().equals(castOther.getIdentifier())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getBcId();
		result = 37
				* result
				+ (getIdentifier() == null ? 0 : this.getIdentifier()
						.hashCode());
		return result;
	}

}
