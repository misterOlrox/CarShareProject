package com.olrox.filter;

import com.olrox.account.AuthorizationBean;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter( urlPatterns = {"/account.xhtml", "/registration.xhtml", "/index.xhtml"},
            filterName = "LoggedFilter")

public class LoggedFilter implements Filter {
    @Inject
    private AuthorizationBean authorizationBean;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(authorizationBean.getRole() != null){
            response.sendRedirect("user/hello-for-user.xhtml");
        }
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }
}
