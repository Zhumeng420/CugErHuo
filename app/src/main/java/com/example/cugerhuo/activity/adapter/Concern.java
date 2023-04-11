package com.example.cugerhuo.activity.adapter;

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
    private String concernName;
    private String concernImgPath;
    private String concernDesc;

    public Concern(String name, String imgPath,String desc) {
        this.concernName =name;
        this.concernImgPath =imgPath;
        this.concernDesc =desc;
    }

    /**
     * 获取昵称
     * @return 返回昵称
     * @author 唐小莉
     * @time 2023/4/7 22:23
     */
    public String getName() {
        return concernName;
    }

    /**
     * 获取头像路径
     * @return 返回头像路径
     * @author 唐小莉
     * @time 2023/4/7 22:23
     */
    public String getConcernImgPath() {
        return concernImgPath;
    }
    /**
     * 获取自我介绍
     * @return 返回头自我介绍
     * @author 唐小莉
     * @time 2023/4/7 22:23
     */
    public String getConcernDesc(){
        return concernDesc;
    }
}
