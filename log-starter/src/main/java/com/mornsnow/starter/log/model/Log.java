package com.mornsnow.starter.log.model;

import java.io.Serializable;

/**
 * Log的描述:<br>
 *
 * @author joe 2016/11/20 下午8:33
 * @versiov 0.0.1 2016/11/20 下午8:33 joe
 */
public class Log implements Serializable {

    private static final long serialVersionUID = 3299487015754804371L;

    /**
     * 日志信息
     */
    private String            log;

    /**
     * 日志时间
     */
    private long              timestamp;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public long getTimestamp() {
        if (timestamp != 0) {
            return timestamp;
        }
        return System.currentTimeMillis();
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
