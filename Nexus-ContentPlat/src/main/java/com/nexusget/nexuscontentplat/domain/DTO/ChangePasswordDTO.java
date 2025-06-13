package com.nexusget.nexuscontentplat.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 密码修改参数
 */
@Data
public class ChangePasswordDTO {
    @NotBlank
    @Pattern(regexp = "^\\$2[ayb]\\$.{56}$") // 验证是否为BCrypt格式
    private String oldPassword;

    @NotBlank
    @Length(min = 8, max = 50)
    private String newPassword;
}
