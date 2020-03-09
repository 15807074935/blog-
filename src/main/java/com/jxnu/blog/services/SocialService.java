package com.jxnu.blog.services;

import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.Vo.ArticleVo;
import com.jxnu.blog.Vo.commentVo;
import com.jxnu.blog.Vo.focusAndFansVo;
import com.jxnu.blog.pojo.article;
import com.jxnu.blog.pojo.user;

import java.util.List;

public interface SocialService {
    ServerResponse like(String username, Integer id);
    ServerResponse<String> dislike(String username, Integer id);
    ServerResponse<String> islike(String username,Integer articleId);
    ServerResponse upComment(String username,String comment,Integer articleId);
    ServerResponse<List<commentVo>> GetComment(Integer articleId);
    ServerResponse focus(String username,Integer id);
    ServerResponse unfocus(String username,Integer id);
    ServerResponse me(String username,Integer id);
    ServerResponse isfocus(String username,Integer id);
    ServerResponse focusArticle(String username);
    ServerResponse<focusAndFansVo> focusAndFans(Integer id);
    ArticleVo getArticleVo(article article);
    ServerResponse<List<user>> getFocus(Integer userId);
    ServerResponse<List<user>> getFans(Integer userId);
    ServerResponse<List<ArticleVo>> getfavArticle(String username);
}
