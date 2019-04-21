package com.olrox.filter;

import com.olrox.account.CurrentSessionBean;
import com.olrox.account.domain.Role;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter( urlPatterns = UserFilter.FILTER + "*", filterName = "UserFilter")
public class UserFilter implements Filter {
    public final static String FILTER = "/user/";

    @Inject
    private CurrentSessionBean currentSessionBean;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(currentSessionBean.getRole() != null){

            String uri = request.getRequestURI();
            String beginOfUserUri = request.getContextPath() + FILTER;

            if(uri.startsWith(beginOfUserUri) && currentSessionBean.getRole() != Role.USER){
                response.sendRedirect(request.getContextPath() + "/errors.xhtml");
                return;
            }

            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        currentSessionBean.setRequestedPage(request.getRequestURI());
        response.sendRedirect(request.getContextPath() + "/index.xhtml");
    }

    @Override
    public void destroy() {

    }
}
