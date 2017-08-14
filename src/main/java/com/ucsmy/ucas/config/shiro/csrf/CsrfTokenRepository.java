package com.ucsmy.ucas.config.shiro.csrf;

import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CsrfTokenRepository {
    public CsrfToken generateToken(HttpServletRequest request);

    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response);

    public CsrfToken loadToken(HttpServletRequest request);

    public String getoauth2Token(String accessToken);
//
//	public void matchs(HttpServletRequest request);

    /**
     * 是否由统一认证登录成功回调
     * 1.来源有统一认证标识
     * 2.请求带code参数
     *
     * @param request 请求
     * @return
     */
    boolean isOauthLoginSuccess(HttpServletRequest request);

}
