package com.olrox.account.ejb;


import com.olrox.account.domain.Credentials;
import com.olrox.account.domain.RentalUser;
import com.olrox.account.domain.Role;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class AuthorizationManager {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public Role signIn(String name, String password){
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(password)){
            return null;
        }

        Credentials credentials = entityManager.find(Credentials.class, name);
        if(credentials == null){
            return null;
        }

        if(!password.equals(credentials.getPassword())){
            return null;
        }

        RentalUser rentalUser = credentials.getRentalUser();
        if(rentalUser == null){
            return null;
        }

        return rentalUser.getRole();
    }

    public void signUp(Credentials credentials, RentalUser rentalUser){
        rentalUser.setCredentials(credentials);
        entityManager.persist(credentials);
        entityManager.persist(rentalUser);
    }
}
