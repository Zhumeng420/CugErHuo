package com.example.cugerhuo.access.commodity;

import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.user.PartUserInfo;

import java.util.List;

/**
 * 存储推荐信息
 */
public class RecommendInfo {
   static List<Commodity> commodityList;
    static  List<PartUserInfo> partUserInfoList;
    public static void setCommodityList(List<Commodity> a)
    {
        commodityList=a;
    }
   public static void setPartUserInfoList(List<PartUserInfo> b)
    {
        partUserInfoList=b;
    }
  public   static List<Commodity> getCommodityList()
    {
        return commodityList;
    }
   public static List<PartUserInfo> getPartUserInfoList()
    {
        return partUserInfoList;
    }
}
