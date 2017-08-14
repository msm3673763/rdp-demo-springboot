package com.ucsmy.ucas.manage.service;

import org.apache.shiro.session.Session;

import javax.servlet.http.HttpSession;
import java.security.PrivateKey;
import java.util.Map;

/**
 * Created by ucs_panwenbo on 2017/4/14.
 */
public interface SysSecretKeyManagerService {


    Map<String,String> getRsaPubKey(HttpSession httpsesssion);

    Map<String,String> getAesKey(HttpSession httpsesssion);

    /**
     * 获取完私钥后消除记录
     *
     * @param session shiro session
     * @return
     */
    PrivateKey getPrivateKeyFromSession(Session session);
}


