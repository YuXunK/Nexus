package com.nexusget.nexuscontentplat.common.Utils;

import com.nexusget.nexuscontentplat.domain.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.security.auth.message.AuthException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @descrpition 负责生成token
 * @author nexus
 */
@Component
public class JwtProvider {
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String secret;

    // 过期时间获取，辅助token刷新
    // 直接返回配置的过期时间（秒）
    @Getter
    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(User user) {
        // 1. 将密钥转换为安全的 SecretKey
        SecretKey key = getSigningKey();

        // 2. 使用新API构建Token
        String token = Jwts.builder()
                .subject(user.getUserId().toString())       // 替代 setSubject
                .claims(createClaims(user))                // 添加自定义Claims
                .issuedAt(Date.from(Instant.now()))       // 替代 setIssuedAt
                .expiration(calculateExpiration())         // 替代 setExpiration
                .signWith(key)                            // 替代 signWith(SignatureAlgorithm, String)
                .compact();
        return BEARER_PREFIX + token;
    }

    // 解析token内容
    public Claims parseToken(String bearerToken) throws AuthException {
        try {
            String token = removeBearerPrefix(bearerToken);
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new AuthException("Token解析失败: " + e.getMessage());
        }
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUserId());
        claims.put("username", user.getUserName());
        claims.put("is_admin", user.getIsAdmin());
        // 可添加更多自定义字段（如角色、权限等）
        return claims;
    }

    private Date calculateExpiration() {
        return Date.from(Instant.now().plusSeconds(expiration));
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ===== 移除Bearer前缀 =====
    private String removeBearerPrefix(String token) throws AuthException {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        throw new AuthException("Token格式错误，必须以Bearer开头");
    }

    public Long paresTokenToGetId(String token) throws RuntimeException {
        // 1. 校验Token格式
        if (StringUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            throw new com.nexusget.nexuscontentplat.common.excption.AuthException("AUTH-400", "Token格式错误");
        }

        // 2. 解析Token获取用户ID
        Claims claims;
        try {
            claims = parseToken(token);
        } catch (Exception e) {
            throw new com.nexusget.nexuscontentplat.common.excption.AuthException("AUTH-401", "Token解析失败");
        }
        return Long.parseLong(claims.getSubject());
    }
}
