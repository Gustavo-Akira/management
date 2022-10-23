package br.com.eaa.management.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTApiAuthenticationFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, ExpiredJwtException {
        try {
            Authentication authentication = new JWTAuthenticationService().getAuthentication((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (Exception e){
            servletResponse.getOutputStream().println("{\"Error\":\" Expired Token\"}");
            servletResponse.setContentType("application/json");
        }
    }
}
