package com.nexusget.nexuscontentplat.common.excption;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    // 获取错误码
    private final String errorCode;

  // 支持错误码+消息的构造方法
  public AuthException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
