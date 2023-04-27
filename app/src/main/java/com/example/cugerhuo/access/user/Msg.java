package com.example.cugerhuo.access.user;

/** 
 * 消息的实体类
 * @Author: 李柏睿
 * @Time: 2023/4/26 17:51
 */
public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;
    /**content 表示消息的内容*/
    private String content;
    /**type 表示消息的类型*/
    private int type;

    public Msg(String content,int type){
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
