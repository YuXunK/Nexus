package com.nexusget.nexuscontentplat.domain.BO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户核心业务对象
 */
@Data
public class UserBO {
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String password;
    private String avatarUrl;
    private String selfIntroduce;
    private Boolean isAdmin;
    private LocalDateTime lastLoginAt;

    // 业务方法
    public boolean isActive() {
        return lastLoginAt != null &&
                lastLoginAt.isAfter(LocalDateTime.now().minusMonths(1));
    }
}
