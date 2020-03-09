package com.jxnu.blog.reporsity;

import com.jxnu.blog.pojo.comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface commentReposity extends JpaRepository<comment,Integer> {
}
