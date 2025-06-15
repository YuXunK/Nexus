package com.nexusget.nexuscontentplat.domain.VO;

import lombok.Data;

import java.util.List;

/**
 * 文章详情视图对象（支持嵌套评论）
 */
@Data
public class ArticleDetailVO {
    //=== 文章核心信息 ===//
    private String custom_id;
    private String title;
    private String content;
    private String createTime;

    //=== 作者信息 ===//
    private AuthorVO author;

    //=== 互动数据 ===//
    private StatsVO stats;

    //=== 评论列表（一级评论+部分回复）===//
    private List<CommentNodeVO> comments;
    private Integer totalComments; // 评论总数（用于分页）

    //=== 嵌套VO定义 ===//
    // AuthorVO 就是文章id相关的user基础信息
    @Data
    public static class AuthorVO {
        private Long userId;
        private String username;
        private String avatarUrl;
    }
    // 点赞统计和判断当前用户是否点赞的判定符
    @Data
    public static class StatsVO {
        private Integer likeCount;
        private Boolean isLiked;     // 当前用户是否点赞
    }

    /**
     * 评论树节点VO（支持嵌套）
     */
    @Data
    public static class CommentNodeVO {
        private Long id;
        private String content;
        private String displayContent; // 处理后的内容（含@和emoji）
        private String createTime;
        private CommenterVO user;
        private CommenterVO replyToUser; // 被回复的用户（如果是回复）
        private Integer likeCount;
        private Boolean isLiked;

        // 子评论（默认加载3条）
        private List<CommentNodeVO> replies;
        private Boolean hasMoreReplies;
        private Integer replyTotal;
    }

    @Data
    public static class CommenterVO {
        private Long userId;
        private String username;
        private String avatarUrl;
    }
}