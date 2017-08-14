package com.ucsmy.ucas.manage.service.impl;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/5

 * Contributors:
 *      - initial implementation
 */

import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.commons.aop.result.ResultConst;
import com.ucsmy.ucas.commons.utils.CheckoutUtils;
import com.ucsmy.ucas.commons.utils.MsgSendUtils;
import com.ucsmy.ucas.manage.ext.SmsSendInput;
import com.ucsmy.ucas.manage.service.SysMsgSendService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 暂无描述
 *
 * @author ucs_leijinming
 * @since 2017/7/5
 */
@Service
public class SysMsgSendServiceImpl implements SysMsgSendService{
    private  final  static String  MOBLIE = "moblie";
    private  final  static String  EMAIL = "email";
    @Override
    public AosResult smsSend(SmsSendInput input,String type) {

        AosResult result = new AosResult();
        if (input == null) {
            result.setRetcode(String.valueOf(ResultConst.ERROR));
            result.setRetmsg("参数不能为空");
            return result;
        }
        String reveice = input.getReveice();
        String title = input.getTitle();
        String content = input.getContent();
        String systemId = input.getSystemId();

        StringBuffer msg = new StringBuffer();
        if(MOBLIE.equals(type)){
            if (StringUtils.isBlank(reveice)) {
                msg.append("手机号不能为空，");
            } else {
                if (!CheckoutUtils.isPhone(reveice)) {
                    msg.append("手机号格式不正确，");
                }
            }
        }else if(EMAIL.equals(type)){
            if(StringUtils.isBlank(reveice)){
                msg.append("邮箱号不能为空，");
            }else{
                if(!CheckoutUtils.isEmail(reveice)){
                    msg.append("邮箱号格式不正确，");
                }
            }
        }

        if (StringUtils.isBlank(title)) {
            msg.append("标题不能为空，");
        }
        if (StringUtils.isBlank(content)) {
            msg.append("内容不能为空，");
        }
        if (StringUtils.isBlank(systemId)) {
            msg.append("系统Id不能为空，");
        }
        if (msg.length() > 0) {
            result.setRetcode(String.valueOf(ResultConst.ERROR));
            result.setRetmsg(msg.substring(0, msg.length() - 1).toString());
            return result;
        }
        try {
            if(MOBLIE.equals(type)){
                MsgSendUtils.smsSend(systemId, reveice, title, content);

            }else if(EMAIL.equals(type)){
                MsgSendUtils.emailSend(systemId,reveice,title,content);
            }
            result.setRetcode(String.valueOf(ResultConst.SUCCESS));
        } catch (Exception e) {
            result.setRetcode(String.valueOf(ResultConst.ERROR));

        }
        return result;
    }

}