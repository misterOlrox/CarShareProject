package com.olrox.admin;

import com.olrox.car.domain.Model;
import com.olrox.car.ejb.ModelsManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class ModelCollectorView implements Serializable {
    @EJB
    private ModelsManager modelsManager;

    private Model model;

    private List<Model> models;

    @PostConstruct
    public void init() {
        model = new Model();
        models = modelsManager.getAll();
    }

    public void createNew() {
        models.add(model);
        model = new Model();
    }

    public String reinit() {
        if(model.getImage() == null){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No image file", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        modelsManager.persist(model);
        model = new Model();
        return null;
    }

    public Model getModel() {
        return model;
    }

    public List<Model> getModels() {
        return models;
    }

    public String delete(Model model){
        modelsManager.remove(model);
        return null;
    }
}
