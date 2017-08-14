package com.ucsmy.ucas.manage.service.impl;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/13

 * Contributors:
 *      - initial implementation
 */

import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.commons.utils.StringUtils;
import com.ucsmy.ucas.manage.service.SysCacheService;
import com.ucsmy.ucas.manage.service.SysCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 图形验证码service
 *
 * @author chenqilin
 * @since 2017/7/13
 */
@Service
public class SysCaptchaServiceImpl implements SysCaptchaService {

    private final static String CAPTCHA_NAMESPACE = "supcar:manager:captcha:";

    // 校验提示语
    private final static String VALIDATE_EXPIRE = "图形验证码已失效，请重新获取输入";
    private final static String VALIDATE_FAIL = "请输入正确的图片验证码";
    private final static String CAPTCHA_EMPTY = "图形验证码不能为空";

    @Autowired
    private SysCacheService sysCacheService;

    @Override
    public void setCaptchaCache(String key, String capText, long expire) {
        sysCacheService.set(CAPTCHA_NAMESPACE + key, capText, expire);
    }

    @Override
    public String getCaptchaCache(String key) {
        return sysCacheService.getString(CAPTCHA_NAMESPACE + key);
    }

    @Override
    public AosResult validateCaptcha(String key, String captcha) {
        if (StringUtils.isEmpty(captcha)) {
            return AosResult.retFailureMsg(CAPTCHA_EMPTY);
        }
        String cacheCaptcha = getCaptchaCache(key);
        if (StringUtils.isEmpty(cacheCaptcha)) {
            return AosResult.retFailureMsg(VALIDATE_EXPIRE);
        }
        if (!cacheCaptcha.equals(captcha)) {
            return AosResult.retFailureMsg(VALIDATE_FAIL);
        }
        return AosResult.retSuccessMsg("success");
    }
}