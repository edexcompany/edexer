package com.edexer.model;

// Generated May 24, 2015 10:17:18 AM by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Title generated by hbm2java
 */
@Entity
@Table(name = "title", catalog = "edexer_edexer")
public class Title implements java.io.Serializable {

	private Integer titleId;
	private Sector sector;
	private BusinessCard businessCard;
	private String company;
	private String department;
	private String title;

	public Title() {
	}

	public Title(BusinessCard businessCard) {
		this.businessCard = businessCard;
	}

	public Title(Sector sector, BusinessCard businessCard, String company,
			String department, String title) {
		this.sector = sector;
		this.businessCard = businessCard;
		this.company = company;
		this.department = department;
		this.title = title;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "title_id", unique = true, nullable = false)
	public Integer getTitleId() {
		return this.titleId;
	}

	public void setTitleId(Integer titleId) {
		this.titleId = titleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sector_sector_id")
	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bc_id", nullable = false)
	public BusinessCard getBusinessCard() {
		return this.businessCard;
	}

	public void setBusinessCard(BusinessCard businessCard) {
		this.businessCard = businessCard;
	}

	@Column(name = "company", length = 100)
	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "department", length = 45)
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "title", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
