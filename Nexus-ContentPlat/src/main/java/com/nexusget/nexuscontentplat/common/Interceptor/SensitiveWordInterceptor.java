package com.nexusget.nexuscontentplat.common.Interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusget.nexuscontentplat.common.filter.SensitiveWordFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * 敏感词请求拦截器
 */
@Component
@RequiredArgsConstructor
public class SensitiveWordInterceptor implements HandlerInterceptor {
    public final SensitiveWordFilter filter;
    private final ObjectMapper objectMapper; // 添加这行

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 只拦截POST/PUT请求
        if (!List.of("POST", "PUT").contains(request.getMethod())) {
            return true;
        }

        // 2. 从请求中提取文本内容（适配不同Content-Type）
        String content = extractContent(request);
        if (content == null) return true;

        // 3. 执行敏感词检测
        if (filter.containsSensitive(content)) {
            response.setContentType("application/json");
            response.setStatus(400);
            response.getWriter().write("""
                {
                    "code": 40003,
                    "message": "内容包含敏感词汇",
                    "data": null
                }
                """);
            return false;
        }
        return true;
    }

    private String extractContent(HttpServletRequest request) {
        return switch (request.getContentType()) {
            case "application/json" -> parseJsonBody(request);
            case "application/x-www-form-urlencoded" -> request.getParameter("content");
            default -> null;
        };
    }

    private String parseJsonBody(HttpServletRequest request) {
        try {
            // 使用Jackson快速提取（避免完整反序列化）
            JsonNode root = objectMapper.readTree(request.getInputStream());
            return root.path("content").asText();
        } catch (Exception e) {
            return null;
        }
    }
}
