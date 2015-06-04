package com.edexer.testmoilns;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class OuthomeText {

	private String message;
	public ResourceBundle lang = ResourceBundle.getBundle("lang");

	@PostConstruct
	public void init() {
		message = lang.getString("IMAGE_ONE");
	}

	public void getfirstmessage(int clintname) {
		if (clintname == 2) {
			message = lang.getString("IMAGE_TWO");
		}else if (clintname == 3){
			message = lang.getString("IMAGE_THREE");
		}else if (clintname == 4){
			message = lang.getString("IMAGE_FOUR");
		}else if (clintname == 5){
			message = lang.getString("IMAGE_FIVE");
		}else if (clintname == 6){
			message = lang.getString("IMAGE_SEX");
		}else if (clintname == 1){
			message = lang.getString("IMAGE_ONE");
		}
	}

	public boolean beas(boolean st) {
		return st;
	}

	public String getmessage() {
		return message;
	}

	public void setmessage(String message) {
		this.message = message;
	}

}
