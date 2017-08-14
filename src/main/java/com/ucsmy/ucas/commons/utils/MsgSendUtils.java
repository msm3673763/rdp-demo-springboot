package com.ucsmy.ucas.commons.utils;

import ucsmy.usp.api.Msg;

public class MsgSendUtils {

    public static void smsSend(String systemId,String moblie, String title, String content) throws Exception {
        Msg.SmsSend(systemId,moblie,title,content);
    }

    public static void emailSend(String systemId,String reveice, String title, String content) throws Exception {
        Msg. EmailSend(systemId,reveice,title,content);
    }

}
