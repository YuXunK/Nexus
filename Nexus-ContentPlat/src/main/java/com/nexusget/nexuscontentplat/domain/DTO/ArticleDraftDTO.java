package com.nexusget.nexuscontentplat.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 文章草稿参数（富文本内容）
 */
@Data
public class ArticleDraftDTO {
    @NotBlank
    @Length(min = 5, max = 100)
    private String title;

    @NotBlank
    private String content; // HTML富文本

    @Size(max = 5)
    private List<Long> tagIds;

    private Boolean isPublish;
}
