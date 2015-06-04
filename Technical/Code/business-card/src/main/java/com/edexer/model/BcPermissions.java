package com.edexer.model;

// Generated May 2, 2015 10:45:02 AM by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * BcPermissions generated by hbm2java
 */
@Entity
@Table(name = "bc_permissions", catalog = "edexer")
public class BcPermissions implements java.io.Serializable {

	private Integer bcPermissionsId;
	private BusinessCard businessCard;
	private UserSubscription userSubscription;

	public BcPermissions() {
	}

	public BcPermissions(BusinessCard businessCard,
			UserSubscription userSubscription) {
		this.businessCard = businessCard;
		this.userSubscription = userSubscription;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "bc_permissions_id", unique = true, nullable = false)
	public Integer getBcPermissionsId() {
		return this.bcPermissionsId;
	}

	public void setBcPermissionsId(Integer bcPermissionsId) {
		this.bcPermissionsId = bcPermissionsId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bc_id", nullable = false)
	public BusinessCard getBusinessCard() {
		return this.businessCard;
	}

	public void setBusinessCard(BusinessCard businessCard) {
		this.businessCard = businessCard;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "user_subscription_user_id", referencedColumnName = "user_id", nullable = false),
			@JoinColumn(name = "user_subscription_sub_type", referencedColumnName = "sub_type", nullable = false) })
	public UserSubscription getUserSubscription() {
		return this.userSubscription;
	}

	public void setUserSubscription(UserSubscription userSubscription) {
		this.userSubscription = userSubscription;
	}

}
