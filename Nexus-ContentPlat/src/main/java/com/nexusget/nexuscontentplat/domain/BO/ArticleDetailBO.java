package com.nexusget.nexuscontentplat.domain.BO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章详情业务对象
 */
@Data
public class ArticleDetailBO {
    private String article_custom_id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private UserBO author;          // 作者信息
    private List<CommentBO> comments; // 评论列表
    private Integer likeCount;
    private Integer favoriteCount;

    @Data
    public static class UserBO {
        private Long userId;
        private String username;
        private String avatarUrl;
    }

    @Data
    public static class CommentBO {
        private Long commentId;
        private String content;
        private LocalDateTime createTime;
        private UserBO commenter;
    }
}
