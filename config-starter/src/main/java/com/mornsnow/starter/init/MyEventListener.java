package com.mornsnow.starter.init;

import com.mornsnow.starter.log.LogUtil;
import com.mornsnow.starter.log.QcLog;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mornsnow.starter.init.SpringValueProcessor.QC_BEAN_MAP;


/**
 * 监听环境变量变化  刷新@Value注解的属性值
 *
 * @author jianyang 2018/12/28
 */
@Component
public class MyEventListener implements ApplicationListener<EnvironmentChangeEvent> {

    private static final QcLog logger = LogUtil.getLogger(MyEventListener.class);

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {

        logger.info("event changed");
        for (String key : event.getKeys()) {

            logger.warn("key:{}", key);
            try {
                if (!QC_BEAN_MAP.containsKey(key)) {
                    logger.warn("QC_BEAN_MAP not contains key:{}", key);
                    continue;
                }

                List<SpringValue> springValues = QC_BEAN_MAP.get(key);
                logger.info("springValus size:{}", springValues.size());

                for (SpringValue value : springValues) {
                    value.update();
                }

            } catch (Exception e) {
                logger.error("refresh properties fail, key:{}", key, e);
            }

        }

    }

}
