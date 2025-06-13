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
 * 评论实体类
 */

@Data
@TableName("comment")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class comment extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long commentId;
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 自定义文章id，暴露返回
     */
    private String custom_article_id;
    /**
     * 评论根id，用于嵌套回复
     */
    private Long root_id;
    /**
     * 评论父id，null表示一级评论
     */
    private Long parent_id;
    /**
     * 回复目标用户ID
     */
    @TableField("reply_to_user_id")
    private Long reply_id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 发布时间
     */
    @TableField("created_at")
    private String createdAt;
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private String updatedAt;
}
