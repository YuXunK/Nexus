package com.nexusget.nexuscontentplat.domain.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author nexus 25-6-12 4:36
 * @descrpition 登录授权验证信息
 */

@Data
public class userAccessVO {
    /**
     * 原token令牌
     */
    @JsonProperty("access_token")
    private String access_token;

    /**
     * 新token令牌
     */
    @JsonProperty("refresh_token")
    private String refresh_token;

    /**
     * 过期时间
     */
    @JsonProperty("expire_time")
    private long expire_time;

    /**
     * 令牌类型（固定为Bearer）
     */
    @JsonProperty("token_type")
    private final String tokenType = "Bearer";
}
