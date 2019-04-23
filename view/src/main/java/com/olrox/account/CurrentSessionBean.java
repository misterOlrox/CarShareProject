package com.olrox.account;

import com.olrox.account.domain.RentalUser;
import com.olrox.account.domain.Role;
import com.olrox.account.ejb.AuthorizationManager;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class CurrentSessionBean implements Serializable {
    private String login;
    private String password;
    private RentalUser currentUser;

    private String requestedPage;

    @EJB
    private AuthorizationManager authorizationManager;

    public void doLogin(){
        if(StringUtils.isEmpty(login) || StringUtils.isEmpty(password)){
            currentUser = null;
            return;
        }

        currentUser = authorizationManager.signIn(login, password);

        redirectToRequestedPage();
    }

    public void redirectToRequestedPage(){
        Role role = getRole();
        if(role == null){
            addError();
        }
        else{
            try {
                if (requestedPage == null){
                    FacesContext.getCurrentInstance().getExternalContext().redirect(role.toString() + "/hello.xhtml");
                }
                else{
                    FacesContext.getCurrentInstance().getExternalContext().redirect(requestedPage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doLogout(){
        login = null;
        password = null;
        requestedPage = null;
        currentUser = null;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/view/index.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addError() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Login failed: Username or password is incorrect.", null));
    }

    public Role getRole() {
        if(currentUser == null){
            return null;
        }
        else {
            return currentUser.getRole();
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRequestedPage() {
        return requestedPage;
    }

    public void setRequestedPage(String requestedPage) {
        this.requestedPage = requestedPage;
    }

    public RentalUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(RentalUser currentUser) {
        this.currentUser = currentUser;
    }
}
