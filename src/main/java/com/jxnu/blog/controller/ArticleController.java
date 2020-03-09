package com.jxnu.blog.controller;

import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.Vo.ArticleVo;
import com.jxnu.blog.dto.articleDto;
import com.jxnu.blog.pojo.WangEditor;
import com.jxnu.blog.pojo.article;
import com.jxnu.blog.services.ArticleService;
import com.jxnu.blog.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@RestController
public class ArticleController {
    @Autowired
    FileService fileServiceImp;
    @Autowired
    ArticleService articleServiceImp;
    @PostMapping("/write/image")
    public WangEditor saveImage(MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetfilename = fileServiceImp.upload(file,path);
        String url = "http://116.62.172.100:80/"+targetfilename;
        WangEditor we = new WangEditor(url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return we;
    }
    @PostMapping("/write/save")
    public ServerResponse<Integer> saveAll(Principal principal, @RequestBody articleDto articleDto){
        return articleServiceImp.saveAll(principal.getName(),articleDto);
    }
    @GetMapping("/write/save")
    public ServerResponse<List<article>> getAllSave(Principal principal){
        return articleServiceImp.getAllSave(principal.getName());
    }
    @PutMapping("/write/update")
    public ServerResponse<article> update(Principal principal,@RequestBody articleDto articleDto){
        return articleServiceImp.update(principal.getName(),articleDto);
    }
    @GetMapping("/write/getsave")
    public ServerResponse<article> getSave(Principal principal,int id){
        return articleServiceImp.getSave(principal.getName(),id);
    }
    @DeleteMapping("/write/delete")
    public ServerResponse<String> deleteOne(Principal principal,@RequestBody articleDto articleDto){
        return articleServiceImp.deleteOne(principal.getName(),articleDto.getId());
    }
    @PostMapping("/write/publish")
    public ServerResponse<Integer> publish(Principal principal, @RequestBody articleDto articleDto){
        return articleServiceImp.publish(principal.getName(),articleDto);
    }
    @PutMapping("/write/publish")
    public ServerResponse<String> publish2(Principal principal, @RequestBody articleDto articleDto){
        return articleServiceImp.publish2(principal.getName(),articleDto);
    }
    @GetMapping("/user/article")
    public ServerResponse<List<ArticleVo>> userArticle(Integer userId){
        return articleServiceImp.userArticle(userId);
    }
    @GetMapping("/user/article/hot")
    public ServerResponse<List<ArticleVo>> userArticleHot(Integer userId){
        return articleServiceImp.userArticleHot(userId);
    }
    @GetMapping("/article/count")
    public ServerResponse<Integer> getArticleCount(Integer id){
        return articleServiceImp.getArticleCount(id);
    }
    @GetMapping("/search/articles")
    public ServerResponse<List<ArticleVo>> getArticleByDetailOrTitle(String dt){
        return articleServiceImp.getArticleByDetailOrTitle(dt);
    }
    @GetMapping("/article/ispublish")
    public ServerResponse isPublish(Integer id){
        return articleServiceImp.isPublish(id);
    }
    @PutMapping("/cancelpublish")
    public ServerResponse cancelPublish(Principal principal,@RequestBody articleDto articleDto){
        return articleServiceImp.cancelPublish(principal.getName(),articleDto.getId());
    }
    @GetMapping("/article/randomarticle")
    public ServerResponse<List<ArticleVo>> randomArticle(){
        return articleServiceImp.randomArticle();
    }
}
