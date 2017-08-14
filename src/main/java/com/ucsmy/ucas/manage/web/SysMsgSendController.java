package com.ucsmy.ucas.manage.web;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/5

 * Contributors:
 *      - initial implementation
 */

import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.manage.ext.SmsSendInput;
import com.ucsmy.ucas.manage.service.SysMsgSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 暂无描述
 *
 * @author ucs_leijinming
 * @since 2017/7/5
 */
@Controller
@RequestMapping("/msg")
public class SysMsgSendController {
    @Autowired
    SysMsgSendService sysMsgSendService;
    @RequestMapping("/smsSend")
    public AosResult smsSend(@RequestBody SmsSendInput input) {
        return sysMsgSendService.smsSend(input,"mobile");
    }

    @RequestMapping("/emailSend")
    public AosResult emailSend(@RequestBody SmsSendInput input) {
        return sysMsgSendService.smsSend(input,"email");
    }
}