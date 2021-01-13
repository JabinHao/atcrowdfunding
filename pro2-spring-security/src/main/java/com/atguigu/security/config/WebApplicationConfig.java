package com.atguigu.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity // 启用web环境下权限控制功能
public class WebApplicationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

/*        builder
                .inMemoryAuthentication()           // 在内存中完成账号、密码的检查
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("tom")           // 指定账号
                .password(new BCryptPasswordEncoder().encode("123456"))                 // 指定密码
                .roles("ADMIN","学徒")                     // 指定当前用户角色
                .and()
                .withUser("jerry")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .authorities("UPDATE","查看")              // 也可以指定权限
                ;*/
        builder
                .userDetailsService(userDetailsService)
                // .passwordEncoder(new BCryptPasswordEncoder())
                .passwordEncoder(passwordEncoder)
                ;
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        security
            .authorizeRequests()                        // 对请求进行授权
            .antMatchers("/index.jsp")    // 针对 /index.jsp 路径进行授权
            .permitAll()                            // 可以无条件访问
            .antMatchers("/layui/**")
            .permitAll()
            .antMatchers("/level1/**")
            .hasRole("学徒")  // 要求具有学徒角色的用户才能访问level1下的资源
            .antMatchers("/level2/**")
            .hasAuthority("查看") // 要求具有查看权限的用户才能访问level2下的资源
            .antMatchers("/level3/**")
            .hasAnyRole("ADMIN","学徒")
            .and()
            .authorizeRequests()
            .anyRequest()                            // 任意请求
            .authenticated()                         // 需要登录以后才可以访问
            .and()
            .formLogin()                             // 使用表单形式登录
            .loginPage("/index.jsp")                 // 指定登录页面，不指定则访问SpringSecurity自带的登录页
            .loginProcessingUrl("/do/login.html")    // 指定提交登录表单的地址，设置后则覆盖loginPage设置的的默认值
            .permitAll()
            .usernameParameter("loginAcct")          // 指定登录账号请求参数名
            .passwordParameter("userPswd")           // 指定登录密码的请求参数名
            .defaultSuccessUrl("/main.html")         // 设置登录成功后默认前往的URL地址
            .and()
            .csrf().disable()                        // 禁用csrf
            .logout()
            .logoutUrl("/do/logout.html")
            .logoutSuccessUrl("/index.jsp")
            .and()
            .exceptionHandling()                       // 指定异常处理器
//            .accessDeniedPage("/to/no/auth/page.html") // 方式一：访问被拒绝时前往的页面
            // 方式二：
            .accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
                httpServletRequest.setAttribute("message", "抱歉！您无法访问这个资源");
                httpServletRequest.getRequestDispatcher("/WEB-INF/views/no_auth.jsp").forward(httpServletRequest,httpServletResponse);
            })
             .and()
             .rememberMe()
             .tokenRepository(tokenRepository)
             ;

    }


}
