package com.lwdevelop.customerServiceAdmin.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.lwdevelop.customerServiceAdmin.dto.AdminDTO;
import com.lwdevelop.customerServiceAdmin.entity.Admin;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;
import com.lwdevelop.customerServiceAdmin.service.impl.AdminServiceImpl;
import com.lwdevelop.customerServiceAdmin.utils.CommUtils;
import com.lwdevelop.customerServiceAdmin.utils.ResponseUtils;
import com.lwdevelop.customerServiceAdmin.utils.RetEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admins")
public class AdminPanelController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AdminServiceImpl adminService;

    @PostMapping("/getAllAdmins")
    public ResponseEntity<ResponseUtils.ResponseData> getAllAdmins(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam("input") String input) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        List<Admin> adminList = adminService.findAll(input, page, pageSize);
        Object pager = CommUtils.Pager(page, pageSize, adminList.size());
        res.put("list", adminList);
        res.put("pager", pager);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, res);
    }

    @PostMapping("/updateAdmin")
    public ResponseEntity<ResponseUtils.ResponseData> updateAdmin(
            HttpServletRequest request,
            @RequestBody AdminDTO adminDTO) throws Exception {
        String adminDTOUsername = adminDTO.getUsername();
        Admin username = adminService.findByUsername(adminDTOUsername);

        if (username != null) {
            log.info("AdminPanelController ==> addAdmin ... 用戶已經存在 [ {} ]", adminDTOUsername);
            return ResponseUtils.response(RetEnum.RET_USER_EXISTS, new HashMap<>());
        }
        Admin admin = adminService.findById(adminDTO.getId()).get();
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(adminDTO.getPassword());
        admin.setEnabled(adminDTO.getEnabled());
        adminService.saveAdmin(admin);
        log.info("AdminPanelController ==> updateAdmin ... [ {} ]", "done");
        return ResponseUtils.response(RetEnum.RET_SUCCESS, new HashMap<>(), "編輯成功");
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<ResponseUtils.ResponseData> addAdmin(
            HttpServletRequest request,
            @RequestBody AdminDTO adminDTO) throws Exception {

        String adminDTOUsername = adminDTO.getUsername();
        Admin username = adminService.findByUsername(adminDTOUsername);

        if (username != null) {
            log.info("AdminPanelController ==> addAdmin ... 用戶已經存在 [ {} ]", adminDTOUsername);
            return ResponseUtils.response(RetEnum.RET_USER_EXISTS, new HashMap<>());
        }

        Admin admin = new Admin();
        List<String> roles = Arrays.asList(new String[] { "ADMIN" });
        admin.setUsername(adminDTOUsername);
        admin.setPassword(adminDTO.getPassword());
        admin.setEnabled(adminDTO.getEnabled());
        admin.setRoles(roles);
        admin.setRegIp(CommUtils.getClientIP(request));
        admin.setEnabled(true);
        admin.setLastLoginIP(CommUtils.getClientIP(request));
        adminService.saveAdmin(admin);
        log.info("AdminPanelController ==> addAdmin ... [ {} ]", adminDTOUsername + "新增成功");
        return ResponseUtils.response(RetEnum.RET_SUCCESS, new HashMap<>(), "新增成功");
    }

    @PostMapping("/delAdmin")
    public ResponseEntity<ResponseUtils.ResponseData> delAdmin(
            HttpServletRequest request,
            @RequestBody Map<String, String> requestData) throws Exception {
        String[] ids = requestData.get(requestData.keySet().toArray()[0]).split(",");
        for (String id : ids) {
            adminService.deleteById(Long.parseLong(id));
        }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, new HashMap<>(), "刪除成功");
    }

    @GetMapping("/admin_panel")
    public String adminPanel(Model model) {
        // List<User> users = userService.getAllUsers();
        // model.addAttribute("users", users);
        return "admin_panel";
    }

    @PostMapping("/send-message")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println("Message received: " + chatMessage);
        messagingTemplate.convertAndSend("/topic/messages", chatMessage);
    }

}