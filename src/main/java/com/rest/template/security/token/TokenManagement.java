package com.rest.template.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenManagement {

    private static final Integer extensionTime = 7200000; // 2h;

    private static final Map<String, Token> sessionMap = new HashMap<>();

    private static final List<String> sessionList = new ArrayList<>();

    @Autowired
    private TokenProvider tokenProvider;

    @Scheduled(fixedDelay = 60000) // her 1 dkk da bir
    private void allSessionCheck( ){
        for (String s:sessionList) {
            Token jwt = sessionMap.getOrDefault(s,null);
            if(jwt != null){
                if (jwt.getExtinctionDate().getTime() < System.currentTimeMillis()){
                    sessionMap.remove(s);
                    sessionList.remove(s);
                }
            }
        }
        System.out.println("Active Token: " + sessionList.size());
    }

    public String createSession(String username,String password) throws Exception {
        try{
            String token = tokenProvider.generateToken(username,password);
            Token jwt = new Token(new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()+extensionTime));
            sessionList.add(token);
            sessionMap.put(token, jwt);
            return token;
        }catch (Exception e){
            throw new Exception("Session Create Error");
        }
    }

    public Boolean checkSession(String token) throws Exception {
        try{
            Token jwt = sessionMap.getOrDefault(token,null);
            if(jwt != null){
                if (jwt.getExtinctionDate().getTime() > System.currentTimeMillis()){
                    jwt.setExtinctionDate(new Date(System.currentTimeMillis()+extensionTime));
                    sessionMap.put(token, jwt);
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            throw new Exception("Session check error");
        }
    }

    public Boolean deleteSession(String token) throws Exception {
        try{
            sessionMap.remove(token);
            sessionList.remove(token);
            return true;
        }catch (Exception e){
            throw new Exception("Session delete error");
        }
    }

}
