package com.nexusget.nexuscontentplat.common.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Order(1)
public class TokenValidationGlobalFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "Nexus_Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String JWT_SECRET = "your-256-bit-secret-key-here-1234567890abc"; // 需替换为实际密钥

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 放行公开路径
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 获取并验证Token
        String token = resolveToken(request);
        if (token == null || token.isEmpty()) {
            sendError(response, "缺少访问令牌", 401);
            return;
        }

        // 3. 解析并验证Token
        try {
            // 3.1 验证签名和基本格式
            JWT jwt = JWTUtil.parseToken(token);
            if (!jwt.setKey(JWT_SECRET.getBytes(StandardCharsets.UTF_8)).verify()) {
                sendError(response, "令牌签名无效", 403);
                return;
            }

            // 3.2 验证有效期
            JWTValidator.of(jwt).validateDate();

            // 3.3 提取用户信息（示例：用户ID和角色）
            Map<String, Object> payload = jwt.getPayloads();
            String userId = (String) payload.get("userId");
            String role = (String) payload.get("role");

            // 3.4 将用户信息存入请求属性（供后续Controller使用）
            request.setAttribute("userId", userId);
            request.setAttribute("userRole", role);

        } catch (Exception e) {
            sendError(response, "令牌验证失败: " + e.getMessage(), 403);
            return;
        }

        // 4. 验证通过，放行请求
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中提取Token
     */
    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(TOKEN_HEADER);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * 判断是否为公开接口
     */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/auth/")
                || uri.equals("/error")
                || uri.startsWith("/swagger")
                || uri.startsWith("/v3/api-docs");
    }

    /**
     * 返回JSON格式错误信息
     */
    private void sendError(HttpServletResponse response, String message, int code)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(code);
        response.getWriter().write(
                String.format("{\"code\": %d, \"message\": \"%s\"}", code, message)
        );
    }
}