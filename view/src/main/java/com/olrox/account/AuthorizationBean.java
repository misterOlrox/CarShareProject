package com.olrox.account;

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
public class AuthorizationBean implements Serializable {
    private Role role;

    private String login;
    private String password;

    private String requestedPage;

    @EJB
    private AuthorizationManager authorizationManager;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public void doLogin(){
        if(StringUtils.isEmpty(login) || StringUtils.isEmpty(password)){
            role = null;
            return;
        }

        role = authorizationManager.signIn(login, password);

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
        role = null;
        login = null;
        password = null;
        requestedPage = null;
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
}
