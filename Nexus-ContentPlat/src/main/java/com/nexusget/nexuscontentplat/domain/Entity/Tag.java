package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nexus
 * @ date 25/6/6
 * 标签类实体
 */

@Data
@TableName("tag")
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @TableId(type = IdType.AUTO)
    @TableField("tag_id")
    private Long tagId;
    /**
     * 标签名
     */
    private String tagName;
}
