package com.zliang.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zliang.shopping.R;
import com.zliang.shopping.bean.Ware;

import java.util.List;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class WareAdapter extends SimpleAdapter<Ware> {

    public WareAdapter(Context context, List datas) {
        super(context, R.layout.templeate_grid_ware, datas);
    }

    @Override
    protected void convert(BaseViewHoloder viewHolder, Ware item) {
        SimpleDraweeView simpleDraweeView = viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(Uri.parse(item.getImgUrl()));
        TextView title = viewHolder.getView(R.id.text_title);
        title.setText(item.getName());
        TextView price = viewHolder.getView(R.id.text_price);
        price.setText(item.getPrice() + "");
    }


}
