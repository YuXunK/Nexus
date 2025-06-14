package com.nexusget.nexuscontentplat.service;

import com.nexusget.nexuscontentplat.domain.BO.FollowBO;
import com.nexusget.nexuscontentplat.domain.BO.PasswordLoginBodyBO;
import com.nexusget.nexuscontentplat.domain.BO.RegisterBodyBO;
import com.nexusget.nexuscontentplat.domain.BO.UserBO;
import com.nexusget.nexuscontentplat.domain.VO.UserVO;
import jakarta.security.auth.message.AuthException;
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
    UserVO register(RegisterBodyBO registerInfo) throws RuntimeException, InterruptedException;

    /**
     * @return UserVO 用户视图对象
     * @author nexus 25-6-12 1:44
     * @descrpition 登录
     */
    UserVO login(PasswordLoginBodyBO login) throws RuntimeException, InterruptedException;

    /**
     * @author nexus 25-6-12 1:44
     * @descrpition 关注 -- pay or not pay attention to sb. == PNO
     */
    boolean PNOAttention(FollowBO followEvent) throws RuntimeException;

    /**
     * @author nexus 25-6-12 1:44
     * @descrpition 退出账号
     */
    void logout(String token) throws RuntimeException, AuthException;

    /**
     * @author nexus 25-6-12 1:44
     * @descrpition 注销
     */
    void cancelAccount(String token) throws RuntimeException;

    /**
     * @return 用户信息视图对象
     * @author nexus 25-6-12 2:36
     * @descrpition 用户信息
     */
    UserVO userMateInfo(String token) throws RuntimeException;

    /**
     * @return 是否修改成功
     * @author nexus 25-6-12 2:18
     * @descrpition 社交或个人信息设定
     */
    boolean socialOrSelfInfoDIY(UserBO infoChange, String token) throws RuntimeException;

    /**
     * @author nexus 25-6-12 2:18
     * @descrpition 文章，关注博主分类初始化 COA == classify of account init
     */
    void COA() throws RuntimeException;

    /**
     * @author nexus 25-6-15 0:36
     * @descrpition 用户手动创建自定义分组
     * @param groupType 0 --表示这是关于关注用户的分组， 1 --表示为文章相关分组
     */
    boolean createManualGroup(String token, String groupName, boolean groupType) throws RuntimeException;

    /**
     * @author nexus 25-6-15 0:38
     * @descrpition 用户调整分组内容所属
     */
    void changeContentGroup(String token,Long contentId, Long newGroupId, boolean groupType) throws RuntimeException;
}
