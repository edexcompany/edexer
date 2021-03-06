package com.edexer.model;

// Generated May 2, 2015 10:45:02 AM by Hibernate Tools 3.6.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SocialNetwork generated by hbm2java
 */
@Entity
@Table(name = "social_network", catalog = "edexer")
public class SocialNetwork implements java.io.Serializable {

	private SocialNetworkId id;
	private BusinessCard businessCard;
	private SocialNetworksTypes socialNetworksTypes;

	public SocialNetwork() {
	}

	public SocialNetwork(SocialNetworkId id, BusinessCard businessCard,
			SocialNetworksTypes socialNetworksTypes) {
		this.id = id;
		this.businessCard = businessCard;
		this.socialNetworksTypes = socialNetworksTypes;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "bcId", column = @Column(name = "bc_id", nullable = false)),
			@AttributeOverride(name = "identifier", column = @Column(name = "identifier", nullable = false, length = 50)) })
	public SocialNetworkId getId() {
		return this.id;
	}

	public void setId(SocialNetworkId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bc_id", nullable = false, insertable = false, updatable = false)
	public BusinessCard getBusinessCard() {
		return this.businessCard;
	}

	public void setBusinessCard(BusinessCard businessCard) {
		this.businessCard = businessCard;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type", nullable = false)
	public SocialNetworksTypes getSocialNetworksTypes() {
		return this.socialNetworksTypes;
	}

	public void setSocialNetworksTypes(SocialNetworksTypes socialNetworksTypes) {
		this.socialNetworksTypes = socialNetworksTypes;
	}

}
