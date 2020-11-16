package com.dancers.service.system.util;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


/**
 * 密码处理帮助类
 *
 * @author iyihua
 *
 */
public class PasswordHelper {

    public static boolean isPasswordRight(String password, String salt, String passwordInput) {
        return superEncrypt(passwordInput, salt).equals(password) ? true : false;
    }

    public static String superEncrypt(String password, String salt) {
        String result = "";
        try {
            result = PasswordSecureHash.hashEncrypt(password, salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return result;
    }
}