package com.example.cugerhuo.access;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取关注用户商品的实体
 * @author carollkarry
 * @time 2023/5/4
 */
public class ConcernCommodity {
    private int page=1;
    private List<Integer> users=new ArrayList<>();

    public void setPage(int page){
        this.page=page;
    }

    public int getPage(){
        return this.page;
    }

    public void setUsers(List<Integer> users){
        this.users=users;
    }

    public List<Integer> getUsers(){
        return this.users;
    }
}
