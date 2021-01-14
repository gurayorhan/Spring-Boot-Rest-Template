package com.rest.template.service;

import com.rest.template.model.User;
import com.rest.template.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    public User findById(String s) throws Exception {
        try {
            return userRepository.findByUsername(s);
        }catch (Exception e){
            throw new Exception("User findById Error",e);
        }
    }

}
