package com.edexer.model;

import java.math.BigInteger;

import org.hibernate.annotations.Entity;

@Entity
public class ReportResult {
	private int id;
	private String name;
	private BigInteger count;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}
}
