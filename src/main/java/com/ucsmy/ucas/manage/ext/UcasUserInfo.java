package com.ucsmy.ucas.manage.ext;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UcasUserInfo extends UcasBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用与用户的唯一标识
     */
    private String openid;

    /**
     * 用户唯一标识
     */
    private String ucasAccount;

    /**
     * 用户邮箱账号
     */
    private String emailAccount;

    /**
     * 用户名称
     */
    private String realName;

    /**
     * 绑定手机
     */
    private String mobilePhone;

    /**
     * 绑定邮箱
     */
    private String email;

    /**
     * 性别
     */
    private String sex;

    /**
     * 头像地址
     */
    private String headImgUrl;

    /**
     * 所属用户组
     */
    private String userAccg;

    public UcasUserInfo() {
    }

    public UcasUserInfo(String errcode, String errmsg) {
        super(errcode, errmsg);
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUcasAccount() {
        return ucasAccount;
    }

    public void setUcasAccount(String ucasAccount) {
        this.ucasAccount = ucasAccount;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getUserAccg() {
        return userAccg;
    }

    public void setUserAccg(String userAccg) {
        this.userAccg = userAccg;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }
}