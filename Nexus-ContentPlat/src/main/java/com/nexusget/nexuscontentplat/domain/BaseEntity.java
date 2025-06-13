package com.nexusget.nexuscontentplat.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类（所有实体类继承此类）
 */
@Data
public abstract class BaseEntity implements Serializable {

    /**
     * 创建时间（数据库自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间（数据库自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除标记（0-未删除 1-已删除）
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}