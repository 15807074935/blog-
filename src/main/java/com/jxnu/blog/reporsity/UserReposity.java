package com.jxnu.blog.reporsity;

import com.jxnu.blog.pojo.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserReposity extends JpaRepository<user,Integer> {
    user findByUserName(String userName);
    user findByEmail(String email);
    List<user> findByUserNameLike(String username);
    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM user WHERE id >= (SELECT FLOOR( MAX(id) * RAND()) FROM user ) ORDER BY id LIMIT 3", nativeQuery = true)
    List<user> findUserByRandom();
}
