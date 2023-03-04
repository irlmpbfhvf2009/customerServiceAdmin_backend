package com.lwdevelop.customerServiceAdmin.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.lwdevelop.customerServiceAdmin.entity.Admin;
import com.lwdevelop.customerServiceAdmin.repository.AdminRepository;
import com.lwdevelop.customerServiceAdmin.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService, UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    
    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDetails userDetails = User.builder()
                .username(admin.getUsername())
                        .password("{noop}"+admin.getPassword()) 
                        .roles(admin.getRoles().get(0))
                        .authorities(new SimpleGrantedAuthority(admin.getRoles().get(0)))
                        .build();
        return userDetails;
    }


    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public List<Admin> findAll(String username,int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return adminRepository.findAllByUsernameContaining(username,pageable).getContent();
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }
}
