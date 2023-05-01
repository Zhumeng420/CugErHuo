package com.example.cugerhuo.activity.session;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

/***
 * 待确认订单
 * @Author: 李柏睿
 * @Time: 2023/5/1 20:59
 */
public class ToBeConfirmedAttachment extends CustomAttachment{

    public String title;
    public String content;

    public ToBeConfirmedAttachment() {
        super(CustomAttachmentType.ToBeConfirmedCustomMsg);
    }

    @Override
    protected void parseData(JSONObject data) {
        Log.d("flag","ToBeConfirmedAttachment==="+data.toString());
        title=data.getString("title");
        content=data.getString("content");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();

        data.put("title",title);
        data.put("content",content);

        return data;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}


