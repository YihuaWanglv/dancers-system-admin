package com.dancers.service.system.service;


import com.dancers.service.system.domain.SystemUser;
import com.dancers.service.system.domain.SystemUserRepository;
import com.dancers.service.system.dto.UserDTO;
import com.dancers.service.system.exceptions.UnauthorizedException;
import com.dancers.service.system.repository.SystemUserTokenRepository;
import com.dancers.service.system.util.CommonGenerator;
import com.dancers.service.system.util.ModelDefaultTransfer;
import com.dancers.service.system.util.PasswordHelper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
public class SystemUserAuthService {

    @Resource
    private SystemUserTokenRepository systemUserTokenRepository;
    @Resource
    private SystemUserRepository systemUserRepository;

    private UserDTO getUserToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (null == token) {
            token = request.getParameter("token");
        }

        if (null == token) {
            return null;
        }

        return systemUserTokenRepository.get(token);
    }

    public void saveUserToken(UserDTO userToken) {
        systemUserTokenRepository.set(userToken.getToken(), userToken);
    }

    public UserDTO createToken(Long userId) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        UserDTO userToken = new UserDTO();
        userToken.setUserId(userId);
        userToken.setToken(token);
        return userToken;
    }

    public String createToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public boolean valid(String token) {
        UserDTO userToken = systemUserTokenRepository.get(token);
        // 每次校验后，重新写入缓存，跟新过期时间
        systemUserTokenRepository.set(token, userToken);
        return userToken != null;
    }

    private String findUriMatchModuleKey(String uri) {
        return null;
    }

    public boolean isUserRegister(String token) {
        UserDTO userToken = systemUserTokenRepository.get(token);
        return userToken != null;
    }

    public Long getTokenUserId(HttpServletRequest request) {
        UserDTO token = this.getUserToken(request);
        if (token != null) {
            return token.getUserId();
        }
        return null;
    }

    public void clearUserToken(HttpServletRequest request) {
        UserDTO token = this.getUserToken(request);
        if (token != null) {
            systemUserTokenRepository.expire(token.getToken());
        }
    }

    public void logout(HttpServletRequest request) {
        clearUserToken(request);
    }

    public UserDTO login(String username, String password) {
        SystemUser u = systemUserRepository.findOne(Example.of(SystemUser.builder().username(username).isDeleted(0).build()))
                .orElseThrow(UnauthorizedException::new);
        boolean isAuth = auth(password, u.getPassword(), u.getSalt());
        if (!isAuth) {
            throw new UnauthorizedException();
        }
        String token = CommonGenerator.generateToken();
        UserDTO user = ModelDefaultTransfer.transfer(u, UserDTO.class);
        user.setToken(token);
        saveUserToken(user);
        user.setAccess(new String[]{"MENU_SYSTEM_ORG", "MENU_SYSTEM_USER"});
        return user;
    }

    private boolean auth(String passwordInput, String password, String salt) {
        return PasswordHelper.isPasswordRight(password, salt, passwordInput);
    }
}