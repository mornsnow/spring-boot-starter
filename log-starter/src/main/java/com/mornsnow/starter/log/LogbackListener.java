package com.mornsnow.starter.log;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author jianyang 2019/3/26
 *         <p>
 *         说明：解决logback配置加载顺序导致的logback配置加载时获取不到环境变量的问题
 */
@Component
public class LogbackListener implements ServletContextListener, EnvironmentAware {
    @Override
    public void contextInitialized(ServletContextEvent event) {

        //设置日志分组
        String logGroup = env.getProperty("logGroup", "undefined");
        System.setProperty("logGroup", logGroup);

        //设置LOGSTASH_HOST
        String logstashHost = env.getProperty("LOGSTASH_HOST_WITH_PORT");
        System.setProperty("LOGSTASH_HOST_WITH_PORT", logstashHost);

        //设置LOGSTASH_HOST
        String logstashHostTrace = env.getProperty("LOGSTASH_HOST_TRACE_WITH_PORT");
        System.setProperty("LOGSTASH_HOST_TRACE_WITH_PORT", logstashHostTrace);

        //预留5个字段，方便扩展
        System.setProperty("column1", env.getProperty("column1", "-"));
        System.setProperty("column2", env.getProperty("column2", "-"));
        System.setProperty("column3", env.getProperty("column3", "-"));
        System.setProperty("column4", env.getProperty("column4", "-"));
        System.setProperty("column5", env.getProperty("column5", "-"));

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        //lc.reset();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    @Autowired
    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    public String getProperty(String key) {
        return env.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return env.getProperty(key) == null ? defaultValue : env.getProperty(key);
    }
}