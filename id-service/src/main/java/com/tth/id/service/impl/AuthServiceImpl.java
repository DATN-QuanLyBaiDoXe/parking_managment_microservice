package com.tth.id.service.impl;

import com.tth.id.auth.CustomUserDetails;
import com.tth.id.model.User;
import com.tth.id.service.AuthService;
import com.tth.id.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserService userService;

    // JwtAuthenticationFilter sẽ sử dụng hàm này
    @Transactional
    @Override
    public UserDetails loadUserByUuid(String uuid) {
        User user = userService.findByUuid(uuid);
        if(user == null){
            throw new UsernameNotFoundException("User not found with uuid: " + uuid);
        }
        return new CustomUserDetails(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByUsername(s);
        if(user == null){
            throw new UsernameNotFoundException("User not found with username " + s);
        } else {
            LOGGER.info("Find user with username " + s + " ==> uuid " + user.getUuid());
        }
        return new CustomUserDetails(user);
    }
}
