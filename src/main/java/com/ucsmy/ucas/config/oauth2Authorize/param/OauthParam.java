package com.ucsmy.ucas.config.oauth2Authorize.param;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/5

 * Contributors:
 *      - initial implementation
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 统一认证参数管理
 *
 * @author chenqilin
 * @since 2017/7/5
 */
@ConfigurationProperties(prefix = "ucas.core")
@Component
public class OauthParam {


    /**
     * 统一认证host
     */
    private String hosturl;

    /**
     * 登录授权scope
     */
    private String scope;

    /**
     * 授权类型
     */
    private String grantType;

    /**
     * 当前系统client_id
     */
    private String clientId;

    /**
     * 当前系统client_secret
     */
    private String clientSecret;

    /**
     * 第三方登录页地址
     */
    private String oauth2Url;

    /**
     * 获取accessToken地址
     */
    private String getTokenUrl;

    /**
     * 本地登录页地址
     */
    private String localLoginUrl;

    /**
     * 本地主页地址
     */
    private String mainIndex;


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getHosturl() {
        return hosturl;
    }

    public void setHosturl(String hosturl) {
        this.hosturl = hosturl;
    }

    public String getOauth2Url() {
        return oauth2Url;
    }

    public void setOauth2Url(String oauth2Url) {
        this.oauth2Url = oauth2Url;
    }

    public String getGetTokenUrl() {
        return getTokenUrl;
    }

    public void setGetTokenUrl(String getTokenUrl) {
        this.getTokenUrl = getTokenUrl;
    }

    public String getLocalLoginUrl() {
        return localLoginUrl;
    }

    public void setLocalLoginUrl(String localLoginUrl) {
        this.localLoginUrl = localLoginUrl;
    }

    public String getMainIndex() {
        return mainIndex;
    }

    public void setMainIndex(String mainIndex) {
        this.mainIndex = mainIndex;
    }
}