package com.nexusget.nexuscontentplat.service.Impl;

import com.nexusget.nexuscontentplat.common.Utils.JwtProvider;
import com.nexusget.nexuscontentplat.common.Utils.StringUtils;
import com.nexusget.nexuscontentplat.domain.Entity.User;
import com.nexusget.nexuscontentplat.service.AccessService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

/**
 * @author nexus
 * 登录授权模块
 */
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    // Token续期阈值（默认在过期前30分钟触发续期）
    @Value("${jwt.renew-threshold:1800}")
    private long renewThresholdSeconds;

    @Override
    public String SysAccess(User user) {
        // 直接返回带Bearer的Token
        return jwtProvider.generateToken(user);
    }

    @Override
    public String validateAndRenewToken(String token) throws AuthException {
        // 1. 基础校验
        if (StringUtils.isEmpty(token)) {
            throw new AuthException("Token不能为空");
        }

        // 2. 解析Token
        Claims claims;
        try {
            claims = jwtProvider.parseToken(token);
        } catch (Exception e) {
            throw new AuthException("Token解析失败");
        }

        // 3. 检查Redis中的有效性
        Long userId = Long.parseLong(claims.getSubject());
        String storedToken = (String) redisTemplate.opsForValue().get("user:token:" + userId);
        if (!token.equals(storedToken)) {
            throw new AuthException("Token已失效");
        }

        // 4. 自动续期逻辑
        Date expiration = claims.getExpiration();
        long remainSeconds = (expiration.getTime() - System.currentTimeMillis()) / 1000;

        if (remainSeconds <= renewThresholdSeconds) {
            // 触发续期：生成新Token并更新Redis
            User user = User.builder()
                    .userId(userId)
                    .userName(claims.get("username", String.class))
                    .isAdmin(claims.get("is_admin", Integer.class))
                    .build();
            // 返回刷新token
            return SysAccess(user);
        }

        return token;
    }
}