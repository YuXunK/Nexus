package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("user_follow_group_rel")
@AllArgsConstructor
@NoArgsConstructor
public class user_follow_group_rel {
    @TableId(type = IdType.AUTO)
    private Long follow_rel_id;
    private Long group_id;
    private Long follow_id;
    private Integer sort;
}
