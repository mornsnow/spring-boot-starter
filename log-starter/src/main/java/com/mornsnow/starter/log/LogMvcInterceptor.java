package com.mornsnow.starter.log;

import com.mornsnow.shared.common.basic.ThreadContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * <strong>描述：</strong> <br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2017/6/22
 */
public class LogMvcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        //防止由于线程池影响，导致未传accountId的请求会获取到之前某次请求的accountId，当accountId为空是，ThreadContext push空字符串
        if (StringUtils.isNotEmpty(httpServletRequest.getHeader(Const.THREAD_KEY_ACCOUNT_ID))) {
            ThreadContext.push(Const.THREAD_KEY_ACCOUNT_ID, httpServletRequest.getHeader(Const.THREAD_KEY_ACCOUNT_ID));
        } else {
            ThreadContext.push(Const.THREAD_KEY_ACCOUNT_ID, "");
        }
        if (StringUtils.isNotEmpty(httpServletRequest.getHeader(Const.THREAD_KEY_USER_ID))) {
            ThreadContext.push(Const.THREAD_KEY_USER_ID, httpServletRequest.getHeader(Const.THREAD_KEY_USER_ID));
        } else {
            ThreadContext.push(Const.THREAD_KEY_USER_ID, "");
        }
        if (StringUtils.isNotEmpty(httpServletRequest.getHeader(Const.THREAD_KEY_COMPANY_ID))) {
            ThreadContext.push(Const.THREAD_KEY_COMPANY_ID, httpServletRequest.getHeader(Const.THREAD_KEY_COMPANY_ID));
        } else {
            ThreadContext.push(Const.THREAD_KEY_COMPANY_ID, "");
        }

        String ip = getIpAddress(httpServletRequest);
        if (StringUtils.isNotEmpty(ip)) {
            ThreadContext.push(Const.THREAD_KEY_CLIENT, ip);
        }

        if (StringUtils.isNotEmpty(httpServletRequest.getHeader(Const.REQUEST_HEADER_TRACE_ID))) {
            ThreadContext.push(Const.THREAD_KEY_LOG_ID, httpServletRequest.getHeader(Const.REQUEST_HEADER_TRACE_ID));
        } else if (StringUtils.isNotEmpty(httpServletRequest.getHeader(Const.REQUEST_HEADER_XB3_TRACE_ID))) {
            ThreadContext.push(Const.THREAD_KEY_LOG_ID, httpServletRequest.getHeader(Const.REQUEST_HEADER_XB3_TRACE_ID));
        } else {
            //http开头以区分接口既为rpc接口又是http接口时  调用方式为http
            ThreadContext.push(Const.THREAD_KEY_LOG_ID, "http" + UUID.randomUUID().toString().replace("-", ""));
        }

        if (StringUtils.isNotEmpty(httpServletRequest.getHeader(Const.REQUEST_HEADER_XB3_SPAN_ID))) {
            ThreadContext.push(Const.THREAD_KEY_SPAN_ID, httpServletRequest.getHeader(Const.REQUEST_HEADER_XB3_SPAN_ID));
        } else {
            ThreadContext.push(Const.THREAD_KEY_SPAN_ID, initSpanId());
        }
        ThreadContext.push(Const.THREAD_KEY_TYPE, Const.THREAD_KEY_TYPE_HTTP);

        return true;
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     */
    public final static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = "";
        try {
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            } else if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (int index = 0; index < ips.length; index++) {
                    String strIp = (String) ips[index];
                    if (!("unknown".equalsIgnoreCase(strIp))) {
                        ip = strIp;
                        break;
                    }
                }
            }
        } catch (Exception e) {

        }

        return ip;
    }

    private String initSpanId() {
        String id = UUID.randomUUID().toString().replace("-", "");
        ThreadContext.push(Const.THREAD_KEY_SPAN_ID, id);
        return id;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
