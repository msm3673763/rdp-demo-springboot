package com.ucsmy.ucas.config.oauth2Authorize.impl;

import com.ucsmy.ucas.config.oauth2Authorize.Oauth2Http;
import com.ucsmy.ucas.config.oauth2Authorize.param.OauthParam;
import com.ucsmy.ucas.config.shiro.ShiroUtils;
import com.ucsmy.ucas.manage.ext.UcasToken;
import com.ucsmy.ucas.manage.ext.UcasUserInfo;
import com.ucsmy.ucas.manage.feign.UcasFeign;
import com.ucsmy.ucas.manage.service.SysTokenManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Oauth2Httpimpl implements Oauth2Http {

    private static final Logger log = LoggerFactory.getLogger(Oauth2Httpimpl.class);
//    static final String INFO_URL = "http://client:secret@localhost/oauth/info";

    @Autowired
    private SysTokenManagerService sysTokenManagerService;
    @Autowired
    private UcasFeign ucasFeign;
    @Autowired
    private OauthParam oauthParam;

    @Override
    public UcasToken getAccessToken(String accessCod) {
        UcasToken ucasToken =null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("grant_type", oauthParam.getGrantType());
            map.put("scope", oauthParam.getScope());
            map.put("code", accessCod);
            map.put("client_id", oauthParam.getClientId());
            map.put("client_secret", oauthParam.getClientSecret());
             ucasToken = ucasFeign.accessTokenByCode(map);
        }catch (Exception e){
            log.error("获取token失败",e);
        }
        return ucasToken;
    }

    @Override
    public boolean checkAccessToken(String accessToken) {
        return false;
    }

    @Override
    public UcasUserInfo getUserInfoByAccessToken(UcasToken ucasToken) {
        // TODO Auto-generated method stub
        UcasUserInfo ucasUserInfo = null;
        try {
            ucasUserInfo = ucasFeign.getUserInfo(ucasToken.getAccessToken());
            if (ucasUserInfo != null) {
                ucasUserInfo = sysTokenManagerService.setSysLoginTokenObject(ucasUserInfo, ucasToken);
            }
        } catch (Exception e) {
            log.error("获取用户信息失败",e);
        }
        return ucasUserInfo;
    }

    @Override
    public Boolean userLogin(String token) {
        // TODO Auto-generated method stub
        Boolean bo = ShiroUtils.autoLogin(token);
        return bo;
    }
}
