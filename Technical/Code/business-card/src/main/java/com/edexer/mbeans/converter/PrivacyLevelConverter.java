package com.edexer.mbeans.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.edexer.mbeans.UtilitiesManagesBean;
import com.edexer.model.PrivacyLevel;

@FacesConverter("privacyConverter")
public class PrivacyLevelConverter implements Converter{
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				UtilitiesManagesBean service = (UtilitiesManagesBean) fc
						.getExternalContext().getApplicationMap()
						.get("utilitiesManagesBean");
				PrivacyLevel c = service.getPrivacyLevel(Integer.parseInt(value));
				return c;
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid privacy Level."));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if (object != null) {
			return String.valueOf(((PrivacyLevel) object).getPrivacyId());
		} else {
			return "";
		}
	}
	
}
