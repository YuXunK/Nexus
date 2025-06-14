package com.nexusget.nexuscontentplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusget.nexuscontentplat.domain.Entity.User_like_article;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserLikeArticleMapper extends BaseMapper<User_like_article> {
    /**
     * 查询所有有效的喜欢记录（未取消的）
     */
    @Select("SELECT * FROM user_like_article WHERE is_canceled = 0")
    List<User_like_article> selectActiveLikes();
}
