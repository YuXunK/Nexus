package com.nexusget.nexuscontentplat.domain.VO;

import lombok.Data;

/**
 * 用户公开信息视图对象
 */
@Data
public class UserVO {
    private Long userId;
    private String username;
    private String avatarUrl;
    private String selfIntroduce;
    private String socialInfo; // JSON字符串，存储社交链接

    // 统计信息
    private Integer articleCount;
    private Integer followerCount;
    private Integer followingCount;

    // 时间字段（ISO格式）
    private String registerTime;
    private String lastLoginTime;
}
