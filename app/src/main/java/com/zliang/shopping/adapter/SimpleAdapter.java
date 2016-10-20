package com.zliang.shopping.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHoloder> {

    public SimpleAdapter(Context context, int layoutResId, List datas) {
        super(context, layoutResId, datas);
    }

}
