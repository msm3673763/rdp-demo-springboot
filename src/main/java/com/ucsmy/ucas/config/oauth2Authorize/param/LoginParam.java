package com.ucsmy.ucas.config.oauth2Authorize.param;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/5

 * Contributors:
 *      - initial implementation
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 登录参数管理
 *
 * @author chenqilin
 * @since 2017/7/5
 */
@Component
@ConfigurationProperties(prefix = "loginParam")
public class LoginParam {

    /** session管理方式：header/cookies */
    private String sessionType;
    /** 是否使用第三方用户登录 */
    private String oauthUser;
    /** 是否有本地用户 */
    private String hasLocalUser = "true";
    /** 是否开启图形验证码 */
    private String captcha;
    /** 图形验证码有效时间（秒） */
    private String captchaExpire;

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getOauthUser() {
        return oauthUser;
    }

    public void setOauthUser(String oauthUser) {
        this.oauthUser = oauthUser;
    }

    public String getHasLocalUser() {
        return hasLocalUser;
    }

    public void setHasLocalUser(String hasLocalUser) {
        this.hasLocalUser = hasLocalUser;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaExpire() {
        return captchaExpire;
    }

    public void setCaptchaExpire(String captchaExpire) {
        this.captchaExpire = captchaExpire;
    }
}