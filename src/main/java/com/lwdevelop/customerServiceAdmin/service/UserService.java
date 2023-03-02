package com.lwdevelop.customerServiceAdmin.service;

import com.lwdevelop.customerServiceAdmin.entity.User;

public interface UserService {
    User getCurrentUser();
    void save(User user);
    String findUserByEmail(String email);
}
