package com.nexusget.nexuscontentplat.domain.BO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import static com.nexusget.nexuscontentplat.common.Enmus.UserConstants.*;
import static com.nexusget.nexuscontentplat.common.Enmus.UserConstants.PASSWORD_MAX_LENGTH;

/**
 * @author nexus
 * @descrpition 注册对象
 */

@Data
@EqualsAndHashCode(callSuper=true)
public class RegisterBodyBO extends LoginBodyBO {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = "用户名长度异常")
    private String username;

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
