package com.nexusget.nexuscontentplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusget.nexuscontentplat.domain.Entity.User_tag_group;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserTagGroupMapper extends BaseMapper<User_tag_group> {
    /**
     * 查询所有有标签组的用户ID（去重）
     */
    @Select("SELECT DISTINCT user_id FROM user_tag_group")
    List<Long> selectDistinctUserIds();

    /**
     * 检查用户是否有默认标签组
     * @param userId 用户ID
     * @return 是否存在默认组
     */
    @Select("SELECT COUNT(1) FROM user_tag_group " +
            "WHERE user_id = #{userId} AND name = '未分类'")
    boolean existsDefaultGroup(@Param("userId") Long userId);

    /**
     * 获取用户的默认标签组ID
     */
    @Select("SELECT user_tag_gp_id FROM user_tag_group " +
            "WHERE user_id = #{userId} AND name = '未分类' LIMIT 1")
    Long selectDefaultGroupId(@Param("userId") Long userId);

    @Select("SELECT COALESCE(MAX(sort), 0) + 1 FROM user_tag_group " +
            "WHERE user_id = #{userId}")
    int selectNextSortValue(@Param("userId") Long userId);

    // UserTagGroupMapper.java
    @Select("SELECT COUNT(1) FROM user_tag_group " +
            "WHERE user_id = #{userId} AND name = #{groupName}")
    boolean exists(@Param("userId") Long userId,
                   @Param("groupName") String groupName);
}
