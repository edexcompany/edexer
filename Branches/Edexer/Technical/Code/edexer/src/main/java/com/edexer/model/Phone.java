package com.edexer.model;

// Generated May 24, 2015 10:17:18 AM by Hibernate Tools 3.6.0

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
 * Phone generated by hbm2java
 */
@Entity
@Table(name = "phone", catalog = "edexer_edexer")
public class Phone implements java.io.Serializable {

	private PhoneId id;
	private BusinessCard businessCard;

	public Phone() {
	}

	public Phone(PhoneId id, BusinessCard businessCard) {
		this.id = id;
		this.businessCard = businessCard;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "bcId", column = @Column(name = "bc_id", nullable = false)),
			@AttributeOverride(name = "phoneNum", column = @Column(name = "phone_num", nullable = false, length = 20)) })
	public PhoneId getId() {
		return this.id;
	}

	public void setId(PhoneId id) {
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

}
