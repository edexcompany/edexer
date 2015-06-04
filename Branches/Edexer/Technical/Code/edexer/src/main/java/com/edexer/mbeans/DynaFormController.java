package com.edexer.mbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;



import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.extensions.model.dynaform.*;


@ManagedBean  
@ViewScoped  
public class DynaFormController implements Serializable {  
  
    private static final long serialVersionUID = 20120423L;  
  
    private DynaFormModel model;  
  
    private static List<SelectItem> LANGUAGES = new ArrayList<SelectItem>();  
  
    @PostConstruct  
    protected void initialize() {  
        model = new DynaFormModel();  
  
        // add rows, labels and editable controls  
        // set relationship between label and editable controls to support outputLabel with "for" attribute  
  
        // 1. row  
        DynaFormRow row = model.createRegularRow();  
  
        DynaFormLabel label11 = row.addLabel("Author");  
        DynaFormControl control12 = row.addControl(new DynaFormProperty("Author", true), "input");  
        label11.setForControl(control12);  
  
        DynaFormLabel label13 = row.addLabel("ISBN");  
        DynaFormControl control14 = row.addControl(new DynaFormProperty("ISBN", true), "input");  
        
        label13.setForControl(control14);  
  
        // 2. row  
        row = model.createRegularRow();  
  
        DynaFormLabel label21 = row.addLabel("Title");  
        DynaFormControl control22 = row.addControl(new DynaFormProperty("Title", false), "input", 3, 1);  
        label21.setForControl(control22);  
  
        // 3. row  
        row = model.createRegularRow();  
  
        DynaFormLabel label31 = row.addLabel("Publisher");  
        DynaFormControl control32 = row.addControl(new DynaFormProperty("Publisher", false), "input");  
        label31.setForControl(control32);  
  
        DynaFormLabel label33 = row.addLabel("Published on");  
        DynaFormControl control34 = row.addControl(new DynaFormProperty("Published on", false), "calendar");  
        label33.setForControl(control34);  
  
        // 4. row  
        row = model.createRegularRow();  
  
        DynaFormLabel label41 = row.addLabel("Language");  
        DynaFormControl control42 = row.addControl(new DynaFormProperty("Language", false), "select");  
        label41.setForControl(control42);  
  
        DynaFormLabel label43 = row.addLabel("Description", 1, 2);  
        DynaFormControl control44 = row.addControl(new DynaFormProperty("Description", false), "textarea", 1, 2);  
        label43.setForControl(control44);  
  
        // 5. row  
        row = model.createRegularRow();  
  
        DynaFormLabel label51 = row.addLabel("Rating");  
        DynaFormControl control52 = row.addControl(new DynaFormProperty("Rating", 3, true), "rating");  
        label51.setForControl(control52);  
    }  
  
    public DynaFormModel getModel() {  
        return model;  
    }  
  
    public List<DynaFormProperty> getBookProperties() {  
        if (model == null) {  
            return null;  
        }  
  
        List<DynaFormProperty> bookProperties = new ArrayList<DynaFormProperty>();  
        for (DynaFormControl dynaFormControl : model.getControls()) {  
            bookProperties.add((DynaFormProperty) dynaFormControl.getData());  
        }  
  
        return bookProperties;  
    }  
  
    public String submitForm() {  
        FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();  
        boolean hasErrors = (sev != null && (FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0));  
  
        RequestContext requestContext = RequestContext.getCurrentInstance();  
        requestContext.addCallbackParam("isValid", !hasErrors);  
  
        return null;  
    }  
  
    public List<SelectItem> getLanguages() {  
        if (LANGUAGES.isEmpty()) {  
            LANGUAGES.add(new SelectItem("en", "English"));  
            LANGUAGES.add(new SelectItem("de", "German"));  
            LANGUAGES.add(new SelectItem("ru", "Russian"));  
            LANGUAGES.add(new SelectItem("tr", "Turkish"));  
        }  
  
        return LANGUAGES;  
    }  
}  


     