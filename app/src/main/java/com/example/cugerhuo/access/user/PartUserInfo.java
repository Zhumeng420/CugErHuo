package com.example.cugerhuo.access.user;

import java.io.Serializable;

/**
 * 存放从redis获取的信息
 * @author 施立豪
 * @time 2023/4/10
 */
public class PartUserInfo implements Serializable {
    /**
     * id 用户id
     * imageUrl 头像url
     * signature 简介
     * username 昵称
     * isConcern 判断是否关注，其中0表示未关注，1表示已关注，2并表示互相关注，3表示未关注状态，但是关注后为互相关注
     */
    private int id;
    private String imageUrl;
    private String signature;
    private String userName;
    private int isConcern;

    public int getId() {
        return id;
    }

    public void setUserId(int userId) {
        this.id = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getConcern(){
        return isConcern;
    }
    public void setConcern(int concern){
        this.isConcern=concern;
    }


}
