package com.jxnu.blog.controller;

import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.dto.checkValidDto;
import com.jxnu.blog.dto.registerDto;
import com.jxnu.blog.pojo.user;
import com.jxnu.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userServiceImp;
    @GetMapping("/register/email")
    public ServerResponse<Boolean> checkEmail(String email){
        return userServiceImp.checkEmail(email);
    }
    @GetMapping("/register/valid/{email}")
    public ServerResponse<String> getValid(@PathVariable("email") String email){
        return userServiceImp.getValid(email);
    }
    @PostMapping("/register/valid")
    public ServerResponse<Map<String,String>> checkValid(@RequestBody checkValidDto checkValidDto){
        return userServiceImp.checkValid(checkValidDto.getEmail(),checkValidDto.getValid());
    }
    @GetMapping("/register/{name}")
    public ServerResponse<Boolean> checkUserName(@PathVariable("name") String userName){
        return userServiceImp.checkUserName(userName);
    }
    @PostMapping("/register")
    public ServerResponse<user> register(@RequestBody registerDto registerDto){
        return userServiceImp.register(registerDto.getUserName(),registerDto.getEmail(),registerDto.getPassword(),registerDto.getToken(),registerDto.getImage());
    }
    @GetMapping("/user")
    public ServerResponse<user> getUser(Principal principal){
        String username = principal.getName();
        return userServiceImp.getUser(username);
    }
    @GetMapping("/users")
    public ServerResponse<List<user>> getUserByUserName(String username) {
        return userServiceImp.getUserByUserName(username);
    }
    @GetMapping("/other")
    public ServerResponse<user> getOtherById(Integer id) {
        return userServiceImp.getOtherById(id);
    }
    @GetMapping("/user/recommend")
    public ServerResponse<List<user>> recommend(){
        return userServiceImp.recommend();
    }
}
