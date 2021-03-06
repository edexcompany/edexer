package com.edexer.model;

// Generated May 24, 2015 10:17:18 AM by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Address generated by hbm2java
 */
@Entity
@Table(name = "address", catalog = "edexer_edexer")
public class Address implements java.io.Serializable {

	private int businessCardBusinessCardId;
	private BusinessCard businessCard;
	private Countries countries;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String zip;

	public Address() {
	}

	public Address(BusinessCard businessCard) {
		this.businessCard = businessCard;
	}

	public Address(BusinessCard businessCard, Countries countries,
			String street1, String street2, String city, String state,
			String zip) {
		this.businessCard = businessCard;
		this.countries = countries;
		this.street1 = street1;
		this.street2 = street2;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "businessCard"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "business_card_business_card_id", unique = true, nullable = false)
	public int getBusinessCardBusinessCardId() {
		return this.businessCardBusinessCardId;
	}

	public void setBusinessCardBusinessCardId(int businessCardBusinessCardId) {
		this.businessCardBusinessCardId = businessCardBusinessCardId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public BusinessCard getBusinessCard() {
		return this.businessCard;
	}

	public void setBusinessCard(BusinessCard businessCard) {
		this.businessCard = businessCard;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country")
	public Countries getCountries() {
		return this.countries;
	}

	public void setCountries(Countries countries) {
		this.countries = countries;
	}

	@Column(name = "street1", length = 80)
	public String getStreet1() {
		return this.street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	@Column(name = "street2", length = 80)
	public String getStreet2() {
		return this.street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	@Column(name = "city", length = 50)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "state", length = 50)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "zip", length = 20)
	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

}
