package com.olrox.ejb;

import com.olrox.domain.car.Model;

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

    public void persist(Model model){
        entityManager.persist(model);
    }

    public void remove(Model model){
        entityManager.remove(entityManager.contains(model) ? model : entityManager.merge(model));
    }

    public Model find(long id){
        return entityManager.find(Model.class, id);
    }

    public List<Model> getAll(){
        TypedQuery<Model> namedQuery = entityManager.createNamedQuery("Model.getAll", Model.class);
        return namedQuery.getResultList();
    }
}
