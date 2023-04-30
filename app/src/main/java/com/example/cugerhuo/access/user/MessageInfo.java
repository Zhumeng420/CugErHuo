package com.example.cugerhuo.access.user;

/**
 * 消息界面实体类
 * @Author: 李柏睿
 * @Time: 2023/4/26 21:21
 */
public class MessageInfo {

    /**
     * content 最近一条消息的缩略内容
     * chatTime 获取最近一条消息的时间，单位为 ms
     */
    private String content;
    private long chatTime;

    public void setContent(String content){
        this.content=content;
    }

    public String getContent(){
        return this.content;
    }

    public void setChatTime(long time){
        this.chatTime=time;
    }
    public long getChatTime(){
        return this.chatTime;
    }
}
