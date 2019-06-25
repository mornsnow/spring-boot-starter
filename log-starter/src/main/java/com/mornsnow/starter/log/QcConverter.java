package com.mornsnow.starter.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jianyang 2017/5/16
 */
public class QcConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {

        String key = this.getFirstOption();
        if (StringUtils.isEmpty(key) || event.getArgumentArray() == null || event.getArgumentArray().length < 1
                || !(event.getArgumentArray()[event.getArgumentArray().length - 1] instanceof LogIndex)) {
            return null;
        }
        return ((LogIndex) event.getArgumentArray()[event.getArgumentArray().length - 1]).get(key);
    }
}