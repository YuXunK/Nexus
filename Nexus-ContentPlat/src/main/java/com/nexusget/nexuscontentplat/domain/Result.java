package com.nexusget.nexuscontentplat.domain;

import com.nexusget.nexuscontentplat.common.Enmus.ResultCode;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author neuxs 25-6-13 4:22
 * @descrpition 同一响应结果设计POJO
 */
@Data
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private T data;
    private Long timestamp = System.currentTimeMillis();

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        return createResult(ResultCode.SUCCESS, data);
    }

    // 失败响应（使用枚举）
    public static <T> Result<T> error(ResultCode resultCode) {
        return createResult(resultCode, null);
    }

    // 失败响应（自定义消息）
    public static <T> Result<T> error(ResultCode resultCode, String customMsg) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMsg(customMsg != null ? customMsg : resultCode.getMsg());
        return result;
    }

    private static <T> Result<T> createResult(ResultCode resultCode, T data) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMsg());
        result.setData(data);
        return result;
    }
}
