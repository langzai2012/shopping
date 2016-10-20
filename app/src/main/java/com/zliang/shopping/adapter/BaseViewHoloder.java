package com.zliang.shopping.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class BaseViewHoloder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SparseArray<View> views;
    private BaseAdapter.OnItemClickListener mOnItemClickListener;

    public BaseViewHoloder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        this.views = new SparseArray<>();
        this.mOnItemClickListener = listener;
        itemView.setOnClickListener(this);
    }

    protected <T extends View> T getView(int viewId) {

        return retireView(viewId);

    }

    protected <T extends View> T retireView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.OnItemClick(v,getLayoutPosition());
        }
    }
}
