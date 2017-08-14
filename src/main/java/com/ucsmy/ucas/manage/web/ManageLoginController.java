package com.ucsmy.ucas.manage.web;

import com.google.code.kaptcha.Producer;
import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.commons.utils.StringUtils;
import com.ucsmy.ucas.config.oauth2Authorize.param.LoginParam;
import com.ucsmy.ucas.config.oauth2Authorize.param.OauthParam;
import com.ucsmy.ucas.config.shiro.MyFormAuthenticationFilter;
import com.ucsmy.ucas.config.shiro.ShiroRealmImpl;
import com.ucsmy.ucas.config.shiro.ShiroUtils;
import com.ucsmy.ucas.manage.entity.ManagePermission;
import com.ucsmy.ucas.manage.ext.MainModulePojo;
import com.ucsmy.ucas.manage.ext.UserProfilePojo;
import com.ucsmy.ucas.manage.service.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenqilin
 * @version V1.0
 * @ClassName:
 * @Description:
 * @date 2017/4/13
 */
@Controller
public class ManageLoginController {

    @Autowired
    private ManagePermissionService managePermissionService;
    @Autowired
    private ManageModuleService manageModuleService;
    @Autowired
    private ManageUserProfileService manageUserProfileService;
    @Autowired
    private SysCaptchaService sysCaptchaService;
    @Autowired
    private SysSecretKeyManagerService sysSecretKeyManagerService;
    @Autowired
    private OauthParam oauthParam;
    @Autowired
    private LoginParam loginParam;
    @Autowired
    private Producer producer;

    @RequestMapping(value = "login")
    public String login(HttpServletRequest httpRequest, Model model) {
        Map<String, String> map = new HashMap();
        String ctx = httpRequest.getScheme() + "://" + httpRequest.getServerName() //服务器地址
                + ":"
                + httpRequest.getServerPort()           //端口号
                + httpRequest.getContextPath();    //项目名称
//        model.addAttribute("ctx", ctx + "/");
        ctx = ctx + "/" + oauthParam.getMainIndex();
        if (!Boolean.parseBoolean(loginParam.getOauthUser())) {
            // 不需要第三方登录
            map.put("loginType", "local");
        } else {
            return "redirect:" + oauthParam.getHosturl() + "/" + oauthParam.getOauth2Url() + oauthParam.getClientId() + "&redirect_uri=" + ctx;
        }
        if (SecurityUtils.getSubject().isAuthenticated())
            return "redirect:index";
        return "login/index";
    }

    @RequestMapping(value = "outSys")
    public String outSys(HttpServletRequest httpRequest, Model model) {
        SecurityUtils.getSubject().logout();
        return "redirect:login";
    }


    @RequestMapping(value = "index")
    public String index(HttpServletRequest httpRequest, Model model) {
        String ctx = httpRequest.getScheme() + "://" + httpRequest.getServerName() //服务器地址
                + ":"
                + httpRequest.getServerPort()           //端口号
                + httpRequest.getContextPath();    //项目名称
//        model.addAttribute("ctx", ctx + "/");
        // 获取用户基本信息
        ShiroRealmImpl.LoginUser user = ShiroUtils.getContextUser();
        List<ManagePermission> permissionList = managePermissionService.queryPermissionByUser(user);
        List<MainModulePojo> moduleList = manageModuleService.queryMainModuleByUser(user);
        // 权限
        Map<String, Boolean> roles = new HashMap<>();
        for (ManagePermission p : permissionList) {
            if (StringUtils.isEmpty(p.getSn()))
                continue;
            roles.put(p.getSn(), true);
        }

        UserProfilePojo userProfile = manageUserProfileService.queryUserProfileByUserId(user.getId());

        model.addAttribute("userProfile", userProfile);
        // 菜单
        model.addAttribute("user_modules", moduleList);
        // 登录信息
        model.addAttribute("loginUser", user);
        //用户角色
        model.addAttribute("user_roles", roles);
        return "main/index";
    }

    /**
     * 绑定网金帐号页面
     *
     * @return
     */
    @RequestMapping(value = "bindAccount")
    public String bindAccount(HttpServletRequest httpRequest, Model model) {
        String ctx = httpRequest.getScheme() + "://" + httpRequest.getServerName() //服务器地址
                + ":"
                + httpRequest.getServerPort()           //端口号
                + httpRequest.getContextPath();    //项目名称
//        model.addAttribute("ctx", ctx + "/");
        if (SecurityUtils.getSubject().isAuthenticated())
            return "redirect:index";
        return "bind/index";
    }


    @RequestMapping(value = "getRsa")
    @ResponseBody
    public AosResult getRsa(HttpSession httpSession) {
        Map<String, String> map;
        map = sysSecretKeyManagerService.getRsaPubKey(httpSession);

        if (!Boolean.parseBoolean(loginParam.getOauthUser())) {
            // 不需要第三方登录
            map.put("loginType", "local");
        } else {
            map.put("loginType", "other");
        }
        return AosResult.retSuccessMsg("success", map);
    }

    /**
     * 生成图形验证码
     */
    @RequestMapping(value = "captcha", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String capText = producer.createText();
        sysCaptchaService.setCaptchaCache(MyFormAuthenticationFilter.LOGIN_IMAGE_CAPTCHA + session.getId(), capText, Long.parseLong(loginParam.getCaptchaExpire()));
//        session.setAttribute(LOGIN_IMAGE_CAPTCHA, capText);
        BufferedImage image = producer.createImage(capText);
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }
}
