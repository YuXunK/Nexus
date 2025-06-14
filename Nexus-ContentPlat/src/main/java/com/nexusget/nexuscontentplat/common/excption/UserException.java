package com.nexusget.nexuscontentplat.common.excption;

import java.io.Serial;

public class UserException extends BaseException {
  @Serial
  private static final long serialVersionUID = 1L;

  public UserException(String code, Object... args) {
    super("user", code, args, null);
  }
}
