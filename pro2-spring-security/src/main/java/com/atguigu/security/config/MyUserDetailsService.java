package com.atguigu.security.config;

import com.atguigu.security.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1.从数据库查询Admin对象
        String sql = "SELECT id,loginacct,userpswd,username,email FROM t_admin WHERE loginacct=?";

        List<Admin> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Admin.class),username);

        Admin admin = list.get(0);

        // 2.给Admin设置角色权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("UPDATE"));

        // 3.把admin对象和authorities封装到UserDetails中
        String userpswd = admin.getUserpswd();
        return new User(username, userpswd, authorities);
    }
}
