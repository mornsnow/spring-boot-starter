package com.mornsnow.starter.init;

import com.mornsnow.starter.log.LogUtil;
import com.mornsnow.starter.log.QcLog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jianyang 2018/12/28
 */
@Component
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryPostProcessor {

    private static final QcLog logger = LogUtil.getLogger(SpringValueProcessor.class);


    public static final ConcurrentHashMap<String, List<SpringValue>> QC_BEAN_MAP = new ConcurrentHashMap<>();


    /**
     * 匹配花括号的正则
     */
    private static Pattern p = Pattern.compile("(\\$\\{[^\\}]*\\})");

    @SuppressWarnings("all")
    private static final String QC_PACKAGE_PREFIX = "com.mornsnow";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        processBeanPropertyValues(bean, beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    private void processBeanPropertyValues(Object bean, String beanName) {
        String className = bean.getClass().getName();

        if (className != null && className.startsWith(QC_PACKAGE_PREFIX)) {
            processor(bean, beanName);
        }
    }

    private void processor(Object bean, String beanName) {

        RefreshScope refreshScope = bean.getClass().getDeclaredAnnotation(RefreshScope.class);
        //添加了refreshScope注解的类 spring会进行动态刷新
        if (refreshScope != null) {
            return;
        }

        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            Value value = field.getAnnotation(Value.class);

            if (value == null) {
                continue;
            }

            SpringValue springValue = new SpringValue(value.value(), bean, beanName, field);

            List<String> keySet = extractKeyByRegular(value.value());

            for (String key : keySet) {
                String k = key.split(":")[0];
                QC_BEAN_MAP.putIfAbsent(k, new ArrayList<>());
                QC_BEAN_MAP.get(k).add(springValue);
            }

        }

    }

    public static List<String> extractKeyByRegular(String placeholder) {
        List<String> list = new ArrayList<>();
        Matcher m = p.matcher(placeholder);
        while (m.find()) {
            list.add(m.group().substring(2, m.group().length() - 1));
        }
        return list;
    }

}
