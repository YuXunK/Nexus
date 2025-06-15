package com.nexusget.nexuscontentplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusget.nexuscontentplat.domain.Entity.Article_like_stats;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ArticleLikeStatsMapper extends BaseMapper<Article_like_stats> {
     List<Article_like_stats> selectLikeCountsByArticleIds(@Param("articleIds") Set<Long> articleIds);
}
