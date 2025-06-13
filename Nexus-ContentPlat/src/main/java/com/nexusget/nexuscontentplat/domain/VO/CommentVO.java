package com.nexusget.nexuscontentplat.domain.VO;

import lombok.Data;

import java.util.List;

/**
 * 嵌套评论VO
 */
@Data
public class CommentVO {
    private Long commentId;
    private String content;
    private UserVO author;
    private String createTime;
    private Integer likeCount;
    private Boolean isLiked;

    //=== 回复相关字段 ===//
    private Long rootId;          // 根评论ID
    private Long parentId;        // 直接父评论ID
    private UserVO replyToUser;   // 回复的目标用户

    //=== 子评论 ===//
    private List<CommentVO> replies; // 二级回复
    private Boolean hasMoreReplies;  // 是否还有更多回复
    private Integer replyTotal;      // 回复总数（用于"查看全部X条回复"）
}
