package com.olrox.appearance.topbar;

import com.olrox.login.AuthorizationBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class MyAccount implements Serializable {
    @Inject
    private AuthorizationBean authorizationBean;

    public String getAccountName(){
        if(authorizationBean.getRole() == null){
            return "Log in";
        }
        else {
            return authorizationBean.getLogin();
        }
    }
}
