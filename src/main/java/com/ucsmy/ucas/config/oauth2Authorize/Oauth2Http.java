package com.ucsmy.ucas.config.oauth2Authorize;

import com.ucsmy.ucas.manage.ext.UcasToken;
import com.ucsmy.ucas.manage.ext.UcasUserInfo;

public interface Oauth2Http {

    UcasToken getAccessToken(String accessToken); // 获取

    boolean checkAccessToken(String accessToken); // 验证access token是否有效

    UcasUserInfo getUserInfoByAccessToken(UcasToken ucasToken);// 根据access token获取用户信息

    Boolean userLogin(String token);

}