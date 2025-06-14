package com.nexusget.nexuscontentplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusget.nexuscontentplat.domain.Entity.User_follow;
import com.nexusget.nexuscontentplat.domain.Entity.User_follow_group_rel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserFollowGroupRelMapper extends BaseMapper<User_follow_group_rel> {
    @Select("SELECT * FROM user_follow f WHERE NOT EXISTS " +
            "(SELECT 1 FROM user_follow_group_rel r WHERE r.follow_id = f.follow_recode_id)")
    List<User_follow> selectUnclassifiedFollows();
}
