package com.example.cugerhuo.access;

import java.io.Serializable;
import java.util.Date;

public class Commodity  implements Serializable {
       /**
         * 商品id
         */

        private Integer id;
        /**
         * 用户id
         */
        private Integer userId;
        /**
         * 商品描述
         */
        private String description;

        /**
         * 图片网址,多个网站以英文分号;分割
         */
        private String url1;
        /**
         * 视频网址
         */
        private String url2;
        /**
         * 定价
         */
        private Float price;
        /**
         * 发布时间
         */
        private Date time;
        /**
         * 状态
         */
        private Boolean state;
        /**
         * 分类，多个分类以|分开
         */
        private String category;
        /**
         * 成色
         */
        private String quality;
        /**
         * 购买渠道
         */
        private String purchasechannel;
        /**
         * 原价
         */
        private float originalprice;

 public Commodity() {
 }

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public Integer getUserId() {
  return userId;
 }

 public void setUserId(Integer userId) {
  this.userId = userId;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public String getUrl1() {
  return url1;
 }

 public void setUrl1(String url1) {
  this.url1 = url1;
 }

 public String getUrl2() {
  return url2;
 }

 public void setUrl2(String url2) {
  this.url2 = url2;
 }

 public Float getPrice() {
  return price;
 }

 public void setPrice(Float price) {
  this.price = price;
 }

 public Date getTime() {
  return time;
 }

 public void setTime(Date time) {
  this.time = time;
 }

 public Boolean getState() {
  return state;
 }

 public void setState(Boolean state) {
  this.state = state;
 }

 public String getCategory() {
  return category;
 }

 public void setCategory(String category) {
  this.category = category;
 }

 public String getQuality() {
  return quality;
 }

 public void setQuality(String quality) {
  this.quality = quality;
 }

 public String getPurchasechannel() {
  return purchasechannel;
 }

 public void setPurchasechannel(String purchasechannel) {
  this.purchasechannel = purchasechannel;
 }

 public float getOriginalprice() {
  return originalprice;
 }

 public void setOriginalprice(float originalprice) {
  this.originalprice = originalprice;
 }

 public String getBrand() {
  return brand;
 }

 public void setBrand(String brand) {
  this.brand = brand;
 }

 public Commodity(Integer id, Integer userId, String description, String url1, String url2, Float price, Date time, Boolean state, String category, String quality, String purchasechannel, float originalprice, String brand) {
  this.id = id;
  this.userId = userId;
  this.description = description;
  this.url1 = url1;
  this.url2 = url2;
  this.price = price;
  this.time = time;
  this.state = state;
  this.category = category;
  this.quality = quality;
  this.purchasechannel = purchasechannel;
  this.originalprice = originalprice;
  this.brand = brand;
 }

 /**
         * 品牌
         */
        private String brand;
}
