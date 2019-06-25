package com.mornsnow.starter.log.model;

/**
 *  系统异常日志
 *
 * @author joe 2016/11/20 下午10:10
 * @version SysLog, v 0.0.1 2016/11/20 下午10:10 joe
 */
public class SysLog extends Log {
    private static final long serialVersionUID = -1710821390555637758L;

    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
