package com.nexusget.nexuscontentplat.domain.BO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import static com.nexusget.nexuscontentplat.common.Enmus.UserConstants.*;

/**
 * @author nexus
 * @descrpition 注册对象
 */

@Data
public class RegisterBodyBO {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = "用户名长度异常")
    private String username;

    /**
     * 绑定邮箱
     */
    @NotBlank(message = "关联邮箱不能为空")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "密码长度异常")
    private String password;

    /**
     * 邀请码
     */
    private Integer isInvited;
}
