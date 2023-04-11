package com.example.cugerhuo.access.user;

/**
 * 全局变量，用于保存用户的Id、头像Url
 * @author  朱萌
 * @2023/4/3 22:02
 */
public class UserInfo {
    /**
     * 用户id
     */
    static int ID;
    /**
     * 头像url
     */
    static String url;
    /**
     * 获取url
     * @author 施立豪
     * @time 2023/4/8
     */
    public static String getUrl(){return url;}
    /**
     * 设置url
     * @author 施立豪
     * @time 2023/4/8
     */
    public static void setID(int id){ID=id;}
    /**
     * 获取ID
     * @author 施立豪
     * @time 2023/4/9
     */
    public static int getID(){return ID;}
    /**
     * 设置ID
     * @author 施立豪
     * @time 2023/4/9
     */
    public static void setUrl(String Url){url=Url;}
}
