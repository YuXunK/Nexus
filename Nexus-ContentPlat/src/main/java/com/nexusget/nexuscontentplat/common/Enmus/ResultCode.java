package com.nexusget.nexuscontentplat.common.Enmus;

import lombok.Getter;

/**
 * 业务状态码枚举（HTTP状态码+自定义码）
 */
@Getter
public enum ResultCode {
    /* 成功状态码 */
    SUCCESS(200, "操作成功"),

    /* 客户端错误 */
    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    /* 服务端错误 */
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),

    /* 自定义业务错误码（6xxxx）*/
    USERNAME_EXISTS(60001, "用户名已存在"),
    ARTICLE_NOT_PUBLISHED(60002, "文章未发布");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
