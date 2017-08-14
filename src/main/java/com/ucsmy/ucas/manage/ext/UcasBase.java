package com.ucsmy.ucas.manage.ext;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by ucs_wuchong on 2017/6/13.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UcasBase implements Serializable {

    private String errcode;

    private String errmsg;

    public UcasBase() {
    }

    public UcasBase(String errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
