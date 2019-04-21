package com.olrox.car.ejb;

import com.olrox.car.domain.Model;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class ModelsManager {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public Model create(String manufacturer, String modelName, int pricePerMinute, byte[] image){
        Model model = new Model();
        model.setManufacturer(manufacturer);
        model.setModelName(modelName);
        model.setPricePerMinute(pricePerMinute);
        model.setImage(image);

        entityManager.persist(model);
        return model;
    }

    public void persist(Model model){
        entityManager.persist(model);
    }

    public void remove(Model model){
        //entityManager.remove(model);
        entityManager.remove(entityManager.contains(model) ? model : entityManager.merge(model));
    }

    public byte[] getImage(long id){
        Model model = entityManager.find(Model.class, id);
        return model.getImage();
    }

    public Model getModel(long id){
        return entityManager.find(Model.class, id);
    }

    public List<Model> getAll(){
        TypedQuery<Model> namedQuery = entityManager.createNamedQuery("Model.getAll", Model.class);
        return namedQuery.getResultList();
    }
}
