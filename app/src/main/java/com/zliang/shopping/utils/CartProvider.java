package com.zliang.shopping.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.zliang.shopping.bean.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/22 0022.
 */
public class CartProvider {


    private SparseArray<ShoppingCart> datas;
    private final String CART_JSON = "cart_json";
    private Context mContext;

    public CartProvider(Context context) {
        mContext = context;
        datas = new SparseArray<>(10);
        listToSparse();
    }

    public void put(ShoppingCart cart) {

        long carId = cart.getId();
        int testId = Integer.parseInt(String.valueOf(carId));
        LogUtils.e("testId:" + testId);

        LogUtils.e("datas.size:" + datas.size());
        ShoppingCart temp = datas.get(Integer.parseInt(String.valueOf(carId)));
        if (temp != null) {
            temp.setCount(temp.getCount() + 1);
        } else {
            temp = cart;
            temp.setCount(1);
        }
        datas.put(Integer.parseInt(String.valueOf(carId)), temp);
        commit();
    }

    public void update(ShoppingCart cart) {
        datas.put(Integer.parseInt(String.valueOf(cart.getId())), cart);
        commit();
    }

    public void delete(ShoppingCart cart) {
        datas.delete(Integer.parseInt(String.valueOf(cart.getId())));
        commit();
    }

    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }

    public void commit() {
        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext, CART_JSON, JSONUtil.toJSON(carts));
    }

    private void listToSparse() {
        List<ShoppingCart> carts = getDataFromLocal();
        if (datas != null) {
            if (carts != null && carts.size() > 0) {
                for (ShoppingCart cart : carts) {
                    datas.put(Integer.valueOf(String.valueOf(cart.getId())), cart);
                }
            }
        }
        LogUtils.e("datas.size:" + datas.size());

    }

    private List<ShoppingCart> sparseToList() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(datas.valueAt(i));
        }
        return list;
    }

    public List<ShoppingCart> getDataFromLocal() {
        String json = PreferencesUtils.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if (!TextUtils.isEmpty(json)) {
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
            LogUtils.e("carts.size:" + carts.size());
        }
        return carts;
    }
}
