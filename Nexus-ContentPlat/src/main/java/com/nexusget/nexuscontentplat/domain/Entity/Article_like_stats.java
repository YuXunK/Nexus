package com.nexusget.nexuscontentplat.domain.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("article_like_stats")
@AllArgsConstructor
@NoArgsConstructor
public class Article_like_stats {
    private String article_id;
    private Integer like_count;
    private String last_updated_time;
}
