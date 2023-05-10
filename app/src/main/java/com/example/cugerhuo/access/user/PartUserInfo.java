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
     * isConcern 判断是否关注，其中0表示未关注，1表示已关注，2并表示互相关注，3表示未关注状态，但是关注后为互相关注，4表示没有对粉丝进行关注，5表示与粉丝互关
     */
    private int id;
    private String imageUrl;
    private String signature;
    private String userName;
    /**
     * 自我介绍
     */
    private String selfIntroduction;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private Integer gender;

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    private String interest;
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
