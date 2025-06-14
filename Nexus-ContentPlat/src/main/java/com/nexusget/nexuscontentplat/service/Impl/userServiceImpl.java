package com.nexusget.nexuscontentplat.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusget.nexuscontentplat.common.Utils.JwtProvider;
import com.nexusget.nexuscontentplat.common.Utils.MapstructUtils;
import com.nexusget.nexuscontentplat.common.Utils.StringUtils;
import com.nexusget.nexuscontentplat.common.core.BaseGroupEntity;
import com.nexusget.nexuscontentplat.common.excption.AuthException;
import com.nexusget.nexuscontentplat.common.excption.BusinessException;
import com.nexusget.nexuscontentplat.common.excption.UserException;
import com.nexusget.nexuscontentplat.domain.BO.FollowBO;
import com.nexusget.nexuscontentplat.domain.BO.PasswordLoginBodyBO;
import com.nexusget.nexuscontentplat.domain.BO.RegisterBodyBO;
import com.nexusget.nexuscontentplat.domain.BO.UserBO;
import com.nexusget.nexuscontentplat.domain.Entity.*;
import com.nexusget.nexuscontentplat.domain.VO.UserVO;
import com.nexusget.nexuscontentplat.mapper.*;
import com.nexusget.nexuscontentplat.service.AccessService;
import com.nexusget.nexuscontentplat.service.userService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class userServiceImpl implements userService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final AccessService accessService;
    private final UserFollowMapper userFollowMapper;
    private final RedisTemplate<String,String> redisTemplate;
    private final UserFollowGroupMapper followGroupMapper;
    private final UserFollowGroupRelMapper followGroupRelMapper;
    private final UserTagGroupMapper tagGroupMapper;
    private final UserTagRelationMapper tagRelationMapper;
    private final UserLikeArticleMapper likeArticleMapper;


    public userServiceImpl(UserMapper userMapper, JwtProvider jwtProvider, RedisTemplate<String,String> redisTemplate, UserFollowMapper userFollowMapper, AccessService accessService, UserFollowGroupMapper followGroupMapper, UserFollowGroupRelMapper followGroupRelMapper, UserTagGroupMapper tagGroupMapper, UserTagRelationMapper tagRelationMapper, UserLikeArticleMapper likeArticleMapper) {
        this.userMapper = userMapper;
        this.jwtProvider = jwtProvider;
        this.redisTemplate = redisTemplate;
        this.userFollowMapper = userFollowMapper;
        this.accessService = accessService;
        this.followGroupMapper = followGroupMapper;
        this.followGroupRelMapper = followGroupRelMapper;
        this.tagGroupMapper = tagGroupMapper;
        this.tagRelationMapper = tagRelationMapper;
        this.likeArticleMapper = likeArticleMapper;
    }


    @Override
    public UserVO register(RegisterBodyBO registerInfo) throws RuntimeException, InterruptedException {
        String email = registerInfo.getEmail();
        boolean isExist = userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (isExist) {
            throw new UserException("user.register.save.error", email);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .userName(registerInfo.getUsername())
                .email(email)
                .password(encoder.encode(registerInfo.getPassword()))
                .build();

        boolean isRegister = userMapper.insert(user) > 0;
        if (!isRegister) {
            throw new RuntimeException("用户注册失败");
        }

        String token = accessService.SysAccess(user);
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("生成 Token 失败");
        }

        UserVO userVO = MapstructUtils.convert(user, UserVO.class);
        if (userVO != null) {
            userVO.setToken(token); // 必定有值
        }

        return userVO;
    }
    /**
     * @return userVO 用户登录视图对象
     * @descrpition 暂时先使用密码登录就行
     */
    @Override
    public UserVO login(PasswordLoginBodyBO login) throws RuntimeException, InterruptedException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 验证账号密码是否正确
        boolean accountCheck = userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getEmail,login.getAccount()).eq(User::getPassword,encoder.encode(login.getPassword())));
        if (!accountCheck) {
            throw new UserException("user.login.error", login.getAccount());
        }
        User user = MapstructUtils.convert(login, User.class);
        @SuppressWarnings("unchecke")
        String token = accessService.SysAccess(user);
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("生成 Token 失败");
        }

        UserVO userVO = MapstructUtils.convert(user, UserVO.class);
        if (userVO != null) {
            userVO.setToken(token); // 必定有值
        }

        return userVO;
    }

    @Override
    public boolean PNOAttention(FollowBO followEvent) throws RuntimeException {
        // 1. 检查是否已存在关注关系
        LambdaQueryWrapper<User_follow> queryWrapper = new LambdaQueryWrapper<User_follow>()
                .eq(User_follow::getUser_id, followEvent.getUserId())
                .eq(User_follow::getFollowee_id, followEvent.getFolloweeId());

        User_follow existingFollow = userFollowMapper.selectOne(queryWrapper);

        // 2. 存在则取关（删除），不存在则关注（新增）
        if (existingFollow != null) {
            // 取关：通过查询到的记录主键id删除
            return userFollowMapper.deleteById(existingFollow.getFollow_recode_id()) > 0;
        } else {
            // 关注：新增记录
            User_follow newFollow = MapstructUtils.convert(followEvent, User_follow.class);
            return userFollowMapper.insert(newFollow) > 0;
        }
    }

    @Override
    public void logout(String token) throws AuthException {
        Long userId = paresTokenToGetId(token);

        // 3. 删除Redis中的Token记录
        String redisKey = "user:token:" + userId;
        String storedToken = redisTemplate.opsForValue().get(redisKey);

        // 3.1 验证当前Token是否与存储的一致
        if (token.equals(storedToken)) {
            redisTemplate.delete(redisKey);
        } else {
            throw new AuthException("AUTH-403", "Token已失效或不属于该用户");
        }

        // 4. （可选）记录登出日志
        log.info("用户登出成功，userId: {}", userId);
    }

    @Override
    public void cancelAccount(String token) throws RuntimeException {
        Long userId = paresTokenToGetId(token);
        // 删除用户相关内容
        userMapper.deleteById(userId);
    }

    @Override
    public UserVO userMateInfo(String token) throws RuntimeException {
        Long userId = paresTokenToGetId(token);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId,userId);

        User user = userMapper.selectOne(queryWrapper);
        return MapstructUtils.convert(user,UserVO.class);
    }

    @Override
    public boolean socialOrSelfInfoDIY(UserBO infoChange, String token) throws RuntimeException {
        Long userId = paresTokenToGetId(token);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUserId,userId);

        User userChange = MapstructUtils.convert(infoChange,User.class);

        return userMapper.update(userChange,updateWrapper) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void COA() throws RuntimeException {
        // 1. 确保每个用户有默认关注分组
        ensureDefaultFollowGroups();

        // 2. 同步关注记录到默认分组（如果未分组）
        syncUnclassifiedFollows();

        // 3. 确保每个用户有默认文章标签分组
        ensureDefaultArticleTagGroups();

        // 4. 同步未分类的喜欢文章到默认标签组
        syncUnclassifiedLikedArticles();
    }

    // ===== 私有方法 =====

    /**
     * 为所有用户创建默认关注分组
     */
    private void ensureDefaultFollowGroups() {
        List<Long> userIds = followGroupMapper.selectDistinctUserIds();
        userIds.forEach(userId -> {
            if (!followGroupMapper.existsDefaultGroup(userId)) {
                User_follow_group group = new User_follow_group();
                group.setUser_id(userId);
                group.setGroup_name("默认关注");
                group.setIs_default(1);
                followGroupMapper.insert(group);
            }
        });
    }

    /**
     * 将未分组的关注记录放入默认分组
     */
    private void syncUnclassifiedFollows() {
        List<User_follow> unclassifiedFollows = followGroupRelMapper.selectUnclassifiedFollows();
        unclassifiedFollows.forEach(follow -> {
            Long defaultGroupId = followGroupMapper.selectDefaultGroupId(follow.getUser_id());

            User_follow_group_rel rel = new User_follow_group_rel();
            rel.setGroup_id(defaultGroupId);
            rel.setFollow_id(follow.getFollow_recode_id());
            rel.setSort(0); // 默认排序

            followGroupRelMapper.insert(rel);
        });
    }

    /**
     * 为所有用户创建默认文章标签分组
     */
    private void ensureDefaultArticleTagGroups() {
        List<Long> userIds = tagGroupMapper.selectDistinctUserIds();
        userIds.forEach(userId -> {
            if (!tagGroupMapper.existsDefaultGroup(userId)) {
                User_tag_group group = new User_tag_group();
                group.setUser_id(userId);
                group.setGroup_name("未分类");
                group.setSort(0);
                tagGroupMapper.insert(group);
            }
        });
    }
    /**
     * 将未分类的喜欢文章关联到默认标签组
     */
    private void syncUnclassifiedLikedArticles() {
        // 1. 从likeArticleMapper直接查询有效喜欢记录
        List<User_like_article> validLikes = likeArticleMapper.selectActiveLikes();

        // 2. 筛选未分类的记录
        List<User_like_article> unclassifiedArticles = validLikes.stream()
                .filter(article -> !tagRelationMapper.existsRelation(
                        article.getUser_id(),
                        article.getArticle_id()
                ))
                .toList();

        // 3. 关联到默认分组
        unclassifiedArticles.forEach(article -> {
            Long defaultGroupId = tagGroupMapper.selectDefaultGroupId(article.getUser_id());

            User_tag_relation relation = new User_tag_relation();
            relation.setUser_id(article.getUser_id());
            relation.setGroup_id(defaultGroupId);
            relation.setTag_id(article.getArticle_id());
            relation.setCreate_time(LocalDateTime.now().toString());

            tagRelationMapper.insert(relation);
        });
    }

    @Override
    public boolean createManualGroup(String token, String groupName, boolean groupType) throws RuntimeException {
        // 1. 从Token解析用户ID
        Long userId = paresTokenToGetId(token);

        // 2. 验证分组名称有效性
        if (!StringUtils.hasText(groupName) || groupName.length() > 20) {
            throw new BusinessException("分组名称长度限制1-20字符");
        }

        // 3. 根据分组类型执行不同逻辑
        if (!groupType) { // 关注分组
            if (followGroupMapper.exists(userId, groupName)) {
                throw new BusinessException("该关注分组已存在");
            }

            User_follow_group group = new User_follow_group();
            group.setUser_id(userId);
            group.setGroup_name(groupName);
            group.setIs_default(0); // 手动分组标记
            return followGroupMapper.insert(group) > 0;

        } else { // 文章分组
            if (tagGroupMapper.exists(userId, groupName)) {
                throw new BusinessException("该文章分组已存在");
            }

            User_tag_group group = new User_tag_group();
            group.setUser_id(userId);
            group.setGroup_name(groupName);
            group.setSort(tagGroupMapper.selectNextSortValue(userId));
            return tagGroupMapper.insert(group) > 0;
        }
    }

    @Override
    @Transactional
    public void changeContentGroup(String token, Long contentId,
                                   Long newGroupId, boolean groupType) {
        // 1. 从Token解析用户ID
        Long userId = paresTokenToGetId(token);

        // 2. 验证新分组归属
        if (!groupType) {
            validateGroupOwnership(userId, newGroupId, followGroupMapper);
        } else {
            validateGroupOwnership(userId, newGroupId, tagGroupMapper);
        }

        // 3. 执行分组变更
        if (!groupType) { // 关注分组调整
            LambdaUpdateWrapper<User_follow_group_rel> updateWrapper =
                    new LambdaUpdateWrapper<User_follow_group_rel>()
                            .eq(User_follow_group_rel::getFollow_id, contentId)
                            .set(User_follow_group_rel::getGroup_id, newGroupId);
            followGroupRelMapper.update(null, updateWrapper);
        } else { // 文章分组调整
            LambdaUpdateWrapper<User_tag_relation> updateWrapper =
                    new LambdaUpdateWrapper<User_tag_relation>()
                            .eq(User_tag_relation::getTag_id, contentId)
                            .set(User_tag_relation::getGroup_id, newGroupId);
            tagRelationMapper.update(null, updateWrapper);
        }
    }

    /**
     * 验证分组是否属于当前用户
     */
    private <T> void validateGroupOwnership(Long userId, Long groupId,
                                            BaseMapper<T> groupMapper) {
        Object group = groupMapper.selectById(groupId);
        if (group == null || !((BaseGroupEntity)group).getUser_id().equals(userId)) {
            throw new BusinessException("目标分组不存在或无权操作");
        }
    }

    public Long paresTokenToGetId(String token) throws RuntimeException {
        // 1. 校验Token格式
        if (StringUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            throw new AuthException("AUTH-400", "Token格式错误");
        }

        // 2. 解析Token获取用户ID
        Claims claims;
        try {
            claims = jwtProvider.parseToken(token);
        } catch (Exception e) {
            throw new AuthException("AUTH-401", "Token解析失败");
        }
        return Long.parseLong(claims.getSubject());
    }
}
