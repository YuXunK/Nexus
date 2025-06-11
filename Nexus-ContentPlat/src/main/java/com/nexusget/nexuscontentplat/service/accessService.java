package com.nexusget.nexuscontentplat.service;

import io.jsonwebtoken.io.IOException;
import org.springframework.stereotype.Service;


/**
 * @author nexus 25-6-12 1:43
 * @descrpition 授权验证
 */
@Service
public interface accessService {
    /**
     * @author nexus 25-6-12 1:43
     * @descrpition 授权--注册登录操作后续联动操作
     */
    void SysAccess() throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 1:43
     * @descrpition token有效性验证--系统补偿，避免网络波动等意外情况token的违规使用
     */
    void getAccessToken() throws IOException, InterruptedException;
}
