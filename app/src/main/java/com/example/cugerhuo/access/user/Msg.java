package com.example.cugerhuo.access.user;

/** 
 * 消息的实体类
 * @Author: 李柏睿
 * @Time: 2023/4/26 17:51
 */
public class Msg {
    /**TYPE_RECEIVED 表示收到的消息*/
    public static final int TYPE_RECEIVED = 0;
    /**TYPE_SEND 表示发送的消息*/
    public static final int TYPE_SEND = 1;
    /**TYPE_RECEIVED_CARD 表示对方已确认交易信息*/
    public static final int TYPE_RECEIVED_CARD = 2;
    /**TYPE_SEND_CARD 表示自己已确认交易信息*/
    public static final int TYPE_SEND_CARD = 3;
    /**TYPE_CONFIRM_CARD 表示接受到对方的提交信息*/
    public static final int TYPE_CONFIRM_CARD = 4;
    /**TYPE_RECEIVED_PIC 表示收到的图片信息*/
    public static final int TYPE_RECEIVED_PIC = 5;
    /**TYPE_SEND_PIC 表示发出的图片信息*/
    public static final int TYPE_SEND_PIC = 6;
    /**TYPE_RECEIVED_AUDIO 表示收到的语音信息*/
    public static final int TYPE_RECEIVED_AUDIO = 7;
    /**TYPE_SEND_AUDIO 表示发出的语音信息*/
    public static final int TYPE_SEND_AUDIO = 8;

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
