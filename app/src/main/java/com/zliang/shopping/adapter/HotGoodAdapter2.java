package com.zliang.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zliang.shopping.R;
import com.zliang.shopping.bean.Page;
import com.zliang.shopping.bean.ShoppingCart;
import com.zliang.shopping.utils.CartProvider;
import com.zliang.shopping.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class HotGoodAdapter2 extends SimpleAdapter<Page> {
    private CartProvider mCartProvider;

    public HotGoodAdapter2(Context context, int layoutResId, List datas) {
        super(context, layoutResId, datas);
        mCartProvider = new CartProvider(context);
    }

    @Override
    protected void convert(BaseViewHoloder viewHolder, final Page item) {
        SimpleDraweeView simpleDraweeView = viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(Uri.parse(item.getImgUrl()));
        TextView text_title = viewHolder.getView(R.id.text_title);
        text_title.setText(item.getName());
        TextView text_price = viewHolder.getView(R.id.text_price);
        text_price.setText(item.getPrice());

        Button btnAdd = viewHolder.getView(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCart cart = convertData(item);
                mCartProvider.put(cart);
                ToastUtils.show(mContext, "已添加到购物车", Toast.LENGTH_LONG);
            }
        });
    }

    private ShoppingCart convertData(Page ware) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(ware.getId());
        cart.setDescription(ware.getDescription());
        cart.setImgUrl(ware.getImgUrl());
        cart.setPrice(ware.getPrice());
        cart.setName(ware.getName());
        return cart;

    }
}
