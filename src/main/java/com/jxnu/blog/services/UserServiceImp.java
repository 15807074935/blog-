package com.jxnu.blog.services;

import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.pojo.user;
import com.jxnu.blog.reporsity.UserReposity;
import com.jxnu.blog.utils.EmailSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.mail.MessagingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserReposity userReposity;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JavaMailSenderImpl javaMailSender;
    EmailSend emailSend = new EmailSend();
    @Async
    @Override
    public ServerResponse<String> getValid(String email) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<6;i++){
           stringBuilder.append(random.nextInt(9));
        }
        String count = stringRedisTemplate.opsForValue().get(email+":count");
        if(count==null){
            stringRedisTemplate.opsForValue().set(email+":count","1",10, TimeUnit.MINUTES);
            stringRedisTemplate.opsForValue().set(email,stringBuilder.toString(),10, TimeUnit.MINUTES);
            try {
                emailSend.emailSend(javaMailSender,email,stringBuilder.toString());
                return ServerResponse.createBySuccess("发送成功","success");
            } catch (MessagingException e) {
                return ServerResponse.createByError("发送失败","error");
            }
        }else{
            if(Integer.valueOf(count)<=5){
                stringRedisTemplate.opsForValue().increment(email+":count");
                stringRedisTemplate.opsForValue().set(email,stringBuilder.toString(),10, TimeUnit.MINUTES);
                try {
                    emailSend.emailSend(javaMailSender,email,stringBuilder.toString());
                    return ServerResponse.createBySuccess("发送成功","success");
                } catch (MessagingException e) {
                    return ServerResponse.createByError("发送失败","error");
                }
            }else{
                return ServerResponse.createByError("请稍后再试","error");
            }
        }
    }
    @Override
    public ServerResponse<Map<String,String>> checkValid(String email, String valid) {
       String code = stringRedisTemplate.opsForValue().get(email);
       Map<String,String> map = new HashMap<>();
       if(valid.equals(code)){
           String token = UUID.randomUUID().toString();
           stringRedisTemplate.opsForValue().set(email+"token",token,10,TimeUnit.MINUTES);
           map.put("token",token);
           return ServerResponse.createBySuccess(map,"验证码正确");
       }else{
           map.put("token","error");
           return ServerResponse.createByError(map,"验证码不正确");
       }
    }
    @Override
    public ServerResponse<Boolean> checkUserName(String userName){
        user user = userReposity.findByUserName(userName);
        if(user==null){
            return ServerResponse.createBySuccess(true,"昵称未使用");
        }else{
            return ServerResponse.createByError(false,"昵称被占用");
        }
    }
    @Override
    public ServerResponse<Boolean> checkEmail(String email) {
        user user = userReposity.findByEmail(email);
        if(user==null){
            return ServerResponse.createBySuccess(true);
        }else{
            return ServerResponse.createByError(false);
        }
    }
    @Override
    public ServerResponse<user> register(String userName, String email, String password, String token,String image) {
       if(!stringRedisTemplate.opsForValue().get(email+"token").equals(token)) {
           return ServerResponse.createByError(null,"token错误");
       }
       if(!checkUserName(userName).isSuccess()){
           return ServerResponse.createByError(null,"昵称被占用");
       }
       if(!checkEmail(email).isSuccess()){
           return ServerResponse.createByError(null,"邮箱已注册");
       }
       String newPassword=passwordEncoder.encode(password);
       user user = new user();
       user.setImage("http:");
       user.setPassword(newPassword);
       user.setUserName(userName);
       user.setEmail(email);
       user.setImage(image);
       user u = userReposity.save(user);
       if (u==null){
           return ServerResponse.createByError(null,"注册失败");
       }else{
           u.setPassword(null);
           return ServerResponse.createBySuccess(u,"注册成功");

       }
    }
    @Override
    public ServerResponse<user> getUser(String userName) {
        user user = userReposity.findByUserName(userName);
        user.setPassword(null);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<List<user>> getUserByUserName(String username) {
        if(StringUtils.isEmpty(username))
            return ServerResponse.createByError(null);
        List<user> users = userReposity.findByUserNameLike("%"+username+"%");
        for(user user:users){
            user.setPassword(null);
            user.setEmail(null);
        }
        return ServerResponse.createBySuccess(users);
    }

    @Override
    public ServerResponse<user> getOtherById(Integer id) {
        user user = userReposity.findById(id).get();
        if(user!=null){
            user.setEmail(null);
            user.setPassword(null);
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByError(user);
    }

    @Override
    public ServerResponse<List<user>> recommend() {
       List<user> list = userReposity.findUserByRandom();
       for(user u:list){
           u.setPassword(null);
           u.setEmail(null);
       }
       return ServerResponse.createBySuccess(list);
    }
}
