package com.zliang.shopping.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zliang.shopping.R;
import com.zliang.shopping.bean.Page;

import java.util.List;

/**
 * Created by Administrator on 2016/10/15 0015.
 */
public class HotGoodsAdapter extends RecyclerView.Adapter<HotGoodsAdapter.ViewHoloder> {

    private List<Page> datas;

    public HotGoodsAdapter(List<Page> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHoloder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_hot_wares, parent, false);
        return new ViewHoloder(view);
    }

    @Override
    public void onBindViewHolder(ViewHoloder holder, int position) {
        Page page = datas.get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(page.getImgUrl()));
        holder.text_title.setText(page.getName());
        holder.text_price.setText(page.getPrice() + "");
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHoloder extends RecyclerView.ViewHolder {
        SimpleDraweeView simpleDraweeView;
        TextView text_title;
        TextView text_price;

        public ViewHoloder(View view) {
            super(view);
            simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.drawee_view);
            text_title = (TextView) view.findViewById(R.id.text_title);
            text_price = (TextView) view.findViewById(R.id.text_price);
        }
    }

    public List<Page> getDatas() {
        return datas;
    }

    public void clearDatas() {
        if (datas != null && datas.size() > 0) {
            datas.clear();
        }
    }

    public void addDatas(List<Page> data) {
        addDatas(0, data);
    }

    public void addDatas(int postion, List<Page> data) {
        if (datas != null) {
            if (data != null && data.size() > 0) {
                datas.addAll(data);
                notifyItemRangeChanged(postion, datas.size());
            }
        }
    }
}
