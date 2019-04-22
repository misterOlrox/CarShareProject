package com.olrox.account;

import com.olrox.account.domain.Credentials;
import com.olrox.account.domain.RentalUser;
import com.olrox.account.domain.Role;
import com.olrox.account.ejb.AuthorizationManager;
import com.olrox.exception.DuplicateCredentialsLoginException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class RegistrationBean implements Serializable {
    @EJB
    AuthorizationManager authorizationManager;

    @Inject
    CurrentSessionBean currentSessionBean;

    private RentalUser rentalUser;

    private Credentials credentials;

    @PostConstruct
    public void init(){
        rentalUser = new RentalUser();
        credentials = new Credentials();
    }

    public RentalUser getRentalUser() {
        return rentalUser;
    }

    public void setRentalUser(RentalUser rentalUser) {
        this.rentalUser = rentalUser;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void createAccount(){
        rentalUser.setRole(Role.USER);
        try {
            authorizationManager.signUp(credentials, rentalUser);
        } catch (DuplicateCredentialsLoginException e) {
            addError(e.getMessage());
            return;
        }
        addSuccessMessage();
        currentSessionBean.setCurrentUser(rentalUser);
        currentSessionBean.setLogin(credentials.getLogin());
        currentSessionBean.redirectToRequestedPage();
    }

    public void addSuccessMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Registration successful!", null));
    }

    public void addError(String error){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                error, null));
    }
}
