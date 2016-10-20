package com.zliang.shopping.bean;

/**
 * Created by Ivan on 15/9/24.
 */
public class Category extends BaseBean {

    private String name;

    private int sort;

    public Category() {
    }

    public Category(String name) {

        this.name = name;
    }

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
