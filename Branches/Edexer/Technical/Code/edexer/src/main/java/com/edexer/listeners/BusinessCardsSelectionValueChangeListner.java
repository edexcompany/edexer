package com.edexer.listeners;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

import com.edexer.model.BusinessCard;

public class BusinessCardsSelectionValueChangeListner implements
		ValueChangeListener {
	@Override
	public void processValueChange(ValueChangeEvent event)
			throws AbortProcessingException {
		// access country bean directly
//		BusinessCard
//		UserData userData = (UserData) FacesContext.getCurrentInstance()
//				.getExternalContext().getSessionMap().get("userData");
//		userData.setSelectedCountry(event.getNewValue().toString());
	}
}
