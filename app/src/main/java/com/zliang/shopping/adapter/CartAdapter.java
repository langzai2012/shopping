package com.zliang.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zliang.shopping.R;
import com.zliang.shopping.bean.ShoppingCart;
import com.zliang.shopping.widget.NumberAddSubView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/23 0023.
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart> {

    public CartAdapter(Context context, List datas) {
        super(context, R.layout.template_cart, datas);
    }

    @Override
    protected void convert(BaseViewHoloder viewHolder, ShoppingCart item) {
        CheckBox cb = viewHolder.getView(R.id.checkbox);
        cb.setChecked(item.isChecked());

        SimpleDraweeView simpleDraweeView = viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(Uri.parse(item.getImgUrl()));

        TextView title = viewHolder.getView(R.id.text_title);
        title.setText(item.getName());

        TextView price = viewHolder.getView(R.id.text_price);
        price.setText("￥" + item.getPrice());

        NumberAddSubView addSubView = viewHolder.getView(R.id.num_control);
        addSubView.setValue(item.getCount());


    }
}
