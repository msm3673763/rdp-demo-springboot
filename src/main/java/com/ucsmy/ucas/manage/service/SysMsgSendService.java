package com.ucsmy.ucas.manage.service;

import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.manage.ext.SmsSendInput;

/**
 * Created by ucs_leijinming on 2017/7/5.
 */
public interface SysMsgSendService {

    AosResult smsSend(SmsSendInput input,String type);
}
