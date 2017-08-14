package com.ucsmy.ucas.manage.feign;

import com.ucsmy.ucas.manage.ext.UcasToken;
import com.ucsmy.ucas.manage.ext.UcasUserInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * AosApi接口
 * Created by ucs_leijinming on 2017/6/13.
 */
@FeignClient(name = "${ucas.core.name}", url = "${ucas.core.hosturl}")
public interface UcasFeign {
    /**
     * 授权码模式获取token
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/oauth/accessToken", produces = "application/json")
    UcasToken accessTokenByCode(@RequestParam Map<String, Object> map);


    /**
     * 根据token获取用户信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/oauth/resource/tokenUser/userInfo", produces = "application/json")
    UcasUserInfo getUserInfo(@RequestParam("access_token") String accessToken);


}