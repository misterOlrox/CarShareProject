package com.olrox.authorization.ejb;


import com.olrox.authorization.domain.Credentials;
import com.olrox.authorization.domain.RentalUser;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class AuthenticationManager {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public boolean login(String name, String password){
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(password)){
            return false;
        }

        Credentials credentials = entityManager.find(Credentials.class, name);
        if(credentials == null){
            return false;
        }

        if(!password.equals(credentials.getPassword())){
            return false;
        }

        RentalUser rentalUser = credentials.getRentalUser();
        if(rentalUser == null){
            return false;
        }

        return true;
    }
}
