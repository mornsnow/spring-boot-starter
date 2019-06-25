package com.mornsnow.starter.log;

import com.alibaba.fastjson.JSONObject;
import com.mornsnow.shared.common.basic.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

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
public class LogHandler {

    private static final QcLog log = LogUtil.getLogger(LogHandler.class);

    public static final ThreadLocal<Long> CURRENT_THREAD = new ThreadLocal<>();

    private static final int MAX_MESSAGE_LENGTH = 5000;

    @Value("${spring.application.name}")
    public String appName;

    @Value("${log.around:true}")
    public boolean logAround;

    @Around("execution(* com.mornsnow..*.*(..)) " +
            "and " +
            "( " +
                "@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
                " or execution(* com.mornsnow..*Impl.*(..))" +
                " or @annotation(com.mornsnow.starter.log.UseLog)" +
            ")"
    )
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        boolean isQc = methodName.startsWith("com.mornsnow") || methodName.startsWith("com.saluki");
        Object[] args = joinPoint.getArgs();
        if (!isQc) {
            return joinPoint.proceed(args);
        }
        long startTime = System.currentTimeMillis();

        QcLoggable loggable = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(QcLoggable.class);
        boolean logReq = logAround;
        boolean logResp = logAround;

        if (loggable != null) {
            if (loggable.value() == QcLoggable.Type.NONE) {
                logReq = false;
                logResp = false;
            } else if (loggable.value() == QcLoggable.Type.REQUEST) {
                logReq = true;
                logResp = false;
            } else if (loggable.value() == QcLoggable.Type.RESPONSE) {
                logReq = false;
                logResp = true;
            } else if (loggable.value() == QcLoggable.Type.REQUEST_RESPONSE) {
                logReq = true;
                logResp = true;
            }
        }
        Long currentTime = System.currentTimeMillis();

        boolean start = false;
        if (CURRENT_THREAD.get() == null) {
            CURRENT_THREAD.set(Thread.currentThread().getId());
            start = true;
        }

        Object obj = null;

        try {
            if (start) {
                if (logReq) {
                    if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                        //适应接口中有httpServletRequest的方法
                        ArrayList<Object> argList = new ArrayList<>();
                        for (int i = 0; i < joinPoint.getArgs().length; i++) {
                            if (joinPoint.getArgs()[i] instanceof HttpServletRequest || joinPoint.getArgs()[i] instanceof MultipartFile) {
                                continue;
                            }
                            argList.add(joinPoint.getArgs()[i]);
                        }
                        log.arroundLog(methodName, null, "[Request]" + JSONObject.toJSONString(argList).replace("{}", "{null}"));
                    } else {
                        log.arroundLog(methodName, null, "[Request] No Params");
                    }
                }
            }


            obj = joinPoint.proceed(args);
            if (start) {
                log.trace(null, Const.TRACE_TAG_SUCCESS, methodName, startTime, System.currentTimeMillis());
                if (logResp) {
                    String resp = obj == null ? "[]" : JSONObject.toJSONString(obj).replace("{}", "{null}");
                    log.arroundLog(methodName, System.currentTimeMillis() - currentTime, "[Response]" + resp);
                }
            }
        } catch (Exception e) {
            log.error("system error", e);
            if (isQc && start) {
                log.trace(null, Const.TRACE_TAG_SUCCESS, methodName, startTime, System.currentTimeMillis());
            }

            throw e;
        } finally {
            if (isQc && start) {
                CURRENT_THREAD.remove();
                ThreadContext.push(Const.THREAD_KEY_LOG_ID, "");
            }
        }
        return obj;
    }
}