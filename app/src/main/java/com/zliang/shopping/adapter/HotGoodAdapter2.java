package com.zliang.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zliang.shopping.R;
import com.zliang.shopping.bean.Page;

import java.util.List;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class HotGoodAdapter2 extends SimpleAdapter<Page> {
    public HotGoodAdapter2(Context context, int layoutResId, List datas) {
        super(context, layoutResId, datas);
    }

    @Override
    protected void convert(BaseViewHoloder viewHolder, Page item) {
        SimpleDraweeView simpleDraweeView = viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(Uri.parse(item.getImgUrl()));
        TextView text_title = viewHolder.getView(R.id.text_title);
        text_title.setText(item.getName());
        TextView text_price = viewHolder.getView(R.id.text_price);
        text_price.setText(item.getPrice());
    }
}
