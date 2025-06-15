package com.nexusget.nexuscontentplat.domain.VO;

import com.nexusget.nexuscontentplat.common.Utils.DateUtils;
import com.nexusget.nexuscontentplat.domain.BO.ArticleListBO;
import lombok.Data;

/**
 * 文章列表视图对象（返回给前端）
 */
@Data
public class ArticleListVO {
    private String id;
    private String title;
    private String excerpt;
    private String createTime; // 格式化后的时间字符串
    private AuthorVO author;
    private StatsVO stats;

    @Data
    public static class AuthorVO {
        private Long userId;
        private String username;
        private String avatarUrl;
    }

    @Data
    public static class StatsVO {
        private Integer likeCount;
    }

    // 转换方法
    public static ArticleListVO fromBO(ArticleListBO bo) {
        ArticleListVO vo = new ArticleListVO();
        vo.setId(bo.getArticleId());
        vo.setTitle(bo.getTitle());
        vo.setExcerpt(bo.getExcerpt());
        vo.setCreateTime(DateUtils.format(bo.getCreatedAt()));

        AuthorVO author = new AuthorVO();
        author.setUserId(bo.getUserId());
        author.setUsername(bo.getUsername());
        author.setAvatarUrl(bo.getAvatarUrl());
        vo.setAuthor(author);

        StatsVO stats = new StatsVO();
        stats.setLikeCount(bo.getLikeCount());
        vo.setStats(stats);

        return vo;
    }
}
