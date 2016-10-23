package com.zliang.shopping.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class Ware extends BaseBean implements Serializable {
    private String name;
    private String imgUrl;
    private String description;
    private Float price;
    private String sale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        return "Ware{" +
                "name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", sale='" + sale + '\'' +
                '}';
    }
}
