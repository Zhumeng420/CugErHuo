package com.example.cugerhuo.Activity.adapter;

/**
 * @Author 唐小莉
 * @Description 创建concern类，定义被关注人的信息进行数据存储
 * @Date 2023/4/7 22：18
 */
public class Concern {
    /**
     * concern_name 关注人的昵称
     * concern_imgPath 关注人的头像
     * concern_desc 关注人的自我介绍
     * @author 唐小莉
     * @Time 2023/4/7 22：16
     */
    private String concern_name;
    private String concern_imgPath;
    private String concern_desc;

    public Concern(String name, String imgPath,String desc) {
        this.concern_name=name;
        this.concern_imgPath=imgPath;
        this.concern_desc=desc;
    }

    /**
     * 获取昵称
     * @return 返回昵称
     * @author 唐小莉
     * @time 2023/4/7 22:23
     */
    public String getName() {
        return concern_name;
    }

    /**
     * 获取头像路径
     * @return 返回头像路径
     * @author 唐小莉
     * @time 2023/4/7 22:23
     */
    public String getConcern_imgPath() {
        return concern_imgPath;
    }
    /**
     * 获取自我介绍
     * @return 返回头自我介绍
     * @author 唐小莉
     * @time 2023/4/7 22:23
     */
    public String getConcern_desc(){
        return concern_desc;
    }
}
