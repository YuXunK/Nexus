package com.nexusget.nexuscontentplat.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nexusget.nexuscontentplat.common.Utils.DateUtils;
import com.nexusget.nexuscontentplat.common.Utils.JwtProvider;
import com.nexusget.nexuscontentplat.common.Utils.MapstructUtils;
import com.nexusget.nexuscontentplat.common.Utils.StringUtils;
import com.nexusget.nexuscontentplat.common.excption.BusinessException;
import com.nexusget.nexuscontentplat.common.excption.UserException;
import com.nexusget.nexuscontentplat.domain.BO.ArticleDetailBO;
import com.nexusget.nexuscontentplat.domain.BO.DraftBO;
import com.nexusget.nexuscontentplat.domain.Entity.*;
import com.nexusget.nexuscontentplat.domain.VO.ArticleDetailVO;
import com.nexusget.nexuscontentplat.domain.VO.ArticleListVO;
import com.nexusget.nexuscontentplat.mapper.*;
import com.nexusget.nexuscontentplat.service.ArticleService;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArticleServiceImpl implements ArticleService {
    private final UserMapper userMapper;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;
    private final CategoryMapper categoryMapper;
    private final UserLikeArticleMapper userLikeArticleMapper;
    private final ArticleLikeStatsMapper articleLikeStatsMapper;
    private final JwtProvider jwtProvider;

    public ArticleServiceImpl(UserMapper userMapper, ArticleMapper articleMapper, CommentMapper commentMapper, CategoryMapper categoryMapper, UserLikeArticleMapper userLikeArticleMapper, ArticleLikeStatsMapper articleLikeStatsMapper, JwtProvider jwtProvider) {
        this.userMapper = userMapper;
        this.articleMapper = articleMapper;
        this.commentMapper = commentMapper;
        this.categoryMapper = categoryMapper;
        this.userLikeArticleMapper = userLikeArticleMapper;
        this.articleLikeStatsMapper = articleLikeStatsMapper;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean draftForSelf(String token, DraftBO draftBO) {
        Long userId = jwtProvider.paresTokenToGetId(token);
        boolean isExits = userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getUserId, userId));
        if (!isExits) {
            throw new UserException("user.addDraft.error", userId);
        }
        Article article = MapstructUtils.convert(draftBO, Article.class);
        return articleMapper.insert(article) > 0;
    }

    @Override
    public boolean groundingArticle(String token, String DraftId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getUserId, jwtProvider.paresTokenToGetId(token))
                .eq(Article::getCustom_article_id, DraftId);
        Article targetDraft = articleMapper.selectOne(queryWrapper);
        if (targetDraft == null) {
            throw new UserException("user.groundingDraft.error", DraftId);
        }
        /*
          将对象草稿状态修改为上架
         */
        targetDraft.setIsDraft(1);
        return articleMapper.updateById(targetDraft) > 0;
    }

    @Override
    public boolean collectArticle(String token, String articleId) {
        Long userId = jwtProvider.paresTokenToGetId(token);
        User_like_article userLikeArticle = new User_like_article();
        userLikeArticle.setUser_id(userId);
        userLikeArticle.setArticle_id(Long.valueOf(articleId));
        return userLikeArticleMapper.insert(userLikeArticle) > 0;
    }

    @Override
    public boolean cancelOrUngroundingArticle(String token, String articleId) {
        // 查找对象是否存在
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getUserId, jwtProvider.paresTokenToGetId(token))
                .eq(Article::getCustom_article_id, articleId);
        Article targetDraft = articleMapper.selectOne(queryWrapper);
        // 判断对象文章是否为草稿以决定接下来的操作
        if (targetDraft.getIsDraft() == 0) {
            return userMapper.deleteById(targetDraft.getUserId()) > 0;
        }
        targetDraft.setIsDraft(0);
        return articleMapper.updateById(targetDraft) > 0;
    }

    @Override
    public boolean commentArticle(String token, List<String> target, String commentContent) {
        if (target == null || target.isEmpty() || target.size() > 2) {
            throw new BusinessException("入参错误：target需包含[文章id]或[文章id,父评论id]");
        }
        Long userId = jwtProvider.paresTokenToGetId(token);
        String articleId = target.get(0);
        boolean isExits = articleMapper.exists(new LambdaQueryWrapper<Article>().eq(Article::getCustom_article_id, articleId));
        if (!isExits) {
            throw new UserException("user.commentArticle.error:评论的文章不存在", userId);
        }
        Comment comment = new Comment();
        comment.setContent(commentContent);
        comment.setUserId(userId);
        comment.setCustom_article_id(target.get(0));
        if (target.get(1) == null || StringUtils.isEmpty(target.get(1))) {
            comment.setParent_id(null);
            commentMapper.insert(comment);
        }
        return commentMapper.insert(comment) > 0;
    }

    @Override
    public Page<ArticleListVO> classifyArticleList(String category,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        // 1. 转换分类标识
        Long categoryId = resolveCategoryId(category);

        // 2. 分页查询文章基础信息（使用MyBatis-Plus的Page）
        Page<Article> articlePage = articleMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getCategoryId, categoryId)
                        .eq(Article::getIsDraft, 0)
                        .orderByDesc(Article::getCreatedAt)
                        .select(Article::getCustom_article_id,
                                Article::getTitle,
                                Article::getExcerpt,
                                Article::getExcerpt_img,
                                Article::getCreatedAt,
                                Article::getUserId)
        );

        // 3. 无数据时直接返回空分页结果
        if (CollectionUtils.isEmpty(articlePage.getRecords())) {
            return new Page<>(page, size);
        }

        // 4. 批量获取关联数据（用户信息、点赞数）
        Set<Long> userIds = articlePage.getRecords().stream()
                .map(Article::getUserId)
                .collect(Collectors.toSet());
        Set<String> articleIds = articlePage.getRecords().stream()
                .map(Article::getCustom_article_id)
                .collect(Collectors.toSet());

        // 4.1 查询用户信息（Map<userId, User>）
        Map<Long, User> userMap = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .in(User::getUserId, userIds)
                        .select(User::getUserId, User::getUserName, User::getAvatar)
        ).stream().collect(Collectors.toMap(User::getUserId, Function.identity()));

        // 4.2 查询点赞数（Map<articleId, likeCount>）
        Map<String, Integer> likeCountMap = articleLikeStatsMapper.selectList(
                new LambdaQueryWrapper<Article_like_stats>()
                        .in(Article_like_stats::getCustom_article_id, articleIds)
                        .select(Article_like_stats::getCustom_article_id, Article_like_stats::getLike_count)
        ).stream().collect(Collectors.toMap(
                Article_like_stats::getCustom_article_id,
                Article_like_stats::getLike_count
        ));

        // 5. 转换为ArticleListVO列表
        List<ArticleListVO> voList = articlePage.getRecords().stream().map(article -> {
            ArticleListVO vo = new ArticleListVO();
            vo.setId(article.getCustom_article_id());
            vo.setTitle(article.getTitle());
            vo.setExcerpt(article.getExcerpt());
            vo.setCreateTime(DateUtils.format(LocalDateTime.parse(article.getCreatedAt())));

            // 设置作者信息
            User author = userMap.get(article.getUserId());
            if (author != null) {
                ArticleListVO.AuthorVO authorVO = new ArticleListVO.AuthorVO();
                authorVO.setUserId(author.getUserId());
                authorVO.setUsername(author.getUserName());
                authorVO.setAvatarUrl(author.getAvatar());
                vo.setAuthor(authorVO);
            }

            // 设置点赞数
            ArticleListVO.StatsVO statsVO = new ArticleListVO.StatsVO();
            statsVO.setLikeCount(likeCountMap.getOrDefault(article.getCustom_article_id(), 0));
            vo.setStats(statsVO);

            return vo;
        }).collect(Collectors.toList());

        // 6. 构造MyBatis-Plus的Page对象（携带分页信息）
        return new Page<ArticleListVO>(
                articlePage.getCurrent(),
                articlePage.getSize(),
                articlePage.getTotal()
        ).setRecords(voList);
    }

    @Override
    public ArticleDetailVO articleMetadata(String customArticleId) {
        // 1. 查询文章基础信息
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getCustom_article_id, customArticleId)
                .select(Article::getCustom_article_id, Article::getTitle, Article::getContent,
                        Article::getCreatedAt, Article::getUserId)
        );
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        // 2. 批量获取关联数据（避免N+1查询）
        // 2.1 查询作者信息
        User author = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserId, article.getUserId())
                .select(User::getUserId, User::getUserName, User::getAvatar)
        );

        // 2.2 查询点赞和收藏数
        Article_like_stats likeStats = articleLikeStatsMapper.selectOne(new LambdaQueryWrapper<Article_like_stats>()
                .eq(Article_like_stats::getCustom_article_id, customArticleId)
        );
        Integer likeCount = (likeStats != null) ? likeStats.getLike_count() : 0;

        // 2.3 查询评论列表（假设CommentMapper已实现）
        List<Comment> comments = commentMapper.selectList(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getCustom_article_id, customArticleId)
                .isNull(Comment::getParent_id) // 假设一级评论的parent_id为null
                .orderByDesc(Comment::getCreatedAt)
        );

        // 3. 构建BO
        ArticleDetailBO bo = new ArticleDetailBO();
        bo.setArticle_custom_id(article.getCustom_article_id());
        bo.setTitle(article.getTitle());
        bo.setContent(article.getContent());
        bo.setCreatedAt(LocalDateTime.parse(article.getCreatedAt()));

        // 设置作者信息
        ArticleDetailBO.UserBO authorBO = new ArticleDetailBO.UserBO();
        authorBO.setUserId(author.getUserId());
        authorBO.setUsername(author.getUserName());
        authorBO.setAvatarUrl(author.getAvatar());
        bo.setAuthor(authorBO);

        // 设置互动数据
        bo.setLikeCount(likeCount);

        // 设置评论列表
        List<ArticleDetailBO.CommentBO> commentBOs = comments.stream().map(comment -> {
            ArticleDetailBO.CommentBO commentBO = new ArticleDetailBO.CommentBO();
            commentBO.setCommentId(comment.getCommentId());
            commentBO.setContent(comment.getContent());
            commentBO.setCreateTime(LocalDateTime.parse(comment.getCreatedAt()));

            // 查询评论者信息
            User commenter = userMapper.selectById(comment.getUserId());
            ArticleDetailBO.UserBO commenterBO = new ArticleDetailBO.UserBO();
            commenterBO.setUserId(commenter.getUserId());
            commenterBO.setUsername(commenter.getUserName());
            commenterBO.setAvatarUrl(commenter.getAvatar());
            commentBO.setCommenter(commenterBO);

            return commentBO;
        }).collect(Collectors.toList());
        bo.setComments(commentBOs);

        // 4. 转换为VO
        return MapstructUtils.convert(bo, ArticleDetailVO.class);
    }

    private Long resolveCategoryId(String category) {
        try {
            return Long.parseLong(category); // 直接传入ID的情况
        } catch (NumberFormatException e) {
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getCategoryId, category);
            Long id = categoryMapper.selectOne(queryWrapper).getCategoryId();
            if (id == null) throw new BusinessException("分类不存在");
            return id;
        }
    }

}
