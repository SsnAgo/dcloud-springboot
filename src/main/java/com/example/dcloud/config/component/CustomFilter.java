package com.example.dcloud.config.component;

import com.example.dcloud.pojo.Menu;
import com.example.dcloud.pojo.Role;
import com.example.dcloud.service.IMenuService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 权限控制
 * 根据请求的url分析该url所需的权限
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Resource
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        // 拿到所有角色对应的所有菜单
        List<Menu> menus = menuService.getMenusWithRole();
        for (Menu menu : menus) {
            // 如果要访问的url在需要权限限制的列表里 对应某个url  则将可以访问该url的所有角色存入SecurityConfig
            if (antPathMatcher.match(menu.getUrl(),requestUrl)){
                // 将可以访问此菜单的所有角色存入Collection<ConfigAttribute>
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(str);
            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
