package com.mornsnow.starter.log;

import java.util.HashMap;
import java.util.Map;

/**
 * <strong>描述：</strong> <br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2017/5/16
 */
public class LogUtil {

    private static Map<String, QcLog> logMap = new HashMap<>();

    public static QcLog getLogger(Class clazz, String logCode) {
        if (logMap.containsKey(clazz.getName())) {
            return logMap.get(clazz.getName());
        } else {
            QcLog log = new QcLog(clazz, logCode);
            logMap.put(clazz.getName(), log);
            return log;
        }
    }

    public static QcLog getLogger(Class clazz) {
        if (logMap.containsKey(clazz.getName())) {
            return logMap.get(clazz.getName());
        } else {
            QcLog log = new QcLog(clazz);
            logMap.put(clazz.getName(), log);
            return log;
        }
    }

    public static QcLog getLogger(String logCode) {
        if (logMap.containsKey(logCode)) {
            return logMap.get(logCode);
        } else {
            QcLog log = new QcLog(logCode);
            logMap.put(logCode, log);
            return log;
        }
    }
}
