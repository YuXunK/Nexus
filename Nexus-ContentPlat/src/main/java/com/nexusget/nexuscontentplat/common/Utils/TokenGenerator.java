package com.nexusget.nexuscontentplat.common.Utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TokenGenerator {
    private static final String SECRET = "your-256-bit-secret-key-here-1234567890abc";
    private static final long EXPIRE_SECONDS = 14400; // 4小时

    public static String generateToken(String userId, String role) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("role", role);
        payload.put("iat", DateUtil.currentSeconds()); // 签发时间
        payload.put("exp", DateUtil.currentSeconds() + EXPIRE_SECONDS); // 过期时间

        return JWTUtil.createToken(payload, SECRET.getBytes(StandardCharsets.UTF_8));
    }
}
