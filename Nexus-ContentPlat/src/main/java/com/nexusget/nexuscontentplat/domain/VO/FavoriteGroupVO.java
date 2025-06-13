package com.nexusget.nexuscontentplat.domain.VO;

import lombok.Data;

/**
 * 收藏分组视图对象
 */
@Data
public class FavoriteGroupVO {
    private Long groupId;
    private String groupName;
    private Integer itemCount;
    private String coverImage; // 第一篇文章的封面图
}