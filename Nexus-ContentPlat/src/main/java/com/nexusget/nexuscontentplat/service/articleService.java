package com.nexusget.nexuscontentplat.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author nexus 25-6-12 2:19
 * @descrpition 文章模块
 */

@Service
public interface articleService {
    /**
     * @author nexus 25-6-12 2:20
     * @descrpition 添加草稿
     */
    void draftForSelf() throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:22
     * @descrpition 发布上架文章
     */
    void groundingArticle() throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:22
     * @descrpition 收藏文章 collect or cancel collection article
     */
    void collectArticle() throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:24
     * @descrpition 删除撤回文章
     */
    void cancelOrUngroundingArticle() throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:32
     * @descrpition 文章评论
     */
    void commentArticle() throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:32
     * @descrpition 文章分类显示列表
     */
    void classifyArticleList() throws IOException, InterruptedException;

    /**
     * @author nexus 25-6-12 2:32
     * @descrpition 文章详情内容
     */
    void articleMetadata() throws IOException, InterruptedException;
}
