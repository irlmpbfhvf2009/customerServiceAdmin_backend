package com.lwdevelop.customerServiceAdmin.service;

import com.lwdevelop.customerServiceAdmin.entity.Admin;
public interface AdminService {
    Admin findByUsername(String username);
    void saveAdmin(Admin admin);
}