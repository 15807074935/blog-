package com.jxnu.blog.controller;

import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.services.MainPageService;
import com.jxnu.blog.Vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class MainPageController {
    @Autowired
    MainPageService mainPageServiceImp;

    @GetMapping("/mainpage/get")
    public ServerResponse<List<ArticleVo>> getArticles(){
         return mainPageServiceImp.getArticles();
    }
    @GetMapping("/mainpage/getone")
    public ServerResponse<ArticleVo> getArticle(int id){
        return mainPageServiceImp.getArticle(id);
    }
}
