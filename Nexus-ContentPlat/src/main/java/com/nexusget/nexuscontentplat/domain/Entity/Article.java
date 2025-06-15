package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nexusget.nexuscontentplat.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author nexus
 * 文章实体类
 */

@Data
@TableName("article")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Article extends BaseEntity {
    @TableId(type = IdType.AUTO)
    @TableField("article_id")
    private Long articleId;
    /**
     * 发布用户id
     */
    @TableField("user_id")
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
     * 内容摘要预览图片，用于简介
     */
    private String excerpt_img;
    /**
     * 类型id
     */
    @TableField("category_id")
    private Long categoryId;
    /**
     * 标签id合集
     */
    @TableField("tag_ids")
    private String[] tagIds;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private String createdAt;
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private String updatedAt;

    /**
     * 是否为草稿 0--是 1--否
     */
    @TableField("is_draft")
    private Integer isDraft;
}
