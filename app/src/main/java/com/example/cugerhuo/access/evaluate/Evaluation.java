package com.example.cugerhuo.access.evaluate;

import java.sql.Timestamp;

/**
 * 商品评价类
 * @author carollkarry
 * @time 2023/5/9
 */
public class Evaluation {
    private int userid;
    private int commerid;
    private String content;
    private Timestamp time;
    private int score;
    private int state;

    public void setUserid(int id){
        this.userid=id;
    }
    public int getUserid(){
        return userid;
    }
    public void setCommerid(int id){
        this.commerid=id;
    }
    public int getCommerid(){
        return commerid;
    }
    public void setContent(String content){
        this.content=content;
    }
    public String getContent(){
        return this.content;
    }
    public void setTime(Timestamp time){
        this.time=time;
    }
    public Timestamp getTime(){
        return this.time;
    }
    public void setScore(int score){
        this.score=score;
    }
    public int getScore(){
        return this.score;
    }
    public void setState(int state){
        this.state=state;
    }
    public int getState(){
        return this.state;
    }
}
