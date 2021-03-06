package com.zliang.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zliang.shopping.R;
import com.zliang.shopping.bean.ShoppingCart;
import com.zliang.shopping.bean.Ware;
import com.zliang.shopping.utils.CartProvider;
import com.zliang.shopping.utils.LogUtils;
import com.zliang.shopping.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class HotGoodAdapter2 extends SimpleAdapter<Ware> {
    private CartProvider mCartProvider;

    public HotGoodAdapter2(Context context, List<Ware> datas) {
        super(context, R.layout.template_hot_wares, datas);
        mCartProvider = new CartProvider(context);
    }

    @Override
    protected void convert(BaseViewHoloder viewHolder, final Ware item) {
        LogUtils.e("BaseViewHoloder:" + viewHolder);
        SimpleDraweeView simpleDraweeView = viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(Uri.parse(item.getImgUrl()));
        TextView text_title = viewHolder.getView(R.id.text_title);
        if (text_title != null) {
            text_title.setText(item.getName());
        }
        TextView text_price = viewHolder.getView(R.id.text_price);

        if (text_price != null) {
            text_price.setText(item.getPrice() + "");
        }
        Button btnAdd = viewHolder.getView(R.id.btn_add);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart cart = convertData(item);
                    mCartProvider.put(cart);
                    ToastUtils.show(mContext, "已添加到购物车", Toast.LENGTH_LONG);
                }
            });
        }

    }

    private ShoppingCart convertData(Ware ware) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(ware.getId());
        cart.setDescription(ware.getDescription());
        cart.setImgUrl(ware.getImgUrl());
        cart.setPrice(ware.getPrice());
        cart.setName(ware.getName());
        return cart;
    }

    public void resetLayout(int layoutId) {
        this.mLayoutResId = layoutId;
        notifyItemChanged(0, getDatas().size());
    }
}
