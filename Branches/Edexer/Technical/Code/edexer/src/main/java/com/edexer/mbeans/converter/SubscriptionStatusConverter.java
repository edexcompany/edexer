package com.edexer.mbeans.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.edexer.mbeans.UtilitiesManagesBean;
import com.edexer.model.Role;
import com.edexer.model.SubscriptionStatus;

@FacesConverter("subscriptionStatusConverter")
public class SubscriptionStatusConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				UtilitiesManagesBean service = (UtilitiesManagesBean) context
						.getExternalContext().getApplicationMap()
						.get("utilitiesManagesBean");
				SubscriptionStatus c = service.getSubscriptionStatus(Integer
						.parseInt(value));
				return c;
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid subscription status."));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			return String
					.valueOf(((SubscriptionStatus) value).getSubStatusId());
		} else {
			return "";
		}
	}

}
