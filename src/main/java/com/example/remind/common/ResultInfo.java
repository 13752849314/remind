package com.example.remind.common;

public enum ResultInfo {
    NOT_FOUND("404", "页面走丢了!"),
    SUCCESS("200", "操作成功!"),
    GLOBAL_ERROR("101","系统繁忙!");
    private String code;
    private String message;

    ResultInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
