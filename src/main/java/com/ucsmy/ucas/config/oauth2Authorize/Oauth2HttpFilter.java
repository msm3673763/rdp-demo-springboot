package com.ucsmy.ucas.config.oauth2Authorize;

import com.ucsmy.ucas.manage.ext.UcasToken;
import com.ucsmy.ucas.manage.ext.UcasUserInfo;
import org.apache.shiro.SecurityUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Oauth2HttpFilter implements Filter {

    Oauth2Verify oauth2Verify;
    /**
     * 第三方登录存储在Session里的UserInfo key值
     */
    private final static String OAUTH_USERINFO_KEY = "USERINFO";
    private static final String BING_URL = "/bindAccount";

    @Override
    public void init(FilterConfig paramFilterConfig) throws ServletException {
    }

    public Oauth2HttpFilter(Oauth2Verify oauth2Verify) {
        this.oauth2Verify = oauth2Verify;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("Oauth2HttpFilter just supports HTTP requests");
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String code = request.getParameter("code");
        if (SecurityUtils.getSubject().isAuthenticated() || code == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //拿code换取token
        UcasToken ucasToken = this.oauth2Verify.getAccessToken(code);
        if (ucasToken == null) {
            response.sendError(443, "Could not get the accessToken or code is Invalid ！");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        UcasUserInfo ucasUserInfo = this.oauth2Verify.getUserInfoByAccessToken(ucasToken);
        if (ucasUserInfo == null) {
            response.sendError(443, "Could not get the accessToken or accessToken is Invalid ！");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(OAUTH_USERINFO_KEY, ucasUserInfo);
        Boolean bindStatus = this.oauth2Verify.bindStatus(ucasUserInfo.getOpenid());
        if (!bindStatus) {
            response.setContentType("text/html;charset=UTF-8");
            response.sendRedirect(request.getContextPath().concat(BING_URL));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (this.oauth2Verify.autoLogin(ucasUserInfo.getOpenid())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
    }


}
