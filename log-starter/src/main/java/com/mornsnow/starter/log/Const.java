package com.mornsnow.starter.log;

/**
 * <strong>描述：</strong> <br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2018/5/15
 */
public class Const {

    public static final String THREAD_KEY_TYPE = "TYPE";
    public static final String THREAD_KEY_TYPE_GRPC = "GRPC";
    public static final String THREAD_KEY_TYPE_HTTP = "HTTP";
    public static final String THREAD_KEY_SPAN_ID = "SPAN_ID";
    public static final String THREAD_KEY_PARENT_ID = "PARENT_ID";
    public static final String THREAD_KEY_LOG_ID = "LOG_ID";
    public static final String THREAD_KEY_CLIENT = "client";
    public static final String THREAD_KEY_TRACE_ID = "TRACE_ID";
    public static final String THREAD_KEY_USER_ID = "userId";
    public static final String THREAD_KEY_ACCOUNT_ID = "accountId";
    public static final String THREAD_KEY_COMPANY_ID = "companyId";
    public static final String THREAD_KEY_TOKEN = "token";
    public static final String THREAD_KEY_PTOKEN = "pToken";


    public static final String REQUEST_HEADER_TRACE_ID = "x-trace-id";
    public static final String REQUEST_HEADER_XB3_TRACE_ID = "x-b3-traceid";
    public static final String REQUEST_HEADER_XB3_SPAN_ID = "x-b3-spanid";

    public static final String TRACE_TAG_SUCCESS = "SUCCESS";
    public static final String TRACE_TAG_ERROR = "ERROR";
}
