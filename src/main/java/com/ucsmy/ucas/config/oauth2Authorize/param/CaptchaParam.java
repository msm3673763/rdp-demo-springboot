package com.ucsmy.ucas.config.oauth2Authorize.param;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 验证码策略配置参数
 * Created by cui on 2017/6/2.
 */
@Component
@ConfigurationProperties
public class CaptchaParam {
    private String border = "yes";// 图片边框，合法值：yes , no
    private String borderColor = "black";// 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.
    private int borderThickness = 1;// 边框厚度，合法值：>0
    private String fontColor = "blue";// 字体颜色，合法值： r,g,b  或者 white,black,blue
    private String fontNames = "宋体,楷体,微软雅黑";// 字体
    private int fontSize = 30;// 字体大小
    private int imageWidth = 90;// 图片宽
    private int imageHeight = 35;// 图片高
    private int charLength = 4;// 验证码长度
    private int charSpace = 1;// 文字间隔
    private String charString = "0123456789";// 文本集合，验证码值从此集合中获取
    private String noiseColor = "black";// 干扰 颜色，合法值： r,g,b 或者 white,black,blue.

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontNames() {
        return fontNames;
    }

    public void setFontNames(String fontNames) {
        this.fontNames = fontNames;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getCharLength() {
        return charLength;
    }

    public void setCharLength(int charLength) {
        this.charLength = charLength;
    }

    public int getCharSpace() {
        return charSpace;
    }

    public void setCharSpace(int charSpace) {
        this.charSpace = charSpace;
    }

    public String getCharString() {
        return charString;
    }

    public void setCharString(String charString) {
        this.charString = charString;
    }

    public String getNoiseColor() {
        return noiseColor;
    }

    public void setNoiseColor(String noiseColor) {
        this.noiseColor = noiseColor;
    }
}
