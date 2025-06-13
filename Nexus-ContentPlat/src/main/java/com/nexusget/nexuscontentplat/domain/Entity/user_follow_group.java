package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("user_follow_group")
@AllArgsConstructor
@NoArgsConstructor
public class user_follow_group {
    @TableId(type = IdType.AUTO)
    private Long follow_gp_id;
    private Long user_id;
    @TableField("name")
    private String group_name;
    private Integer is_default;
}
