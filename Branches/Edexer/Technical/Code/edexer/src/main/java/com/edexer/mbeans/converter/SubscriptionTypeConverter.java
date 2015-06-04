package com.edexer.mbeans.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.edexer.mbeans.UtilitiesManagesBean;
import com.edexer.model.Subscription;

@FacesConverter("subscriptionTypeConverter")
public class SubscriptionTypeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				UtilitiesManagesBean service = (UtilitiesManagesBean) context
						.getExternalContext().getApplicationMap()
						.get("utilitiesManagesBean");
				Subscription c = service.getSubscriptionType(Integer
						.parseInt(value));
				return c;
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid role."));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) {
		if (object != null) {
			return String.valueOf(((Subscription) object).getSubscriptionId());
		} else {
			return "";
		}
	}

}
