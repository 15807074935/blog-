package com.jxnu.blog.reporsity;

import com.jxnu.blog.pojo.article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface articleReposity extends JpaRepository<article,Integer> {
     List<article> findByAuthorIdAndPublishAndStatus(int authorId,int status1,int status2);
     List<article> findByAuthorIdAndPublish(int authorId,int status1);
     article findByAuthorIdAndId(int authorId,int id);
     article findByIdAndPublish(int articleId,int status);
     List<article> findByDetailLikeOrTitleLikeAndPublish(String detail,String title,int code);
     @Modifying
     @Transactional
     @Query(value = "select * from article where article.author_id=?1 and article.publish=?2 order by article.create_time desc", nativeQuery = true)
     List<article> findByAuthorIdAndPublishAndOrderByCreateTimeAsc(int authorId,int status1);
     @Modifying
     @Transactional
     @Query(value = "update article set article.title=?1,article.detail=?2,article.withcode=?3,article.main_image=?4,article.label=?6,article.update_time=unix_timestamp(now()) where article.id=?5", nativeQuery = true)
     int updateArticle(String title,String detail,String withcode,String mainImage,int id,String label);
     @Modifying
     @Transactional
     @Query(value = "update article set article.title=?1,article.detail=?2,article.withcode=?3,article.main_image=?4,article.publish=?6,article.label=?7,article.update_time=unix_timestamp(now()) where article.id=?5", nativeQuery = true)
     int publishArticle(String title,String detail,String withcode,String mainImage,int id,int status,String label);
     @Modifying
     @Transactional
     @Query(value = "update article set article.publish=?1,article.update_time=unix_timestamp(now()) where article.id=?2", nativeQuery = true)
     int unPublishArticle(int code,int articleId);
     @Modifying
     @Transactional
     @Query(value = "update article set article.status=?1,article.update_time=unix_timestamp(now()) where article.id=?2", nativeQuery = true)
     int DeleteArticle(int code,int articleId);
     @Modifying
     @Transactional
     @Query(value = "SELECT * FROM article WHERE id >= (SELECT FLOOR( MAX(id) * RAND()) FROM article ) ORDER BY id LIMIT 2", nativeQuery = true)
     List<article> randomArticles();
}
