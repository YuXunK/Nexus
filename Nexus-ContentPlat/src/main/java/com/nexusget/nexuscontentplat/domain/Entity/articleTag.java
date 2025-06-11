package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nexus
 * 文章与tag关联信息
 */

@Data
@TableName("article_tag")
@AllArgsConstructor
@NoArgsConstructor
public class articleTag {
    @TableId(type = IdType.AUTO)
    private Long articleTagId;
    private Long articleId;
    private Long tagId;
}
