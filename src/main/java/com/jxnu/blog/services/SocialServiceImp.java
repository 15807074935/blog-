package com.jxnu.blog.services;

import com.jxnu.blog.common.ArticlePublish;
import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.Vo.ArticleVo;
import com.jxnu.blog.Vo.commentVo;
import com.jxnu.blog.Vo.focusAndFansVo;
import com.jxnu.blog.pojo.article;
import com.jxnu.blog.pojo.comment;
import com.jxnu.blog.pojo.user;
import com.jxnu.blog.reporsity.UserReposity;
import com.jxnu.blog.reporsity.articleReposity;
import com.jxnu.blog.reporsity.commentReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SocialServiceImp implements SocialService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserReposity userReposity;
    @Autowired
    commentReposity commentReposity;
    @Autowired
    articleReposity articleReposity;
    @Override
    public ServerResponse<String> like(String username, Integer id) {
        user user = userReposity.findByUserName(username);
        redisLike(user.getId(),id);
        return ServerResponse.createBySuccess("success");
    }

    @Override
    public ServerResponse<String> dislike(String username, Integer id) {
        user user = userReposity.findByUserName(username);
        redisDisLike(user.getId(),id);
        return ServerResponse.createBySuccess("success");
    }

    @Async
    public void redisLike(int userId,int articleId){
        stringRedisTemplate.opsForList().leftPush("thinkgood:"+userId, String.valueOf(articleId));
        stringRedisTemplate.opsForValue().increment("praise:"+articleId);
    }
    @Async
    public void redisDisLike(int userId,int articleId){
        stringRedisTemplate.opsForList().remove("thinkgood:"+userId,0,String.valueOf(articleId));
        stringRedisTemplate.opsForValue().decrement("praise:"+articleId);
    }
    @Override
    public ServerResponse<String> islike(String username, Integer articleId){
        String id = String.valueOf(articleId);
        user user = userReposity.findByUserName(username);
        List<String> list=stringRedisTemplate.opsForList().range("thinkgood:"+user.getId(),0,-1);
        for(String s:list){
            if(s.equals(id)){
                return ServerResponse.createBySuccess("success");
            }
        }
        return ServerResponse.createByError("error");
    }

    @Override
    public ServerResponse<String> upComment(String username, String c, Integer articleId) {
        user user = userReposity.findByUserName(username);
        comment comment = new comment();
        comment.setArticleId(articleId);
        comment.setComment(c);
        comment.setUserId(user.getId());
        comment = commentReposity.save(comment);
        commentRedisIn(articleId,comment.getId());
        return ServerResponse.createBySuccess("success");
    }
    @Async
    public void commentRedisIn(Integer articleId,Integer commentId){
        stringRedisTemplate.opsForList().rightPush("comment:"+articleId,String.valueOf(commentId));
    }
    @Override
    public ServerResponse<List<commentVo>> GetComment(Integer articleId) {
        List<String> list = stringRedisTemplate.opsForList().range("comment:"+articleId,0,-1);
        if(list.size()!=0){
            List<commentVo> commentVoList = new ArrayList<>();
           for(String commentId:list){
               commentVo commentVo = new commentVo();
               comment comment = commentReposity.getOne(Integer.valueOf(commentId));
               commentVo.setComment(comment.getComment());
               commentVo.setUpdateTime(comment.getUpdateTime());
               user user = userReposity.findById(comment.getUserId()).get();
               commentVo.setUsername(user.getUserName());
               commentVo.setUserImage(user.getImage());
               commentVoList.add(commentVo);
           }
           return ServerResponse.createBySuccess(commentVoList);
        }else{
            return ServerResponse.createByError(null);
        }
    }

    @Override
    public ServerResponse focus(String username, Integer id) {
        ServerResponse serverResponse1 = me(username,id);
        if(serverResponse1.isSuccess()){
            ServerResponse serverResponse2 = isfocus(username,id);
            if(serverResponse2.isSuccess()){
                return ServerResponse.createByError("无法重复关注");
            }else{
                user user = userReposity.findByUserName(username);
                focusRedis(user.getId(),id);
                return ServerResponse.createBySuccess("关注成功");
            }
        }else{
            return ServerResponse.createByError("关注失败");
        }
    }

    @Override
    public ServerResponse unfocus(String username, Integer id) {
        ServerResponse serverResponse = me(username,id);
        if(serverResponse.isSuccess()){
            user user = userReposity.findByUserName(username);
            unfocusRedis(user.getId(),id);
            return serverResponse;
        }else{
            return serverResponse;
        }
    }

    @Override
    public ServerResponse me(String username, Integer id) {
        user user = userReposity.findByUserName(username);
        int userId=user.getId();
        if(userId==id){
            return ServerResponse.createByError("error");
        }else{
            return ServerResponse.createBySuccess("success");
        }
    }
    public ServerResponse isfocus(String username,Integer id){
        user user = userReposity.findByUserName(username);
        int userId=user.getId();
        List<String> list = stringRedisTemplate.opsForList().range("focus:"+userId,0,-1);
        String ids=String.valueOf(id);
        for(String focusid:list){
            if(focusid.equals(ids)){
                return ServerResponse.createBySuccess("focus");
            }
        }
        return ServerResponse.createByError("unfocus");
    }

    @Override
    public ServerResponse<List<ArticleVo>> focusArticle(String username) {
        user user = userReposity.findByUserName(username);
        List<ArticleVo> list = new ArrayList<>();
        List<String> articleIds = stringRedisTemplate.opsForList().range("subscribe:"+user.getId(),0,-1);
        for(String articleId:articleIds){
            article article = articleReposity.findByIdAndPublish(Integer.valueOf(articleId),ArticlePublish.ISPUBLISH.getCode());
            if(article!=null){
                ArticleVo articleVo = getArticleVo(article);
                list.add(articleVo);
            }
        }
        return ServerResponse.createBySuccess(list);
    }

    @Override
    public ServerResponse<focusAndFansVo> focusAndFans(Integer id) {
        focusAndFansVo focusAndFansVo = new focusAndFansVo();
        List<String> focusId = stringRedisTemplate.opsForList().range("focus:"+id,0,-1);
        List<String> fansId = stringRedisTemplate.opsForList().range("befocus:"+id,0,-1);
        focusAndFansVo.setFocuss(focusId.size());
        focusAndFansVo.setFans(fansId.size());
        return ServerResponse.createBySuccess(focusAndFansVo);
    }

    @Async
    public void focusRedis(int userId,int id){
        stringRedisTemplate.opsForList().rightPush("focus:"+userId,String.valueOf(id));//user关注了谁
        stringRedisTemplate.opsForList().rightPush("befocus:"+id, String.valueOf(userId));//谁被user关注
    }
    @Async
    public void unfocusRedis(int userId,int id){
        stringRedisTemplate.opsForList().remove("focus:"+userId,0,String.valueOf(id));
        stringRedisTemplate.opsForList().remove("befocus:"+id,0,String.valueOf(userId));
    }

    public ArticleVo getArticleVo(article article) {
        ArticleVo articleVo = new ArticleVo();
        String praise = stringRedisTemplate.opsForValue().get("praise:" + article.getId());
        List<String> list = stringRedisTemplate.opsForList().range("comment:" + article.getId(), 0, -1);
        String view = stringRedisTemplate.opsForValue().get("view:" + article.getId());
        articleVo.setAuthorId(article.getAuthorId());
        user user = userReposity.findById(article.getAuthorId()).get();
        articleVo.setAuthorName(user.getUserName());
        articleVo.setDetail(article.getDetail());
        articleVo.setMainImage(article.getMainImage());
        articleVo.setId(article.getId());
        articleVo.setTitle(article.getTitle());
        articleVo.setWithcode(article.getWithcode());
        articleVo.setView(Integer.valueOf(view));
        articleVo.setComments(list);
        articleVo.setLike(Integer.valueOf(praise));
        articleVo.setAuthorImage(user.getImage());
        articleVo.setLabel(article.getLabel());
        articleVo.setUpdateTime(article.getUpdateTime());
        return articleVo;
    }

    @Override
    public ServerResponse<List<user>> getFocus(Integer userId) {
        List<user> list = new ArrayList<>();
        List<String> focusId = stringRedisTemplate.opsForList().range("focus:"+userId,0,-1);
        for(String id:focusId){
            user u = userReposity.getOne(Integer.valueOf(id));
            u.setEmail(null);
            u.setPassword(null);
            list.add(u);
        }
        return ServerResponse.createBySuccess(list);
    }

    @Override
    public ServerResponse<List<user>> getFans(Integer userId) {
        List<user> list = new ArrayList<>();
        List<String> focusId = stringRedisTemplate.opsForList().range("befocus:"+userId,0,-1);
        for(String id:focusId){
            user u = userReposity.getOne(Integer.valueOf(id));
            u.setEmail(null);
            u.setPassword(null);
            list.add(u);
        }
        return ServerResponse.createBySuccess(list);
    }

    @Override
    public ServerResponse<List<ArticleVo>> getfavArticle(String username) {
        user u = userReposity.findByUserName(username);
        List<String> articleIds = stringRedisTemplate.opsForList().range("thinkgood:"+u.getId(),0,-1);
        List<ArticleVo> articleVos = new ArrayList<>();
        for(String id:articleIds){
            article article = articleReposity.findByIdAndPublish(Integer.valueOf(id),ArticlePublish.ISPUBLISH.getCode());
            if(article!=null){
                ArticleVo articleVo = getArticleVo(article);
                articleVos.add(articleVo);
            }
        }
        return ServerResponse.createBySuccess(articleVos);
    }
}
