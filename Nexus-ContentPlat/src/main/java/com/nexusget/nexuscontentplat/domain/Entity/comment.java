package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nexus
 * 评论实体类
 */

@Data
@TableName("comment")
@AllArgsConstructor
@NoArgsConstructor
public class comment {
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
    private String createdAt;
    /**
     * 更新时间
     */
    private String updatedAt;
}
