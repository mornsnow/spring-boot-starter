package com.mornsnow.starter.log;

/**
 * <strong>描述：</strong> 日志异常<br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2017/5/16
 */
public class LogException extends RuntimeException {
    public LogException(String errMsg) {
        super(errMsg);
    }
}
