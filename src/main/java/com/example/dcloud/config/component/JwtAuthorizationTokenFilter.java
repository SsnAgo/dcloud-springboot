package com.example.dcloud.config.component;


import com.example.dcloud.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 每一次请求都要通过jwt验证
 */
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从request拿到header  即配置文件中的Authorization
        String authHeader = request.getHeader(tokenHeader);
        // 如果有header 以及是以tokenHead(即配置文件中的Bearer)开头的，表明有该token
        if (null != authHeader && authHeader.startsWith(tokenHead)){
            //拿到该请求头中的token并获取该请求头对应的用户名。
            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            // 有用户名但是没有权限列表（即未登录
            if (null != username && null == SecurityContextHolder.getContext().getAuthentication()){
                //登陆
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //验证userDetails是否是有效的，重新设置登陆对象
                if (jwtTokenUtil.validateToken(authToken, userDetails)){
                    // 根据userDetails拿到authenticationToken对象
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //再用这个对象设置一个  新的   userDetails，从WebAuthenticationDetailsSource里build一个
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //向Security环境中设置此权限对象。
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
