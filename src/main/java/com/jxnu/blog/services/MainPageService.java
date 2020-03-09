package com.jxnu.blog.services;

import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.Vo.ArticleVo;

import java.util.List;

public interface MainPageService {
    ServerResponse<List<ArticleVo>> getArticles();
    ServerResponse<ArticleVo> getArticle(int id);
    ArticleVo getArticleVo(String key,int view);
}
