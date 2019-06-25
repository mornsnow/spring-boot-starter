package com.mornsnow.starter.log;

import com.mornsnow.shared.common.basic.ThreadContext;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * <strong>描述：</strong> 日志<br>
 * <strong>功能：维护logId</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2017/5/8
 */
@Aspect
@Component()
public class FlushLogIdHandler {
    @After("@annotation(com.mornsnow.starter.log.FlushLogId)")
    public void after() throws Throwable {
        ThreadContext.push(Const.THREAD_KEY_LOG_ID, "");
    }
}