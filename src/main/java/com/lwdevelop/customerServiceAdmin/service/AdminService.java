package com.lwdevelop.customerServiceAdmin.service;

import java.util.List;
import java.util.Optional;

import com.lwdevelop.customerServiceAdmin.entity.Admin;

public interface AdminService {
    Optional<Admin> findById(Long id);
    Admin findByUsername(String username);
    List<Admin> findAll(String username, int page, int pageSize);
    void saveAdmin(Admin admin);
    void deleteById(Long id);
}