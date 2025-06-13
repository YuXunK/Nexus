package com.nexusget.nexuscontentplat.domain.VO;

import lombok.Data;

import java.util.List;

@Data
public class userHistory {
    private Long user_id;
    private List<ArticleListVO> articleList;
}
