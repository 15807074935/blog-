package com.jxnu.blog.services;

import com.jxnu.blog.common.ArticlePublish;
import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.pojo.article;
import com.jxnu.blog.pojo.user;
import com.jxnu.blog.reporsity.UserReposity;
import com.jxnu.blog.reporsity.articleReposity;
import com.jxnu.blog.Vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainPageServiceImp implements MainPageService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    articleReposity articleReposity;
    @Autowired
    UserReposity userReposity;
    @Override
    public ServerResponse<List<ArticleVo>> getArticles() {
        List<ArticleVo> list = new ArrayList<>();
        Set<String> keys = stringRedisTemplate.keys("view:*");
            for(String key:keys){
                int view = Integer.valueOf(stringRedisTemplate.opsForValue().get(key));
                ArticleVo articleVo = getArticleVo(key,view);
                list.add(articleVo);
            }
            Collections.sort(list);
            if(list.size()<=6){
                return ServerResponse.createBySuccess(list);
            }else{
                return ServerResponse.createBySuccess(list.subList(0,6));
            }
        }

    @Override
    public ServerResponse<ArticleVo> getArticle(int id) {
        article article = articleReposity.findById(id).get();
        if (article != null) {
            int view = Integer.valueOf(stringRedisTemplate.opsForValue().get("view:" + id));
            ArticleVo articleVo = getArticleVo("view:" + id, view);
            articleViewUp(id);
            return ServerResponse.createBySuccess(articleVo);
        } else {
            return ServerResponse.createByError(null);
        }
    }
    @Async
    public void articleViewUp(int id) {
        stringRedisTemplate.opsForValue().increment("view:" + id);
    }
    public ArticleVo getArticleVo(String key,int view){
        ArticleVo articleVo = new ArticleVo();
        int articleId = Integer.valueOf(key.split(":")[1]);
        String praise = stringRedisTemplate.opsForValue().get("praise:"+articleId);
        List<String> list = stringRedisTemplate.opsForList().range("comment:"+articleId,0,-1);
        article article = articleReposity.findByIdAndPublish(articleId, ArticlePublish.ISPUBLISH.getCode());
        if(article!=null){
            articleVo.setAuthorId(article.getAuthorId());
            user user = userReposity.findById(article.getAuthorId()).get();
            articleVo.setAuthorName(user.getUserName());
            articleVo.setDetail(article.getDetail());
            articleVo.setMainImage(article.getMainImage());
            articleVo.setId(article.getId());
            articleVo.setTitle(article.getTitle());
            articleVo.setWithcode(article.getWithcode());
            articleVo.setView(view);
            articleVo.setComments(list);
            articleVo.setLike(Integer.valueOf(praise));
            articleVo.setAuthorImage(user.getImage());
            articleVo.setLabel(article.getLabel());
            articleVo.setUpdateTime(article.getUpdateTime());
        }
        return articleVo;
    }
}
