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
 * @author jianyang 2017/6/21
 */
public class QcLogCache {
    private String info;
    private Object[] vars;
    private BizLogLevel level;
    private boolean isBizLog;
    private Throwable e;

    public QcLogCache() {

    }

    public QcLogCache(String info, Object[] vars, BizLogLevel level, boolean isBizLog) {
        this.info = info;
        this.vars = vars;
        this.level = level;
        this.isBizLog = isBizLog;
    }

    public QcLogCache(String info, Object[] vars, Throwable e, BizLogLevel level) {
        this.info = info;
        this.vars = vars;
        this.level = level;
        this.isBizLog = false;
        this.e = e;
    }


    public boolean isBizLog() {
        return isBizLog;
    }

    public void setBizLog(boolean bizLog) {
        isBizLog = bizLog;
    }

    public BizLogLevel getLevel() {
        return level;
    }

    public void setLevel(BizLogLevel level) {
        this.level = level;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object[] getVars() {
        return vars;
    }

    public void setVars(Object[] vars) {
        this.vars = vars;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }
}
