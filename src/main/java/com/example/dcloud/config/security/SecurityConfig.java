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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
            if (user == null ){
                return null;
            }
            throw new UsernameNotFoundException("??????????????????");
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
        http    //??????jwt??????????????????csrf
                .csrf()
                .disable()
                //??????token??? ?????????session????????????
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //??????????????????????????????????????????
                .authorizeRequests()
                //anyRequest??????????????????
                .anyRequest()
                .authenticated()
                // ??????????????????
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        // ??????????????????????????????Manager
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        // ????????????????????????
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                //????????????
                .headers()
                .cacheControl();
        http.cors().configurationSource(CorsConfigurationSource());

        // ??????jwt????????????????????????
        http.addFilterBefore(jwtAuthorizationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //?????????????????????????????????????????????
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);

    }

    @Bean
    public JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter(){
        return new JwtAuthorizationTokenFilter();
    }

    private CorsConfigurationSource CorsConfigurationSource() {
        CorsConfigurationSource source =   new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");	//???????????????*????????????????????????????????????????????????ip???????????????????????????localhost???8080?????????????????????????????????
        corsConfiguration.addAllowedHeader("*");//header???????????????header????????????????????????token???????????????*?????????token???
        corsConfiguration.addAllowedMethod("*");	//????????????????????????PSOT???GET???
        ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**",corsConfiguration); //???????????????????????????url
        return source;
    }
}
