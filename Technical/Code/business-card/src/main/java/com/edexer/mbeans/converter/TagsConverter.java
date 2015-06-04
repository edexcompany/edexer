package com.edexer.mbeans.converter;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;

import com.edexer.dao.TagsEntityDaoImpl;
import com.edexer.mbeans.TagsManagedBean;
import com.edexer.model.Tags;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.TagsServiceManager;
import com.edexer.service.TagsServiceManagerImpl;

@FacesConverter("tagConverter")
public class TagsConverter implements Converter{
	
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
            	TagsManagedBean service = (TagsManagedBean) fc.getExternalContext().getApplicationMap().get("tagsManagedBean");
                return service.getTag(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid tag."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
        	Tags t =(Tags)object;
            return String.valueOf(((Tags)object).getTagId());
        }
        else {
            return "";
        }
    }

}
