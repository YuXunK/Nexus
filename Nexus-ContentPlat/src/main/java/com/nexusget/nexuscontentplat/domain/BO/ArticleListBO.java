package com.nexusget.nexuscontentplat.domain.BO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章列表业务对象（用于服务层内部处理）
 */
@Data
public class ArticleListBO {
    private Long articleId;
    private String title;
    private String excerpt;
    private String excerpt_img;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
    private String avatarUrl;
    private Integer likeCount;
    private Integer favoriteCount;

    // 业务方法
    public boolean isPopular() {
        return likeCount != null && likeCount > 100;
    }
}