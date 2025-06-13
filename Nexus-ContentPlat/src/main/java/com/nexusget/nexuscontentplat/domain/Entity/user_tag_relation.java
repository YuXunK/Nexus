package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("user_tag_relation")
@AllArgsConstructor
@NoArgsConstructor
public class user_tag_relation {
    @TableId(type = IdType.AUTO)
    private Long user_tag_rel_id;
    private Long user_id;
    private Long group_id;
    private Long tag_id;
    private String create_time;
}
