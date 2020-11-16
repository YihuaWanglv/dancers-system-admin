package com.dancers.service.system.api;

import com.dancers.service.system.dto.UserDTO;
import com.dancers.service.system.service.SystemUserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class SystemAuthController {

    @Resource
    SystemUserAuthService systemUserAuthService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(String username, String password) {
        UserDTO user = systemUserAuthService.login(username, password);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        systemUserAuthService.logout(request);
        return ResponseEntity.ok(true);
    }
}
