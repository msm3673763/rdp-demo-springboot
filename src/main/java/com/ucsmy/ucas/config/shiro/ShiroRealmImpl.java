package com.ucsmy.ucas.config.shiro;


import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.commons.aop.result.ResultConst;
import com.ucsmy.ucas.commons.utils.RSAUtils;
import com.ucsmy.ucas.commons.utils.StringAndNumberUtil;
import com.ucsmy.ucas.commons.utils.StringUtils;
import com.ucsmy.ucas.config.oauth2Authorize.param.LoginParam;
import com.ucsmy.ucas.manage.entity.ManagePermission;
import com.ucsmy.ucas.manage.entity.ManageUserAccount;
import com.ucsmy.ucas.manage.ext.UcasUserInfo;
import com.ucsmy.ucas.manage.ext.enums.ModuleEnums;
import com.ucsmy.ucas.manage.service.ManagePermissionService;
import com.ucsmy.ucas.manage.service.ManageUserAccountService;
import com.ucsmy.ucas.manage.service.SysCaptchaService;
import com.ucsmy.ucas.manage.service.SysSecretKeyManagerService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.security.PrivateKey;
import java.sql.Timestamp;
import java.util.List;


/**
 * Created by ucs_zhongtingyuan on 2017/1/17.
 */
public class ShiroRealmImpl extends AuthorizingRealm {

    /**
     * 第三方登录存储在Session里的UserInfo key值
     */
    private final static String OAUTH_USERINFO_KEY = "USERINFO";

    @Autowired
    private ManageUserAccountService userAccountService;
    @Autowired
    private ManagePermissionService managePermissionService;
    @Autowired
    private SysCaptchaService sysCaptchaService;
    @Autowired
    private SysSecretKeyManagerService sysSecretKeyManagerService;
    @Autowired
    private LoginParam loginParam;

    public ShiroRealmImpl() {
        super();
    }

    // 登录信息和用户验证信息验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取登录信息
        MyUsernamePasswordToken token = (MyUsernamePasswordToken) authenticationToken;
        // 获取当前Session
        Session session = SecurityUtils.getSubject().getSession();
        // 从 Session 里获取 userInfo
        Object userInfo = session.getAttribute(OAUTH_USERINFO_KEY);
        // 使用第三方登录
        String openId = "";
        if (Boolean.parseBoolean(loginParam.getOauthUser())) {
            if (userInfo == null) {
                throw new AuthenticationException("用户信息为空");
            }
            UcasUserInfo ucasUserInfo = (UcasUserInfo) userInfo;
            openId = ucasUserInfo.getOpenid();
        }
        // 没有本地用户，即只有第三方的用户信息
        if (!Boolean.parseBoolean(loginParam.getHasLocalUser())) {
            UcasUserInfo ucasUserInfo = (UcasUserInfo) userInfo;
            // 没有本地用户的情况下，LoginUser里记录openId、UcasAccount
            return new SimpleAuthenticationInfo(
                    new LoginUser(openId, ucasUserInfo.getUcasAccount(), ucasUserInfo.getUcasAccount(), openId)
                    , ""
                    , getName());
        }

        //图形验证码开启关闭
        if (Boolean.parseBoolean(loginParam.getCaptcha())) {
            AosResult validate = sysCaptchaService.validateCaptcha(MyFormAuthenticationFilter.LOGIN_IMAGE_CAPTCHA + session.getId(), token.getCaptcha());
            if (String.valueOf(ResultConst.ERROR).equals(validate.getRetcode())) {
                throw new AuthenticationException(validate.getRetmsg());
            }
        }

        if (token.getUsername() == null) {
            throw new AuthenticationException("用户名不能为空");
        }
        if (token.getPassword() == null) {
            throw new AuthenticationException("密码不能为空");
        }


        ManageUserAccount user = userAccountService.findByUserName(token.getUsername());

        if (user == null) {
            throw new AuthenticationException("账号或密码不正确");
        }
        if (Boolean.parseBoolean(loginParam.getOauthUser())
                && !token.isAutoLogin()
                && userAccountService.isBindedOauthByUserId(token.getUsername())) {
            throw new AuthenticationException("该帐号已被绑定，不能再绑定");
        }
        String account = user.getAccount();
        String salt = user.getSalt();
        String password = String.valueOf(token.getPassword());
        PrivateKey privateKey = sysSecretKeyManagerService.getPrivateKeyFromSession(session);
        if (!StringAndNumberUtil.isNull(password) && privateKey != null) {
            password = RSAUtils.decrypt(privateKey, password);
            token.setPassword(account.concat(password).concat(salt).toCharArray());
        }
        return new SimpleAuthenticationInfo(
                new LoginUser(user.getUserId(), token.getUsername(), user.getAccount(), openId)
                , user.getPassword()
                , getName());
    }

    // 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        LoginUser user = ShiroUtils.getContextUser();
        List<ManagePermission> ps;
        ps = managePermissionService.queryPermissionByUser(user);
        for (ManagePermission p : ps) {
            if (StringUtils.isEmpty(p.getSn()))
                continue;
            info.addRole(p.getSn());
        }
        return info;
    }

    public static class LoginUser implements Serializable {
        private static final long serialVersionUID = 1L;
        /** userId，如果没有本地用户，则为openId */
        private String id;
        private String loginUserName;
        private String userName;
        private String openId;
        private Timestamp loginTime;

        public LoginUser(String id, String loginUserName, String userName, String openId) {
            this.id = id;
            this.loginUserName = loginUserName;
            this.userName = userName;
            this.openId = openId;
            this.loginTime = new Timestamp(System.currentTimeMillis());
        }

        public String getId() {
            return id;
        }

        public String getLoginUserName() {
            return loginUserName;
        }

        public String getUserName() {
            return userName;
        }

        public Timestamp getLoginTime() {
            return loginTime;
        }

        public String getOpenId() {
            return openId;
        }
    }
}
