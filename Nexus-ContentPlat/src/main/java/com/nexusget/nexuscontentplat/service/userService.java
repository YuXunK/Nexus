package com.nexusget.nexuscontentplat.service;

import org.springframework.stereotype.Service;

/**
 * @author nexus 25-6-12 1:44
 * @descrpition 用户操作模块
 */
@Service
public interface userService {

    /**
     * @author nexus 25-6-12 1:44
     * @descrpition 注册
     */
    void register() throws RuntimeException;

    /**
     * @author nexus 25-6-12 1:44
     * @descrpition 登录
     */
    void login() throws RuntimeException;

    /**
     * @author nexus 25-6-12 1:44
     * @descrpition 关注 -- pay or not pay attention to sb. == PNO
     */
    void PNOAttention() throws RuntimeException;

    /**
     * @author nexus 25-6-12 1:44
     * @descrpition 退出账号
     */
    void logout() throws RuntimeException;

    /**
     * @author nexus 25-6-12 1:44
     * @descrpition 注销
     */
    void cancelAccount() throws RuntimeException;

    /**
     * @author nexus 25-6-12 2:36
     * @descrpition 用户信息
     */
    void userMateInfo() throws RuntimeException;

    /**
     * @author nexus 25-6-12 2:18
     * @descrpition 社交或个人信息设定
     */
    void socialOrSelfInfoDIY() throws RuntimeException;

    /**
     * @author nexus 25-6-12 2:18
     * @descrpition 文章，关注博主分类 COA == classify of account
     */
    void COA() throws RuntimeException;
}
