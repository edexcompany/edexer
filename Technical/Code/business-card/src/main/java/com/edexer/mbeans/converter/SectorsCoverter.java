package com.edexer.mbeans.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.edexer.mbeans.UtilitiesManagesBean;
import com.edexer.model.Sector;

@FacesConverter("sectorConverter")
public class SectorsCoverter implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				UtilitiesManagesBean service = (UtilitiesManagesBean) fc
						.getExternalContext().getApplicationMap()
						.get("utilitiesManagesBean");
				Sector c = service.getSectory(Integer.parseInt(value));
				return c;
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid sector."));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if (object != null) {
			return String.valueOf(((Sector) object).getSectorId());
		} else {
			return "";
		}
	}
}