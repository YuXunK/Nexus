package com.nexusget.nexuscontentplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusget.nexuscontentplat.domain.Entity.User_follow_group;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserFollowGroupMapper extends BaseMapper<User_follow_group> {
    @Select("SELECT DISTINCT user_id FROM user_follow_group")
    List<Long> selectDistinctUserIds();

    @Select("SELECT COUNT(*) FROM user_follow_group WHERE user_id = #{userId} AND is_default = 1")
    boolean existsDefaultGroup(Long userId);

    @Select("SELECT follow_gp_id FROM user_follow_group WHERE user_id = #{userId} AND is_default = 1 LIMIT 1")
    Long selectDefaultGroupId(Long userId);

    @Select("SELECT COUNT(1) FROM user_follow_group " +
            "WHERE user_id = #{userId} AND name = #{groupName}")
    boolean exists(@Param("userId") Long userId,
                   @Param("groupName") String groupName);

}
