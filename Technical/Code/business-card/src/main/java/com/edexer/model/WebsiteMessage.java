package com.edexer.model;

// Generated May 3, 2015 11:11:40 PM by Hibernate Tools 3.6.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * WebsiteMessage generated by hbm2java
 */
@Entity
@Table(name = "website_message", catalog = "edexer")
public class WebsiteMessage implements java.io.Serializable {

	private WebsiteMessageId id;

	public WebsiteMessage() {
	}

	public WebsiteMessage(WebsiteMessageId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "keyName", column = @Column(name = "key_name", length = 200)),
			@AttributeOverride(name = "value", column = @Column(name = "value", length = 65535)) })
	public WebsiteMessageId getId() {
		return this.id;
	}

	public void setId(WebsiteMessageId id) {
		this.id = id;
	}

}
