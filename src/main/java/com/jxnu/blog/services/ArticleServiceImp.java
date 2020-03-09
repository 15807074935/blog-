package com.jxnu.blog.services;

import com.jxnu.blog.common.ArticlePublish;
import com.jxnu.blog.common.ArticleStuts;
import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.Vo.ArticleVo;
import com.jxnu.blog.dto.articleDto;
import com.jxnu.blog.pojo.article;
import com.jxnu.blog.pojo.user;
import com.jxnu.blog.reporsity.UserReposity;
import com.jxnu.blog.reporsity.articleReposity;
import com.jxnu.blog.utils.getMainImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//@CacheConfig(cacheNames = "articleCache")
@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    UserReposity userReposity;
    @Autowired
    articleReposity articleReposity;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    SocialService socialServiceImp;

    @Override
    public ServerResponse<Integer> saveAll(String username, articleDto articleDto) {
        user user = userReposity.findByUserName(username);
        article article = articleSave(user,articleDto,ArticlePublish.NOTPUBLISH.getCode());
        if(article==null){
            return ServerResponse.createByError(-1);
        }else
            return ServerResponse.createBySuccess(article.getId());
    }
    @Override
    public ServerResponse<List<article>> getAllSave(String username) {
        user user = userReposity.findByUserName(username);
        List<article> articles = articleReposity.findByAuthorIdAndPublishAndStatus(user.getId(),ArticlePublish.NOTPUBLISH.getCode(),ArticleStuts.online.getCode());
        return ServerResponse.createBySuccess(articles);
    }
//    @Cacheable(key = "'unPublish:'+#id",unless="result==null")
    @Override
    public ServerResponse<article> getSave(String username, int id) {
        user user = userReposity.findByUserName(username);
        article article = articleReposity.findByAuthorIdAndId(user.getId(),id);
        if(article==null){
            return ServerResponse.createByError(null);
        }else
            return ServerResponse.createBySuccess(article);
    }
//    @CachePut(key = "'unPublish:'+#articleDto.id")
    @Override
    public ServerResponse<article> update(String username, articleDto articleDto) {
        user user = userReposity.findByUserName(username);
        article test = articleReposity.findByAuthorIdAndId(user.getId(),articleDto.getId());
        if(test==null){
            return ServerResponse.createByError(null);
        }else{
            String mainImage = getMainImage.getMainImage(articleDto.getWithcode());
            int count = articleReposity.updateArticle(articleDto.getTitle(),articleDto.getDetail(),articleDto.getWithcode(),mainImage,articleDto.getId(),"暂无");
            if(count==0){
                return ServerResponse.createByError(null);
            }else{
                article a = articleReposity.findById(articleDto.getId()).get();
                return ServerResponse.createBySuccess(a);
            }
        }
    }
//    @CacheEvict(key = "'unPublish:'+#id")
    @Override
    public ServerResponse<String> deleteOne(String username, int id) {
        user user = userReposity.findByUserName(username);
        article article = articleReposity.findByAuthorIdAndId(user.getId(),id);
        if(article==null){
            return ServerResponse.createByError("article is null");
        }else{
            int count = articleReposity.DeleteArticle(ArticleStuts.down.getCode(),id);
            if(count>0){
                return ServerResponse.createBySuccess("success");
            }else{
                return ServerResponse.createByError("delete failed");
            }
        }
    }

    @Override
    public ServerResponse<Integer> publish(String username, articleDto articleDto) {
        user user = userReposity.findByUserName(username);
        article article = articleSave(user,articleDto,ArticlePublish.ISPUBLISH.getCode());
        if(article==null){
            return ServerResponse.createByError(-1);
        }else{
            articleRedis(article.getId());
            articlePush(user.getId(),article.getId());
            return ServerResponse.createBySuccess(article.getId());
        }
    }
    @Async
    public void articlePush(int userId,int articleId){
       List<String> fans = stringRedisTemplate.opsForList().range("befocus:"+userId,0,-1);
       for(String fansId:fans){
           int id=Integer.valueOf(fansId);
           stringRedisTemplate.opsForList().leftPush("subscribe:"+id,String.valueOf(articleId));
       }
    }
    @Override
    public ServerResponse<String> publish2(String username, articleDto articleDto) {
        user user = userReposity.findByUserName(username);
        article test = articleReposity.findByAuthorIdAndId(user.getId(),articleDto.getId());
        if(test==null){
            return ServerResponse.createByError("error");
        }else{
            String mainImage = getMainImage.getMainImage(articleDto.getWithcode());
            int count = articleReposity.publishArticle(articleDto.getTitle(),articleDto.getDetail(),articleDto.getWithcode(),mainImage,articleDto.getId(),ArticlePublish.ISPUBLISH.getCode(),"暂无");
            if(count==0){
                return ServerResponse.createByError("error");
            }else{
                articleRedis(articleDto.getId());
                articlePush(user.getId(),articleDto.getId());
                return ServerResponse.createBySuccess("success");
            }
        }
    }

    @Override
    public ServerResponse<List<ArticleVo>> userArticle(Integer userId) {
        List<ArticleVo> list = new ArrayList<>();
        List<article> articles = articleReposity.findByAuthorIdAndPublishAndOrderByCreateTimeAsc(userId,ArticlePublish.ISPUBLISH.getCode());
        for(article article:articles){
            ArticleVo articleVo = socialServiceImp.getArticleVo(article);
            list.add(articleVo);
        }
        return ServerResponse.createBySuccess(list);
    }
    @Override
    public ServerResponse<List<ArticleVo>> userArticleHot(Integer userId){
        List<ArticleVo> list = new ArrayList<>();
        List<article> articles = articleReposity.findByAuthorIdAndPublish(userId,ArticlePublish.ISPUBLISH.getCode());
        for(article article:articles){
            ArticleVo articleVo = socialServiceImp.getArticleVo(article);
            list.add(articleVo);
        }
        Collections.sort(list);
        return ServerResponse.createBySuccess(list);
    }

    @Override
    public ServerResponse<List<ArticleVo>> randomArticle() {
        List<ArticleVo> articleVos = new ArrayList<>();
        List<article> articles = articleReposity.randomArticles();
        for(article article:articles){
            ArticleVo articleVo = new ArticleVo();
            articleVo.setId(article.getId());
            articleVo.setTitle(article.getTitle());
            String view = stringRedisTemplate.opsForValue().get("view:" + article.getId());
            articleVo.setView(Integer.valueOf(view));
            articleVos.add(articleVo);
        }
        if(articleVos.size()>0){
            return ServerResponse.createBySuccess(articleVos);
        }
        else {
            return ServerResponse.createByError(null);
        }
    }

    @Override
    public ServerResponse<Integer> getArticleCount(Integer id) {
        List<article> articles = articleReposity.findByAuthorIdAndPublish(id,ArticlePublish.ISPUBLISH.getCode());
        return ServerResponse.createBySuccess(articles.size());
    }

    @Override
    public ServerResponse<List<ArticleVo>> getArticleByDetailOrTitle(String dt) {
        List<article> articles = articleReposity.findByDetailLikeOrTitleLikeAndPublish("%"+dt+"%","%"+dt+"%",ArticlePublish.ISPUBLISH.getCode());
        List<ArticleVo> articleVos = new ArrayList<>();
        if(articles.size()!=0){
           for(article article:articles){
               ArticleVo articleVo = socialServiceImp.getArticleVo(article);
               articleVos.add(articleVo);
           }
           Collections.sort(articleVos);
        }
        return ServerResponse.createBySuccess(articleVos);
    }

    @Override
    public ServerResponse isPublish(Integer articleId) {
        article article = articleReposity.findById(articleId).get();
        if(article.getPublish()==ArticlePublish.ISPUBLISH.getCode()){
            return ServerResponse.createBySuccess(null);
        }else{
            return ServerResponse.createByError(null);
        }
    }

    @Override
    public ServerResponse cancelPublish(String username,Integer articleId) {
        user user = userReposity.findByUserName(username);
        article test = articleReposity.findByAuthorIdAndId(user.getId(),articleId);
        if(test==null){
            return ServerResponse.createByError("error");
        }else{
           int count = articleReposity.unPublishArticle(ArticlePublish.NOTPUBLISH.getCode(),articleId);
           if(count>0){
               return ServerResponse.createBySuccess("success");
           }
           return ServerResponse.createByError("error");
        }
    }
    @Async
    public void articleRedis(int id){
        //浏览数
        String view = stringRedisTemplate.opsForValue().get("view:"+id);
        if(view==null){
            stringRedisTemplate.opsForValue().set("view:"+id,"0");
        }
        //点赞数
        String praise = stringRedisTemplate.opsForValue().get("praise:"+id);
        if(praise==null){
            stringRedisTemplate.opsForValue().set("praise:"+id,"0");
        }
    }
//    @CachePut(key = "'publishCache:'+result.id")
    public article articleSave(user user, articleDto articleDto,int publish){
        String code = articleDto.getWithcode();
        String mainImage= getMainImage.getMainImage(code);
        article article = new article();
        article.setAuthorId(user.getId());
        article.setDetail(articleDto.getDetail());
        article.setTitle(articleDto.getTitle());
        article.setStatus(ArticleStuts.online.getCode());
        article.setMainImage(mainImage);
        article.setWithcode(articleDto.getWithcode());
        article.setPublish(publish);
        article.setLabel("暂无");
        article = articleReposity.save(article);
        return article;
    }
}
