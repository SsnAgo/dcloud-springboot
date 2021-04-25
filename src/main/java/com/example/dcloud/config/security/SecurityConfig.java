package com.example.dcloud.config.security;


import com.example.dcloud.config.component.*;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true,jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private IUserService userService;
    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Resource
    private CustomUrlDecisionManager customUrlDecisionManager;
    @Resource
    private CustomFilter customFilter;


    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return usernameOrPhone -> {
            User user = userService.getUserByUsernameOrPhone(usernameOrPhone);
            if (null != user){
                user = userService.getUserInfo(user);
                return user;
            }
            throw new UsernameNotFoundException("输入信息有误");
        };
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/mobile/loginByPassword",
                "/mobile/loginByCode",
                "/manage/loginByPassword",
                "/manage/loginByCode",
                "/mobile/oauth/**",
                "/mobile/quickRegister",
                "/loginCaptcha",
                "/registerCaptcha",
                "/logout",
                "/common/**",
                "/css/**",
                "/register",
                "/js/**",
                "/webjars/**",
                "favicon.ico",
                "/index.html",
                "/doc.html",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                //"/captcha",
                "/ws/**"
        );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    //使用jwt，所以不使用csrf
                .csrf()
                .disable()
                //使用token， 不需要session，故关闭
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //允许登陆和注销这两个请求访问
                .authorizeRequests()
                //anyRequest都要求认证。
                .anyRequest()
                .authenticated()
                // 动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        // 判断用户角色和权限的Manager
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        // 可访问的权限列表
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                //禁用缓存
                .headers()
                .cacheControl();
        // 添加jwt，登陆授权过滤器
        http.addFilterBefore(jwtAuthorizationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义授权和未登陆结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);

    }

    @Bean
    public JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter(){
        return new JwtAuthorizationTokenFilter();
    }




}
