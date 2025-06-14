package com.nexusget.nexuscontentplat.service;

import com.nexusget.nexuscontentplat.domain.Entity.User;
import io.jsonwebtoken.io.IOException;
import jakarta.security.auth.message.AuthException;
import org.springframework.stereotype.Service;


/**
 * @author nexus 25-6-12 1:43
 * @descrpition 授权验证
 */
@Service
public interface AccessService {
    /**
     * @return token
     * @author nexus 25-6-12 1:43
     * @descrpition 授权--注册登录操作后续联动操作负责生成有效token
     */
    String SysAccess(User user) throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 1:43
     * @descrpition token有效性验证--系统补偿，避免网络波动等意外情况token的违规使用
     */
    String validateAndRenewToken(String token) throws AuthException;
}
