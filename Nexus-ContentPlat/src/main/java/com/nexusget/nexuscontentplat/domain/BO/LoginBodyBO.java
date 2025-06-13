package com.nexusget.nexuscontentplat.domain.BO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author nexus
 * @descrpition 用户登录对象
 */

@Data
public class LoginBodyBO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ClientID
     */
    @NotBlank(message = "客户端ID为空")
    @Pattern(regexp = "^[a-zA-Z0-9-_.]+$", message = "客户端ID格式错误")
    private String clientId;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一识别码
     */
    private String uuid;
}
