package com.example.cugerhuo.tools.entity;


/**
 * 公共返回对象
 * @Author: 朱佳睿
 * @Time: 2023.03.29
 */
public class RespBean {
    /**返回的状态码*/
    private long code;

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getObject() {
        return object;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    /**相应的提示信息*/
    private String message;
    /**准备返回对象*/
    private Object object;
    public RespBean(){}
}
