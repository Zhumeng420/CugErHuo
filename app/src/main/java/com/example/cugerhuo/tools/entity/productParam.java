package com.example.cugerhuo.tools.entity;
/**
 *
 * 商品类
 * @author 施立豪
 * @date：2023/3/24
 */
public class productParam {
    private String name;
           private int productId;
    private int userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public productParam(String name, int productId, int userId) {
        this.name = name;
        this.productId = productId;
        this.userId = userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public productParam(){}

}
