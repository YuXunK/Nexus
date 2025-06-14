package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nexusget.nexuscontentplat.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author nexus
 * @date "2023-06-25 14:30:00"
 * 用户实体类
 */

@Data
@Builder
@TableName("user")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @TableId(type = IdType.AUTO)
    @TableField("user_id")
    private Long userId;
    /**
     * 用户名
     */
    @NotNull
    private String userName;
    /**
     * 头像图片
     */
    @TableField("avatar_url")
    private String avatar;
    /**
     * 个人主页介绍
     */
    private String self_Introduce;
    /**
     * 关联邮箱
     */
    private String email;
    /**
     * 关联手机
     */
    private String phone;
    /**
     * 密码
     */
    @TableField("password_hash")
    private String password;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private String createdAt;
    /**
     * 最后登录日期
     */
    @TableField("last_login_at")
    private String lastLoginAt;
    /**
     * 管理员标识 0--super 1--admin 2--normal
     */
    @TableField("is_admin")
    private int isAdmin;
}
