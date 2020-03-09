package com.jxnu.blog.controller;

import com.jxnu.blog.Vo.ArticleVo;
import com.jxnu.blog.Vo.commentVo;
import com.jxnu.blog.Vo.focusAndFansVo;
import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.dto.*;
import com.jxnu.blog.pojo.user;
import com.jxnu.blog.services.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class SocialController {
    @Autowired
    SocialService socialServiceImp;
    @PutMapping("/social/like")
    public ServerResponse<String> like(Principal principal,@RequestBody articleDto articleDto){
        return socialServiceImp.like(principal.getName(),articleDto.getId());
    }
    @DeleteMapping("/social/dislike")
    public ServerResponse<String> dislike(Principal principal,@RequestBody articleDto articleDto){
        return socialServiceImp.dislike(principal.getName(),articleDto.getId());
    }
    @GetMapping("/social/islike")
    public ServerResponse<String> islike(Principal principal,Integer articleId){
        return socialServiceImp.islike(principal.getName(),articleId);
    }
    @PostMapping("/social/comment")
    public ServerResponse<String> upComment(Principal principal,@RequestBody commentDto commentDto){
        return socialServiceImp.upComment(principal.getName(),commentDto.getComment(),commentDto.getArticleId());
    }
    @GetMapping("/social/comment")
    public ServerResponse<List<commentVo>> GetComment(Integer id){
        return socialServiceImp.GetComment(id);
    }
    @PostMapping("/social/focus")
    public ServerResponse focus(Principal principal,@RequestBody focusDto focusDto){
        return socialServiceImp.focus(principal.getName(),focusDto.getId());
    }
    @DeleteMapping("/social/focus")
    public ServerResponse unfocus(Principal principal,@RequestBody focusDto focusDto){
        return socialServiceImp.unfocus(principal.getName(),focusDto.getId());
    }
    @GetMapping("/social/me")
    public ServerResponse me(Principal principal,String id){
        return socialServiceImp.me(principal.getName(),Integer.valueOf(id));
    }
    @GetMapping("/social/isfocus")
    public ServerResponse isfocus(Principal principal,String id){
        return socialServiceImp.isfocus(principal.getName(),Integer.valueOf(id));
    }
    @GetMapping("/social/focusarticle")
    public ServerResponse<List<ArticleVo>> focusArticle(Principal principal){
        return socialServiceImp.focusArticle(principal.getName());
    }
    @GetMapping("/social/focusandfans")
    public ServerResponse<focusAndFansVo> focusAndFans(Integer id){
         return socialServiceImp.focusAndFans(id);
    }
    @GetMapping("/social/focus")
    public ServerResponse<List<user>> getFocus(Integer userId){
        return socialServiceImp.getFocus(userId);
    }
    @GetMapping("/social/fans")
    public ServerResponse<List<user>> getFans(Integer userId){
        return socialServiceImp.getFans(userId);
    }
    @GetMapping("/social/thinkgoods")
    public ServerResponse<List<ArticleVo>> getfavArticle(Principal principal){
        return socialServiceImp.getfavArticle(principal.getName());
    }
}
