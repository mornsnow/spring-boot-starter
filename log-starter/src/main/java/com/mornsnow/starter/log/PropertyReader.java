package com.mornsnow.starter.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * <strong>描述：</strong> <br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2017/4/27
 */
@Component
public class PropertyReader implements EnvironmentAware {
    @Autowired
    private static Environment env;

    @Override
    public void setEnvironment(Environment env) {
        PropertyReader.env = env;
    }

    public static String getProperty(String key) {
        if (env == null) {
            return null;
        }
        return env.getProperty(key);
    }

    public static <T> T getProperty(String key, T defaultValue) {
        if (env == null) {
            return null;
        }
        return env.getProperty(key) == null ? defaultValue : (T) env.getProperty(key);
    }
}
