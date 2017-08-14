package com.ucsmy.ucas.config.shiro;

import com.ucsmy.ucas.commons.utils.EncryptUtils;
import com.ucsmy.ucas.config.oauth2Authorize.param.LoginParam;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 密码验证
 * Created by ucs_zhongtingyuan on 2017/1/17.
 */
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {

    @Autowired
    private LoginParam loginParam;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {

        MyUsernamePasswordToken token = (MyUsernamePasswordToken) authcToken;
        if(token.isAutoLogin() || Boolean.parseBoolean(loginParam.getOauthUser())) {
            return true;
        }
        String tokenCredentials = String.valueOf(token.getPassword());
        Object accountCredentials = getCredentials(info);
        return equals(EncryptUtils.MD5(tokenCredentials), accountCredentials);
    }
}
