package com.rest.template.security;

import com.rest.template.model.Role;
import com.rest.template.model.User;
import com.rest.template.security.token.TokenManagement;
import com.rest.template.security.token.TokenProvider;
import com.rest.template.util.AllUserPermissions;
import com.rest.template.util.UserRolePermissions;
import io.jsonwebtoken.JwtException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SecurityFilter extends GenericFilter {

    private TokenProvider tokenProvider;

    private TokenManagement tokenManagement;

    public SecurityFilter(TokenProvider tokenProvider, TokenManagement tokenManagement) {
        this.tokenProvider = tokenProvider;
        this.tokenManagement = tokenManagement;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String token = null;

        if(!unauthorizedControl(request.getRequestURI())){

            try{
                token = tokenProvider.resolveToken(request);
            }catch (JwtException | IllegalArgumentException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT Token");
                return;
            }

            if(token == null){
                try{
                    token = tokenProvider.resolveToken(request.getParameter("authorization"));
                }catch (Exception e){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT Token");
                    return;
                }
            }

            try {
                if(!tokenManagement.checkSession(token)){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT Token");
                    return;
                }
                if(!authorizedControl(request.getRequestURI(),tokenProvider.getToUser(token))){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Authorization Error");
                    return;
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Boolean unauthorizedControl(String url){
        for (String s : AllUserPermissions.All_PERMISSIONS) {
            if (check(url, s)) return true;
        }
        return false;
    }

    private Boolean authorizedControl(String url, User user){
        for (Role role : user.getRoles()) {
            List<String> permission = UserRolePermissions.USER_TO_ROLES_MAP.get(role.getName());
            for (String s : permission) {
                if (s.equals("*")){
                    return true;
                } else {
                    if (check(url, s)) return true;
                }
            }
        }
        return false;
    }

    private boolean check(String url, String s) {
        if(s.endsWith("**")){
            s = s.substring(0, s.length()-2);
            if(url.contains(s)) {
                return true;
            }
        }
        else if (s.equals(url)){
            return true;
        }
        return false;
    }

}
