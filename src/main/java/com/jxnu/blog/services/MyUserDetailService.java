package com.jxnu.blog.services;

import com.jxnu.blog.pojo.user;
import com.jxnu.blog.reporsity.UserReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    UserReposity userReposity;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        user u = userReposity.findByUserName(s);
        if(u==null){
            u = userReposity.findByEmail(s);
        }
        User user = new User(u.getUserName(),u.getPassword(),true,true,true,true, AuthorityUtils.commaSeparatedStringToAuthorityList("ss"));
        return user;
    }
}
