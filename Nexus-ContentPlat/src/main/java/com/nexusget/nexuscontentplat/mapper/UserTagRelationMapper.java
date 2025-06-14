package com.nexusget.nexuscontentplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusget.nexuscontentplat.domain.Entity.User_tag_relation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserTagRelationMapper extends BaseMapper<User_tag_relation> {
    /**
     * 检查指定用户和文章是否已存在关联
     */
    @Select("SELECT COUNT(1) FROM user_tag_relation " +
            "WHERE user_id = #{userId} AND tag_id = #{articleId}")
    boolean existsRelation(
            @Param("userId") Long userId,
            @Param("articleId") Long articleId
    );
}
