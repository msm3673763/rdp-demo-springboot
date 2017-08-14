package com.ucsmy.ucas.manage.service;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/13

 * Contributors:
 *      - initial implementation
 */

import com.ucsmy.ucas.commons.aop.result.AosResult;

/**
 * 图形验证码service
 *
 * @author chenqilin
 * @since 2017/7/13
 */

public interface SysCaptchaService {

    /**
     * 缓存图形验证码
     *
     * @param key     key
     * @param capText 验证码内容
     * @param expire  过期时间（秒）
     */
    void setCaptchaCache(String key, String capText, long expire);

    /**
     * 从缓存中获取图形验证码
     *
     * @param key key
     */
    String getCaptchaCache(String key);

    /**
     * 校验图形验证码
     *
     * @param key     key
     * @param captcha 待校验的验证码
     * @return
     */
    AosResult validateCaptcha(String key, String captcha);
}
