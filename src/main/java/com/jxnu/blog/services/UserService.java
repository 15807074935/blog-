package com.jxnu.blog.services;

import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.pojo.user;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface UserService {
    ServerResponse<String> getValid(String email);
    ServerResponse<Map<String,String>> checkValid(String email, String valid);
    ServerResponse<Boolean> checkUserName(String userName);
    ServerResponse<user> register(String userName, String email, String password, String token,String image);
    ServerResponse<Boolean> checkEmail(String email);
    ServerResponse<user> getUser(String userName);
    ServerResponse<List<user>> getUserByUserName(String username);
    ServerResponse<user> getOtherById(Integer id);
    ServerResponse<List<user>> recommend();
}
