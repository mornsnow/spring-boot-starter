package com.mornsnow.starter.init;

import com.mornsnow.starter.log.LogUtil;
import com.mornsnow.starter.log.QcLog;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author jianyang 2018/12/28
 */
public class SpringValue {

    /**
     * 类属性字段
     */
    private Field field;

    /**
     * 对需要动态刷新配置类实例的引用
     */
    private WeakReference<Object> beanRef;

    private String beanName;

    /**
     * @Value注解placeholder
     */
    private String placeholder;

    private static final QcLog logger = LogUtil.getLogger(SpringValue.class);

    public SpringValue(String placeholder, Object bean, String beanName, Field field) {
        this.beanRef = new WeakReference<>(bean);
        this.beanName = beanName;
        this.field = field;
        this.placeholder = placeholder;
    }

    public void update() throws IllegalAccessException, InvocationTargetException {

        List<String> keys = SpringValueProcessor.extractKeyByRegular(placeholder);

        String value = placeholder;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String[] ksp = key.split(":");
            String k = ksp[0];
            String defaultValue = ksp.length == 2 ? ksp[1] : null;
            String v = defaultValue == null ? SpringPropertyReaderConfig.getProperty(k) : SpringPropertyReaderConfig.getProperty(k, defaultValue);
            value = value.replace("${" + key + "}", v);
        }

        injectField(ConvertUtils.convert(value, field.getType()));
    }

    private void injectField(Object newVal) throws IllegalAccessException {
        Object bean = beanRef.get();
        if (bean == null) {
            logger.info("bean is null");
            return;
        }
        logger.info("bean:{}", bean.toString());
        logger.info("bean hashcode:{}", bean.hashCode());
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(bean, newVal);
        field.setAccessible(accessible);
    }

}
