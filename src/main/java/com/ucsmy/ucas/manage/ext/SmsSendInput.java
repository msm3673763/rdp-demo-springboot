package com.ucsmy.ucas.manage.ext;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/5

 * Contributors:
 *      - initial implementation
 */

/**
 * 暂无描述
 *
 * @author ucs_leijinming
 * @since 2017/7/5
 */

public class SmsSendInput {

    private String reveice;
    private String title;
    private String content;
    private String systemId;

    public String getReveice() {
        return reveice;
    }

    public void setReveice(String reveice) {
        this.reveice = reveice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

}