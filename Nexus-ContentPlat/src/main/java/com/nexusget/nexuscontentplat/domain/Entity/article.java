package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nexus
 * 文章实体类
 */

@Data
@TableName("article")
@AllArgsConstructor
@NoArgsConstructor
public class article {
    @TableId(type = IdType.AUTO)
    private Long articleId;
    /**
     * 发布用户id
     */
    private Long userId;
    /**
     * 自定义文章id，暴露返回
     */
    private String custom_article_id;
    /**
     * 标题
     */
    private String title;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 内容摘要，用于简介
     */
    private String excerpt;
    /**
     * 类型id
     */
    private Long categoryId;
    /**
     * 标签id合集
     */
    private String[] tagIds;
    /**
     * 创建时间
     */
    private String createdAt;
    /**
     * 更新时间
     */
    private String updatedAt;
}
