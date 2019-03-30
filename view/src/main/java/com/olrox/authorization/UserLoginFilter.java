package com.olrox.authorization;

import com.olrox.authorization.domain.Role;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {UserLoginFilter.USER_FILTER + "*", UserLoginFilter.ADMIN_FILTER + "*"})
public class UserLoginFilter implements Filter {
    public final static String USER_FILTER = "/user/";
    public final static String ADMIN_FILTER = "/admin/";

    @Inject
    private AuthorizationBean authorizationBean;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(authorizationBean.getRole() != null){

            String uri = request.getRequestURI();
            String beginOfAdminUri = request.getContextPath() + ADMIN_FILTER;

            if(uri.startsWith(beginOfAdminUri) && authorizationBean.getRole() != Role.ADMIN){
                response.sendRedirect(request.getContextPath() + "/errors.xhtml");
                return;
            }

            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }



        authorizationBean.setRequestedPage(request.getRequestURI());
        response.sendRedirect(request.getContextPath() + "/login.xhtml");
    }

    @Override
    public void destroy() {

    }
}
