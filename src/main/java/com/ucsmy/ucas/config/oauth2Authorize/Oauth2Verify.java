package com.ucsmy.ucas.config.oauth2Authorize;


import com.ucsmy.ucas.commons.utils.StringUtils;
import com.ucsmy.ucas.commons.utils.UUIDGenerator;
import com.ucsmy.ucas.config.oauth2Authorize.param.LoginParam;
import com.ucsmy.ucas.config.shiro.ShiroRealmImpl;
import com.ucsmy.ucas.config.shiro.ShiroUtils;
import com.ucsmy.ucas.manage.dao.ManageUserOauth2RelMapper;
import com.ucsmy.ucas.manage.entity.ManageUserOauth2Rel;
import com.ucsmy.ucas.manage.ext.UcasToken;
import com.ucsmy.ucas.manage.ext.UcasUserInfo;
import com.ucsmy.ucas.manage.service.ManageUserAccountService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;


public class Oauth2Verify {
    @Autowired
    ManageUserOauth2RelMapper manageUserOauth2RelMapper;
    @Autowired
    ManageUserAccountService manageUserAccountService;
    @Autowired
    Oauth2Http oauth2Http;
    @Autowired
    private LoginParam loginParam;
    /**
     * 第三方登录存储在Session里的UserInfo key值
     */
    private final static String OAUTH_USERINFO_KEY = "USERINFO";

    public Oauth2Verify() {
    }

    protected boolean bindStatus(String openID) {
        if (!Boolean.parseBoolean(loginParam.getHasLocalUser())) {
            // 没有本地用户，不需要绑定
            return true;
        }
        Boolean rt = false;
        ManageUserOauth2Rel manageUserOauth2Rel = manageUserOauth2RelMapper.selectByOpenid(openID);
        if (manageUserOauth2Rel != null) {
            rt = true;
        }

        return rt;
    }

    /**
     * 添加本地用户和openId授权关系
     *
     * @param subject
     */
    public void addUserOauthRel(Subject subject) {
        if (!Boolean.parseBoolean(loginParam.getHasLocalUser())) {
            return;
        }
        ShiroRealmImpl.LoginUser user = (ShiroRealmImpl.LoginUser) subject.getPrincipal();
        Session session = SecurityUtils.getSubject().getSession();
        Object userInfo = session.getAttribute(OAUTH_USERINFO_KEY);
        if (userInfo == null) {
            return;
        }
        // 将userInfo的信息映射到LoginUser中，记录openId和ucasAccount
        ManageUserOauth2Rel manageUserOauth2Rel = manageUserOauth2RelMapper.selectByOpenid(((UcasUserInfo) userInfo).getOpenid());
        if (manageUserOauth2Rel == null) {
            manageUserOauth2Rel = new ManageUserOauth2Rel();
            manageUserOauth2Rel.setOpenId(((UcasUserInfo) userInfo).getOpenid());
            manageUserOauth2Rel.setUserId(user.getId());
            manageUserOauth2Rel.setUuid(UUIDGenerator.generate(32));
            manageUserOauth2RelMapper.insert(manageUserOauth2Rel);
        }
    }

    public Boolean autoLogin(String openid) {
        String account = manageUserAccountService.getAccountByOprenid(openid);
        if (StringUtils.isNotEmpty(account)) {
            ShiroUtils.autoLogin(account);
            return true;
        }
        return false;
    }

    public UcasToken getAccessToken(String code) {
        return oauth2Http.getAccessToken(code);
    }

    public UcasUserInfo getUserInfoByAccessToken(UcasToken ucasToken) {
        return oauth2Http.getUserInfoByAccessToken(ucasToken);
    }
}
