package com.lwdevelop.customerServiceAdmin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lwdevelop.customerServiceAdmin.entity.User;
import com.lwdevelop.customerServiceAdmin.repository.UserRepository;
import com.lwdevelop.customerServiceAdmin.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }

    @Override
    public String findUserByEmail(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'findUserByIp'");
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

}
