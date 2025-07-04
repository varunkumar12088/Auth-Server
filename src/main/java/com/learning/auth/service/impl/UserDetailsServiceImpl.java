package com.learning.auth.service.impl;

import com.learning.auth.entity.User;
import com.learning.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null) {
            LOGGER.warn("User not found with username or email: {}", username);
            throw new UsernameNotFoundException("User not found with username or email: " + username);
        }
        if(!user.isEnabled()) {
            LOGGER.warn("User is not enabled: {}", username);
            throw new UsernameNotFoundException("User is not enabled: " + username);
        }
        GrantedAuthority grantedAuthority = () ->  user.getRole();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Arrays.asList(grantedAuthority)
        );
    }
}
