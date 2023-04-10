package com.example.cugerhuo.DataAccess.User;

/**
 * 存放从redis获取的信息
 * @author 施立豪
 * @time 2023/4/10
 */
public class PartUserInfo {
    private int id;

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

    private String imageUrl;
    private String signature;
    private String userName;
}
