package com.nexusget.nexuscontentplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusget.nexuscontentplat.domain.Entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserMapper extends BaseMapper<User> {
    List<User> selectByIds(@Param("userIds") Set<Long> userIds);
}
