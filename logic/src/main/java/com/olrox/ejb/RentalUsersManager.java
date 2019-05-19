package com.olrox.ejb;

import com.olrox.domain.account.Credentials;
import com.olrox.domain.account.RentalUser;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class RentalUsersManager {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public RentalUser findByLogin(String login){
        Credentials credentials = entityManager.find(Credentials.class, login);
        return credentials.getRentalUser();
    }
}
