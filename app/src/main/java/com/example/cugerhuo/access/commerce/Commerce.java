package com.example.cugerhuo.access.commerce;

import java.util.Date;

public class Commerce {
    /**
     * 买家id
     */
    int buyerid;

    public int getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(int buyerid) {
        this.buyerid = buyerid;
    }

    public int getCommerceid() {
        return commerceid;
    }

    public void setCommerceid(int commerceid) {
        this.commerceid = commerceid;
    }

    public int getCommodityid() {
        return commodityid;
    }

    public void setCommodityid(int commodityid) {
        this.commodityid = commodityid;
    }

    public int getSellerid() {
        return sellerid;
    }

    public void setSellerid(int sellerid) {
        this.sellerid = sellerid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * 自增主键
     */
    int commerceid;
    /**
     * 商品id
     */
    int commodityid;
    /**
     * 卖家id
     */
    int sellerid;
    /**
     * 0关闭，1等待，2完成
     */
    int  state;
    Date time;
    /**
     * 交易价格
     */
    double price;
    /**
     * 交易地点
     */
    String  place;

}
