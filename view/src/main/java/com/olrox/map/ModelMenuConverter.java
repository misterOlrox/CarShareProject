package com.olrox.map;

import com.olrox.admin.ModelCollectorView;
import com.olrox.car.domain.Model;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.io.Serializable;

@SessionScoped
@FacesConverter(value="ModelMenuConverter")
public class ModelMenuConverter implements Converter, Serializable {
    @Inject
    ModelCollectorView modelCollectorView;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if(StringUtils.isEmpty(value)) {
            return null;
        }
        else {
            long id = Long.parseLong(value);
            for(Model model : modelCollectorView.getModels()){
                if(model.getId()==id)
                    return model;
            }
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((Model) value).getId());
        }
    }
}
