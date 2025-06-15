package com.nexusget.nexuscontentplat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nexusget.nexuscontentplat.domain.BO.DraftBO;
import com.nexusget.nexuscontentplat.domain.VO.ArticleDetailVO;
import com.nexusget.nexuscontentplat.domain.VO.ArticleListVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

/**
 * @author nexus 25-6-12 2:19
 * @descrpition 文章模块
 */

@Service
public interface ArticleService {
    /**
     * @author nexus 25-6-12 2:20
     * @descrpition 添加草稿
     */
    boolean draftForSelf(String token, DraftBO draftBO) throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:22
     * @descrpition 发布上架文章
     */
    boolean groundingArticle(String token, String draftId) throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:22
     * @descrpition 收藏文章 like or not like collection article
     */
    boolean collectArticle(String token, String articleId) throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:24
     * @descrpition 删除撤回文章
     */
    boolean cancelOrUngroundingArticle(String token, String articleId) throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:32
     * @descrpition 文章评论
     */
    boolean commentArticle(String token, List<String> targetId, String commentContent) throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:32
     * @descrpition 文章分类显示列表
     */
    Page<ArticleListVO> classifyArticleList(String category, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:32
     * @descrpition 文章详情内容
     */
    ArticleDetailVO articleMetadata(String customArticleId) throws IOException, InterruptedException;
}
