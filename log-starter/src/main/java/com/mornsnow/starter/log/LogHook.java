package com.mornsnow.starter.log;

import org.springframework.beans.factory.InitializingBean;
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
 * @author jianyang 2017/11/28
 */
@Component
public class LogHook implements InitializingBean {

    private static final QcLog LOG = LogUtil.getLogger(LogHook.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                System.out.println("###########################");
                LOG.warn("server is down");
                System.out.println("###########################");
            }
        });
    }

}