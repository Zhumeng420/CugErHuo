package com.example.cugerhuo.tools.entity;

import java.io.Serializable;

public class TradeInfo implements Serializable {
    double price;
    String tradeTime;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTradePlace() {
        return tradePlace;
    }

    public void setTradePlace(String tradePlace) {
        this.tradePlace = tradePlace;
    }

    String tradePlace;
}
