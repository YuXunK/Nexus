package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nexusget.nexuscontentplat.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@TableName("user_follow")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class user_follow extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long follow_recode_id;
    private Long user_id;
    private Long followed_user_id;
    private String created_at;
}
