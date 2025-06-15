package com.nexusget.nexuscontentplat.domain.BO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DraftBO {
    @NotBlank
    private String title;
    private String content;      // 原始内容
    private Long categoryId;
    private Long[] tagIds;       // 需转换为逗号分隔字符串
    private String excerpt;      // 可选摘要
    private String excerptImg;   // 可选摘要图
    /**
     * 用于显示草稿最近编辑时间
     */
    private String updated_at;
}
