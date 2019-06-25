package com.mornsnow.starter.log;

/**
 * <strong>描述：</strong>业务日志级别<br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 */
public enum BizLogLevel {

    Error("error"), WARN("warn"), INFO("info");

    private String code;

    private BizLogLevel(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}

