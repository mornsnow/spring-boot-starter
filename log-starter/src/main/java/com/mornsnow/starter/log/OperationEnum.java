package com.mornsnow.starter.log;

/**
 * <strong>描述：</strong>操作类型枚举 <br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 */
public enum OperationEnum {

    CREATE("CREATE", "添加"), DELETED("DELETE", "删除"), UPDATE("UPDATE", "更新"), QUERY("QUERY", "查询"), CONFIRM("COMFIRM", "确认"), DINY("DINY", "否认");

    private String code;

    private String name;

    private OperationEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

