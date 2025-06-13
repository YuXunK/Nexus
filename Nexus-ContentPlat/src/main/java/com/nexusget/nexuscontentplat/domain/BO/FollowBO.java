package com.nexusget.nexuscontentplat.domain.BO;

import com.nexusget.nexuscontentplat.common.excption.BusinessException;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 关注业务对象
 */
@Data
public class FollowBO {
    private Long followerId;
    private Long followeeId;
    private LocalDateTime createTime;

    // 业务校验
    public void validate() {
        if (followerId.equals(followeeId)) {
            throw new BusinessException("不能关注自己");
        }
    }
}