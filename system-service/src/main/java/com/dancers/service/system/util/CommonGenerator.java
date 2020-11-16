package com.dancers.service.system.util;

import java.util.UUID;

public class CommonGenerator {

    public static String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
