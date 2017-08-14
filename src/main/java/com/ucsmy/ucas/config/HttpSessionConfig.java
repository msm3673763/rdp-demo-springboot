package com.ucsmy.ucas.config;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/4

 * Contributors:
 *      - initial implementation
 */

import com.ucsmy.ucas.config.oauth2Authorize.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Session管理配置，根据sessionType来决定sessionId在header还是cookies。<br>
 * sessionType: session管理方式，即sessionId存放的位置。<br>
 * 如果填 header，登录成功后会在响应头部返回 x-auth-token，同时要求客户端本地保存这个参数，在之后的请求头里带上这个参数；<br>
 * 如果填 cookies，登录成功后会在cookies里添加 SESSION 为key的键值对。<br>
 *
 * @author chenqilin
 * @since 2017/7/4
 */
@Configuration
public class HttpSessionConfig {

    /**
     * session管理方式：header
     */
    private final static String SESSION_HEADER = "header";

    @Autowired
    private LoginParam loginParam;

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        if (SESSION_HEADER.equals(loginParam.getSessionType())) {
            return new HeaderHttpSessionStrategy();
        }
        return new CookieHttpSessionStrategy();
    }

}