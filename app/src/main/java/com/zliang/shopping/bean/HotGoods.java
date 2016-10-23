package com.zliang.shopping.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/15 0015.
 * <p/>
 * 热卖商品
 */
public class HotGoods implements Serializable {
    private int totalCount;
    private int currentPage;
    private int pageSize;
    private int totalPage;
    private List<Ware> list;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Ware> getList() {
        return list;
    }

    public void setList(List<Ware> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "HotGoods{" +
                "totalCount=" + totalCount +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", list=" + list +
                '}';
    }
}
