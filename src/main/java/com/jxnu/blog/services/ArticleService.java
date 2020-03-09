package com.jxnu.blog.services;

import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.Vo.ArticleVo;
import com.jxnu.blog.dto.articleDto;
import com.jxnu.blog.pojo.article;

import java.util.List;

public interface ArticleService {
    ServerResponse<Integer> saveAll(String username, articleDto articleDto);
    ServerResponse<List<article>> getAllSave(String username);
    ServerResponse<article> getSave(String username,int id);
    ServerResponse<article> update(String username,articleDto articleDto);
    ServerResponse<String> deleteOne(String username,int id);
    ServerResponse<Integer> publish(String username,articleDto articleDto);
    ServerResponse<String> publish2(String username,articleDto articleDto);
    ServerResponse<List<ArticleVo>> userArticle(Integer userId);
    ServerResponse<Integer> getArticleCount(Integer id);
    ServerResponse<List<ArticleVo>> getArticleByDetailOrTitle(String dt);
    ServerResponse isPublish(Integer articleId);
    ServerResponse cancelPublish(String username,Integer articleId);
    ServerResponse<List<ArticleVo>> userArticleHot(Integer userId);
    ServerResponse<List<ArticleVo>> randomArticle();
}
