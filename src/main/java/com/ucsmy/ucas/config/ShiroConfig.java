package com.ucsmy.ucas.config;


import com.ucsmy.ucas.commons.operation.log.LogFilter;
import com.ucsmy.ucas.commons.utils.StringUtils;
import com.ucsmy.ucas.config.oauth2Authorize.Oauth2HttpFilter;
import com.ucsmy.ucas.config.oauth2Authorize.Oauth2Verify;
import com.ucsmy.ucas.config.shiro.MyCredentialsMatcher;
import com.ucsmy.ucas.config.shiro.MyFormAuthenticationFilter;
import com.ucsmy.ucas.config.shiro.MyRolesAuthorizationFilter;
import com.ucsmy.ucas.config.shiro.ShiroRealmImpl;
import com.ucsmy.ucas.config.shiro.csrf.CsrfAuthenticationStrategy;
import com.ucsmy.ucas.config.shiro.csrf.CsrfFilter;
import com.ucsmy.ucas.config.shiro.csrf.CsrfTokenRepository;
import com.ucsmy.ucas.config.shiro.csrf.HttpSessionCsrfTokenRepository;
import com.ucsmy.ucas.manage.dao.ManagePermissionMapper;
import com.ucsmy.ucas.manage.entity.ManagePermission;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * shiro配置
 * Created by ucs_zhongtingyuan on 2017/3/28.
 */
@Configuration
public class ShiroConfig {
    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

    @Bean
    public AuthorizingRealm getShiroRealm() {
        AuthorizingRealm realm = new ShiroRealmImpl();
        realm.setCredentialsMatcher(getCredentialsMatcher());
        return realm;
    }

    @Bean
    public SimpleCredentialsMatcher getCredentialsMatcher() {
        return new MyCredentialsMatcher();
    }

    @Bean(name = "cacheManager")
    public CacheManager getCacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    @Bean(name = "csrfTokenRepository")
    public CsrfTokenRepository getHttpSessionCsrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean(name = "oauth2Verify")
    public Oauth2Verify getOauth2Verify() {
        return new Oauth2Verify();
    }

    @Bean(name = "csrfAuthenticationStrategy")
    public CsrfAuthenticationStrategy getCsrfAuthenticationStrategy() {
        CsrfAuthenticationStrategy cas = new CsrfAuthenticationStrategy();
        cas.setCsrfTokenRepository(getHttpSessionCsrfTokenRepository());
        return cas;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(getShiroRealm());
        dwsm.setCacheManager(getCacheManager());
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(ManagePermissionMapper managePermissionMapper) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());
        // 增加Filter
        shiroFilterFactoryBean.getFilters().put("authc", new MyFormAuthenticationFilter(getHttpSessionCsrfTokenRepository(),getOauth2Verify()));
        shiroFilterFactoryBean.getFilters().put("roles", new MyRolesAuthorizationFilter());
        shiroFilterFactoryBean.getFilters().put("oauth2", new Oauth2HttpFilter(getOauth2Verify()));
        shiroFilterFactoryBean.getFilters().put("csrf", new CsrfFilter(getHttpSessionCsrfTokenRepository()));
        // 为日志输出添加ip和用户信息的上下文
        shiroFilterFactoryBean.getFilters().put("log", new LogFilter());
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/main/index");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/Javascript/**", "anon");
        filterChainDefinitionMap.put("/libs/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/pages/login/**", "anon");
        filterChainDefinitionMap.put("/getRsa/**", "anon");
        filterChainDefinitionMap.put("/bindAccount/**", "anon");
        filterChainDefinitionMap.put("/pages/bind/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico/**", "anon");
        filterChainDefinitionMap.put("/service/model/**", "authc");
        this.initPermissionList(managePermissionMapper);
        // 注意在initPermissionList里也要有对应的拦截器添加
        filterChainDefinitionMap.put("/**", "oauth2,log,csrf,authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 初始化权限列表
     */
    private void initPermissionList(ManagePermissionMapper managePermissionMapper) {
        List<ManagePermission> ps = managePermissionMapper.queryPermissionAll();
        Map<String, List<String>> map_ps = new HashMap<>();
        for (ManagePermission p : ps) {
            if (StringUtils.isEmpty(p.getSn()) || StringUtils.isEmpty(p.getUrlAction()))
                continue;
            if (!map_ps.containsKey(p.getUrlAction())) {
                map_ps.put(p.getUrlAction(), new ArrayList<>());
            }
            map_ps.get(p.getUrlAction()).add(p.getSn());
        }
        for (Map.Entry<String, List<String>> entry : map_ps.entrySet()) {
            String roles = entry.getValue().toString().replace(" ", "");
            // 注意拦截器的添加
            filterChainDefinitionMap.put(entry.getKey(), "oauth2,log,csrf,authc,roles".concat(roles));
        }

    }
}
