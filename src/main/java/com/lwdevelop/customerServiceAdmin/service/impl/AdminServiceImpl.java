package com.lwdevelop.customerServiceAdmin.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.lwdevelop.customerServiceAdmin.dto.AdminDTO;
import com.lwdevelop.customerServiceAdmin.entity.Admin;
import com.lwdevelop.customerServiceAdmin.repository.AdminRepository;
import com.lwdevelop.customerServiceAdmin.service.AdminService;
import com.lwdevelop.customerServiceAdmin.utils.CommUtils;
import com.lwdevelop.customerServiceAdmin.utils.JwtUtils;
import com.lwdevelop.customerServiceAdmin.utils.ResponseUtils;
import com.lwdevelop.customerServiceAdmin.utils.RetEnum;
import com.lwdevelop.customerServiceAdmin.utils.ResponseUtils.ResponseData;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
                .password("{noop}" + admin.getPassword())
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
    public List<Admin> findAll(String username, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return adminRepository.findAllByUsernameContaining(username, pageable).getContent();
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<ResponseData> loginProcess(HttpServletRequest request, String username, String password) {
        Admin admin = findByUsername(username);
        HashMap<String, Object> data = new HashMap<>();
        if (admin == null) {
            return ResponseUtils.response(RetEnum.RET_USER_NOT_EXIST, data);
        }
        if (!admin.getPassword().equals(password)) {
            return ResponseUtils.response(RetEnum.RET_USER_PASSWORD_ERROR, data);
        }
        if (!admin.getEnabled()) {
            return ResponseUtils.response(RetEnum.RET_USER_DISABLED, data);
        }
        try {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.length() > 255) {
                userAgent = userAgent.substring(0, 255);
            }
            admin.setLastLoginIP(CommUtils.getClientIP(request));
            saveAdmin(admin);
            log.info("AdminPanelController ==> login ... [ {}{} ]", username, "登入成功");
            JwtUtils jwtToken = new JwtUtils();
            String token = jwtToken.generateToken(admin); // 取得token
            data.put("token", token);
            return ResponseUtils.response(RetEnum.RET_SUCCESS, data, "登入成功");
        } catch (Exception e) {
            log.info("AdminPanelController ==> login ... [ {} ] Exception:{}", "登入失敗", e.toString());
            return ResponseUtils.response(RetEnum.RET_LOGIN_FAIL, data);
        }

    }

    @Override
    public ResponseEntity<ResponseData> loginOutProcess(HttpServletRequest request, String token) {
        new JwtUtils().invalidateToken(token);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, new HashMap<>());
    }

    @Override
    public ResponseEntity<ResponseData> getInfoProcess(HttpServletRequest request, String token) {
        String username = new JwtUtils().verifyToken(token);
        Admin admin = findByUsername(username);
        HashMap<String, Object> data = new HashMap<>();
        data.put("info", admin);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> getAllAdminsProcess(HttpServletRequest request, int page, int pageSize,
            String input) {
        HashMap<String, Object> data = new HashMap<>();
        List<Admin> adminList = findAll(input, page, pageSize);
        Object pager = CommUtils.Pager(page, pageSize, adminList.size());
        data.put("list", adminList);
        data.put("pager", pager);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> updateAdminProcess(HttpServletRequest request, AdminDTO adminDTO) {
        String adminDTOUsername = adminDTO.getUsername();
        Admin username = findByUsername(adminDTOUsername);

        if (username != null) {
            log.info("AdminPanelController ==> addAdmin ... 用戶已經存在 [ {} ]", adminDTOUsername);
            return ResponseUtils.response(RetEnum.RET_USER_EXIST, new HashMap<>());
        }
        Admin admin = findById(adminDTO.getId()).get();
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(adminDTO.getPassword());
        admin.setEnabled(adminDTO.getEnabled());
        saveAdmin(admin);
        log.info("AdminPanelController ==> updateAdmin ... [ {} ]", "done");
        return ResponseUtils.response(RetEnum.RET_SUCCESS, new HashMap<>(), "編輯成功");
    }

    @Override
    public ResponseEntity<ResponseData> deleteAdminProcess(HttpServletRequest request, Map<String, String> requestData) {
        String[] ids = requestData.get(requestData.keySet().toArray()[0]).split(",");
        for (String id : ids) {
            deleteById(Long.parseLong(id));
        }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, new HashMap<>(), "刪除成功");
    }

    @Override
    public ResponseEntity<ResponseData> addAdminProcess(HttpServletRequest request, AdminDTO adminDTO) {
        String adminDTOUsername = adminDTO.getUsername();
        Admin username = findByUsername(adminDTOUsername);

        if (username != null) {
            log.info("AdminPanelController ==> addAdmin ... 用戶已經存在 [ {} ]", adminDTOUsername);
            return ResponseUtils.response(RetEnum.RET_USER_EXIST, new HashMap<>());
        }

        Admin admin = new Admin();
        List<String> roles = Arrays.asList(new String[] { "ADMIN" });
        admin.setUsername(adminDTOUsername);
        admin.setPassword(adminDTO.getPassword());
        admin.setEnabled(adminDTO.getEnabled());
        admin.setRoles(roles);
        admin.setRegIp(CommUtils.getClientIP(request));
        admin.setLastLoginIP(CommUtils.getClientIP(request));
        saveAdmin(admin);
        log.info("AdminPanelController ==> addAdmin ... [ {} ]", adminDTOUsername + "新增成功");
        return ResponseUtils.response(RetEnum.RET_SUCCESS, new HashMap<>(), "新增成功");
    }

    @Override
    public ResponseEntity<ResponseData> passwordChangeProcess(HttpServletRequest request, AdminDTO adminDTO) {

        Long id = adminDTO.getId();
        String password = adminDTO.getPassword();
        Admin admin = findById(id).get();
        admin.setPassword(password);
        saveAdmin(admin);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, new HashMap<>(), "修改密碼成功，即將跳轉到登入頁面");
    }
}
