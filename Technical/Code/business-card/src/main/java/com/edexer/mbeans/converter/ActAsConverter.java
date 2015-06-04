package com.edexer.mbeans.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.edexer.mbeans.ActAsManagedBean;
import com.edexer.model.ActAs;

@FacesConverter("actAsConverter")
public class ActAsConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if(value != null && value.trim().length() > 0) {
			try{
				ActAsManagedBean actAsMb = (ActAsManagedBean) context.getExternalContext().getApplicationMap().get("actAsBean");
				return actAsMb.getActAsList().get(Integer.parseInt(value)-1);
			} catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Object."));
            }
		}
		else{
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		 if(value != null) {
	        	ActAs actAs =(ActAs)value;
	            return String.valueOf(((ActAs)value).getActAsId());
	        }
	        else {
	            return "";
	        }
	}

}
