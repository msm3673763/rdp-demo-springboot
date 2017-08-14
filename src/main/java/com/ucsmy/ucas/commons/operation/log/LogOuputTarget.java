package com.ucsmy.ucas.commons.operation.log;

/**
 * log输出类型
 * Created by chenqilin on 2017/4/28.
 */
public enum LogOuputTarget {

    FILE("file", "日志文件输出"),
    DATABASE("database", "数据库输出");

    LogOuputTarget(String name, String des) {

    }
}
