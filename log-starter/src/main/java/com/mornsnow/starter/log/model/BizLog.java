package com.mornsnow.starter.log.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 业务日志
 *
 * @author joe 2016/11/20 下午10:11
 * @version BizLog, v 0.0.1 2016/11/20 下午10:11 joe Exp $$
 */
public class BizLog extends Log {

    private static final long   serialVersionUID = -5139418568134849384L;
    /**
     * 日志ID
     */
    private String              bizId;
    /**
     * 业务类型
     */
    private String              bizType;
    /**
     * 账户ID
     */
    private long                accountId;
    /**
     * 扩展字段
     */
    private Map<String, String> attrs            = new LinkedHashMap<>();

    public BizLog(String bizId, String bizType){
        this.bizId = bizId;
        this.bizType = bizType;
    }

    public BizLog(String logId, String bizType, String log){
        this(logId, bizType);
        setLog(log);
    }

    public BizLog(String logId, String bizType, String log, long accountId){
        this(logId, bizType, log);
        this.accountId = accountId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }
}
