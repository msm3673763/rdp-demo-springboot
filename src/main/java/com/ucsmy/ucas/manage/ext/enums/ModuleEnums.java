package com.ucsmy.ucas.manage.ext.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * aosapi 验证模块
 * <p>
 * Created by ucs_wuchong on 2017/6/15.
 */
public enum ModuleEnums {

    /**
     * 1注册，2找回密码，3身份验证，4登录，5手机或邮箱解绑验证，6手机或邮箱绑定验证，7手机或邮箱变更验证
     */
    Register("1"), Password_Retrieval("2"), Authentication("3"), Login("4"), Tying_Verification("5"), Bind_Verification("6"), Change_Verification("7");

    public String value;

    private static List<String> valueList = new ArrayList<>();

    static {
        for (ModuleEnums enums : ModuleEnums.values()) {
            valueList.add(enums.value);
        }
    }

    ModuleEnums(String value) {
        this.value = value;
    }

    /**
     * 是否存在指定的模块id
     *
     * @param value 待校验的模块id
     * @return
     */
    public static boolean containsValue(String value) {
        return valueList.contains(value);
    }
}
