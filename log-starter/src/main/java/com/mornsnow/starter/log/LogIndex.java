package com.mornsnow.starter.log;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <strong>描述：</strong> 自定义日志<br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2017/5/16
 */
@Component
public class LogIndex {
    private Map<String, String> params = new HashMap<>();

    public boolean containsKey(String key) {
        return params != null && params.containsKey(key);
    }


    public LogIndex(String key, String value) {
        params.put(key, value);
    }

    public LogIndex() {
    }

    public LogIndex add(String key, String value) {
        params.put(key, value);
        return this;
    }

    public String get(String key) {
        return params.get(key);
    }
}
