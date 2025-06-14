package com.nexusget.nexuscontentplat.domain.BO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import static com.nexusget.nexuscontentplat.common.Enmus.UserConstants.*;

/**
 * @author nexus
 * @descrpition 密码登录对象
 */

@Data
public class PasswordLoginBodyBO {

    /**
     * 手机号或邮箱
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "}$",
            message = "密码需包含字母、数字和特殊字符"
    )
    @Length(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "密码长度异常")
    private String password;

    //TODO 暂时不用code字段因此没加NotBlank校验，后期再加图像验证码或邮箱验证码登录
    private String code;
}
