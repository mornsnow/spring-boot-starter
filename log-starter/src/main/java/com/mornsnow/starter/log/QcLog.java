package com.mornsnow.starter.log;

import brave.Tracing;
import brave.internal.HexCodec;
import com.mornsnow.shared.common.basic.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.UUID;

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
@RefreshScope
public class QcLog {
    private Logger log = LoggerFactory.getLogger("SYS");
    public static volatile Logger traceLog = LoggerFactory.getLogger("TRACE");
    private String logCode;
    private static final String LOG_ID = "logId";
    private static String host;

    @Value("${dropTrace:false}")
    public boolean dropTrace;

    public QcLog(Class clazz, String logCode) {
        this.log = LoggerFactory.getLogger(clazz);
        this.logCode = logCode;
    }

    public QcLog(Class clazz) {
        this.log = LoggerFactory.getLogger(clazz);
        this.logCode = "";
    }

    public QcLog(String logCode) {
        this.log = LoggerFactory.getLogger(logCode);
        this.logCode = logCode;
    }

    public QcLog() {
    }

    public void error(String logInfo, Object... vars) {
        if (log.isErrorEnabled()) {
            if (vars == null || vars.length == 0) {
                log.error(logInfo, formatVars(vars));
            } else {
                Throwable e;
                if (vars[vars.length - 1] instanceof Throwable) {
                    Object[] formattedVars = formatVars(vars);
                    e = (Throwable) formattedVars[vars.length - 1];
                    Object[] tempVars = Arrays.copyOf(formattedVars, formattedVars.length - 2);
                    log.error(logInfo + "\n", tempVars, formattedVars[formattedVars.length - 1], e);
                } else {
                    log.error(logInfo, formatVars(vars));
                }
            }
        }
    }

    /**
     * @deprecated replaced by error()
     */
    @Deprecated
    public void errorNoCache(String logInfo, Object... vars) {
        error(logInfo, vars);
    }

    public void warn(String logInfo, Object... vars) {
        if (log.isWarnEnabled()) {
            if (vars == null || vars.length == 0) {
                log.warn(logInfo, formatVars(vars));
            } else {
                Throwable e;
                if (vars[vars.length - 1] instanceof Throwable) {
                    Object[] formattedVars = formatVars(vars);
                    e = (Throwable) formattedVars[vars.length - 1];
                    Object[] tempVars = Arrays.copyOf(formattedVars, formattedVars.length - 2);
                    log.warn(logInfo + "\n", tempVars, formattedVars[formattedVars.length - 1], e);
                } else {
                    log.warn(logInfo, formatVars(vars));
                }
            }
        }
    }

    /**
     * @deprecated replaced by warn()
     */
    @Deprecated
    public void warnNoCache(String logInfo, Object... vars) {
        warn(logInfo, vars);
    }

    public void info(String logInfo, Object... vars) {
        if (log.isInfoEnabled()) {
            log.info(logInfo, formatVars(vars));
        }
    }


    public void trace(String type, String tag, String methodName, long startTime, long endTime) {

        if (dropTrace) {
            return;
        }
        String traceId = getLogId();
        if (type == null) {
            type = getType();
        }
        LogIndex logIndex = new LogIndex("type", type)
                .add("tag", tag)
                .add("host_ip", getHost())
                .add("span_id", getSpanId())
                .add("parent_id", getParentId())
                .add("trace_id", traceId)
                .add("log_id", traceId)
                .add("method_name", methodName)
                .add("user_id", getAccountId())
                .add("start_time", startTime + "")
                .add("end_time", endTime + "")
                .add("duration", endTime - startTime + "");

        traceLog.error("", logIndex);
    }

    /**
     * @deprecated replaced by info()
     */
    @Deprecated
    public void infoNoCache(String logInfo, Object... vars) {
        info(logInfo, vars);
    }

    public void debug(String logInfo, Object... vars) {
        if (log.isDebugEnabled()) {
            log.debug(logInfo, formatVars(vars));
        }
    }

    /**
     * @deprecated replaced by info(),warn(),error()
     */
    @Deprecated
    public void bizLog(OperationEnum operation, String logInfo, Object... vars) {
        if (log.isWarnEnabled()) {
            log.warn(logInfo, formatVars(vars));
        }
    }

    /**
     * @deprecated replaced by info(),warn(),error()
     */
    @Deprecated
    public void bizLog(String logInfo, Object... vars) {
        if (log.isWarnEnabled()) {
            log.warn(logInfo, formatVars(vars));
        }
    }

    public void arroundLog(String methodName, Long methodTime, String logInfo) {
        if (log.isWarnEnabled()) {
            log.warn(logInfo, formatVars(methodName, methodTime, logCode, null));
        }
    }

    private Object[] formatVars(Object[] vars) {
        StackTraceElement st = Thread.currentThread().getStackTrace()[3];
        if (this.getClass().getName().equals(st.getClassName())) {
            st = Thread.currentThread().getStackTrace()[4];
        }
        String methodName = st.getClassName() + "." + st.getMethodName() + "[" + st.getLineNumber() + "]";
        return formatVars(methodName, null, "", vars);
    }

    private Object[] formatVars(String methodName, Long methodTime, String logCode, Object[] vars) {
        int varLength = vars == null ? 0 : vars.length;
        if (varLength > 0) {
            if ((vars[varLength - 1] instanceof LogIndex)) {
                fillLogIndex(((LogIndex) vars[varLength - 1]), methodName, methodTime, logCode);
                return vars;
            } else {
                Object[] newVars = Arrays.copyOf(vars, varLength + 1);
                LogIndex params = new LogIndex();
                fillLogIndex(params, methodName, methodTime, logCode);
                newVars[varLength] = params;
                return newVars;
            }
        } else {
            Object[] newVars = new Object[1];
            LogIndex params = new LogIndex();
            fillLogIndex(params, methodName, methodTime, logCode);
            newVars[0] = params;
            return newVars;
        }
    }

    private void fillLogIndex(LogIndex logIndex, String methodName, Long methodTime, String logCode) {
        logIndex.add("methodName", methodName)
                .add("executeTime", methodTime == null ? null : Long.toString(methodTime))
                .add(LOG_ID, getLogId())
                .add("appName", PropertyReader.getProperty("spring.application.name"))
                .add("indexGroup", PropertyReader.getProperty("indexGroup", "default"))
                .add("parentId", getParentId())
                .add("spanId", getSpanId())
                .add(Const.THREAD_KEY_CLIENT, getClientHost())
                .add(Const.THREAD_KEY_ACCOUNT_ID, getAccountId());
        if (!logIndex.containsKey("logCode")) {
            logIndex.add("logCode", logCode);
        }

    }

    private static String getLogId() {


        if (Tracing.current() != null
                && Tracing.current().tracer() != null
                && Tracing.current().tracer().currentSpan() != null
                && Tracing.current().tracer().currentSpan().context() != null) {
            return Tracing.current().tracer().currentSpan().context().traceIdString();
        }

        Object logIdObj = ThreadContext.get(Const.THREAD_KEY_LOG_ID);
        if (logIdObj == null || logIdObj.toString().equals("")) {
            String id = UUID.randomUUID().toString().replace("-", "");
            ThreadContext.push(Const.THREAD_KEY_LOG_ID, id);
            return id;
        }

        return logIdObj.toString();
    }

    private static String getClientHost() {
        return ThreadContext.get(Const.THREAD_KEY_CLIENT) == null ? ""
                : ThreadContext.get(Const.THREAD_KEY_CLIENT).toString();
    }

    private static String getAccountId() {
        return ThreadContext.get(Const.THREAD_KEY_ACCOUNT_ID) == null ? ""
                : ThreadContext.get(Const.THREAD_KEY_ACCOUNT_ID).toString();
    }

    private static String getType() {
        return ThreadContext.get(Const.THREAD_KEY_TYPE) == null ? "" : ThreadContext.get(Const.THREAD_KEY_TYPE).toString();
    }

    private static String getParentId() {
        return ThreadContext.get(Const.THREAD_KEY_PARENT_ID) == null ? "" : ThreadContext.get(Const.THREAD_KEY_PARENT_ID).toString();
    }

    private static String getUserId() {
        return ThreadContext.get(Const.THREAD_KEY_USER_ID) == null ? "" : ThreadContext.get(Const.THREAD_KEY_USER_ID).toString();
    }

    public static String getSpanId() {


        if (Tracing.current() != null
                && Tracing.current().tracer() != null
                && Tracing.current().tracer().currentSpan() != null
                && Tracing.current().tracer().currentSpan().context() != null) {

            char[] result = new char[16];
            HexCodec.writeHexLong(result, 0, Tracing.current().tracer().currentSpan().context().spanId());
            return new String(result);
        }
        
        Object logIdObj = ThreadContext.get(Const.THREAD_KEY_SPAN_ID);
        if (logIdObj == null || logIdObj.toString().equals("")) {
            String id = UUID.randomUUID().toString().replace("-", "");
            ThreadContext.push(Const.THREAD_KEY_SPAN_ID, id);
            return id;
        }

        return logIdObj.toString();
    }

    private static String getHost() {

        if (host == null) {
            try {
                host = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {

            }
        }
        return host;
    }

    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }
}
