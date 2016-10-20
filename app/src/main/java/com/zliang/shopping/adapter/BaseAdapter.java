package com.zliang.shopping.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public abstract class BaseAdapter<T, H extends BaseViewHoloder> extends RecyclerView.Adapter<BaseViewHoloder> {
    protected Context mContext;
    protected int mLayoutResId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;


    public BaseAdapter(Context context, int layoutResId, List<T> datas) {
        this.mContext = context;
        this.mLayoutResId = layoutResId;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHoloder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mLayoutResId, parent, false);
        return new BaseViewHoloder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHoloder holder, int position) {
        T item = getItem(position);
        convert((H) holder, item);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public T getItem(int position) {
        if (position > mDatas.size()) {
            return null;
        }
        return mDatas.get(position);
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public void clearDatas() {
        if (mDatas != null && mDatas.size() > 0) {
            int itemCount = mDatas.size();
            mDatas.clear();
            //如果不添加此行代码,删除时,在添加刷新的时候,造成数据源不一致,会引起崩溃
            this.notifyItemRangeRemoved(0, itemCount);
        }
    }

    public void addDatas(List<T> data) {
        addDatas(0, data);
    }

    public void addDatas(int postion, List<T> data) {
        if (mDatas != null) {
            if (data != null && data.size() > 0) {
                mDatas.addAll(data);
                this.notifyItemRangeChanged(postion, mDatas.size());
            }
        }
    }

    protected abstract void convert(H viewHolder, T item);

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
