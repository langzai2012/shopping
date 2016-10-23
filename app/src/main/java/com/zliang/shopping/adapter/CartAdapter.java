package com.zliang.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.db.annotation.Check;
import com.zliang.shopping.R;
import com.zliang.shopping.bean.ShoppingCart;
import com.zliang.shopping.utils.CartProvider;
import com.zliang.shopping.widget.NumberAddSubView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/10/23 0023.
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {
    private CheckBox checkBox;
    private TextView tvTotalPrice;
    private List<ShoppingCart> datas;
    private CartProvider cartProvider;

    public CartAdapter(Context context, List<ShoppingCart> datas, CheckBox checkBox, TextView tvTotalPrice) {
        super(context, R.layout.template_cart, datas);
        this.checkBox = checkBox;
        this.tvTotalPrice = tvTotalPrice;
        cartProvider = new CartProvider(context);
        this.datas = datas;
        showTotalPrice();
        setOnItemClickListener(this);
        checkBox(checkBox);
    }

    @Override
    protected void convert(BaseViewHoloder viewHolder, final ShoppingCart item) {
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

        addSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }
        });


    }

    private void checkBox(final CheckBox checkBox) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }

    public void checkAll(boolean isChecked) {
        if (isNull()) {
            return;
        }
        int i = 0;
        for (ShoppingCart cart : datas) {
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }

    public void showTotalPrice() {
        int total = (int) getTotalPrice();
        tvTotalPrice.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    private float getTotalPrice() {
        int sum = 0;
        if (isNull()) {
            return sum;
        }

        for (ShoppingCart cart : datas) {
            if (cart.isChecked()) {
                sum += cart.getCount() * cart.getPrice();
            }
        }
        return sum;
    }

    private boolean isNull() {
        if (datas == null || datas.size() <= 0) {
            return true;
        }

        return false;

    }

    @Override
    public void OnItemClick(View view, int position) {
        ShoppingCart cart = getItem(position);
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(position);
        checkListenen();
        showTotalPrice();
    }

    private void checkListenen() {
        int count = 0;
        int checkNum = 0;
        if (datas != null) {
            count = datas.size();
            for (ShoppingCart cart : datas) {
                if (!cart.isChecked()) {
                    checkBox.setChecked(false);
                    break;
                } else {
                    checkNum += 1;
                }
            }

            if (count == checkNum) {
                checkBox.setChecked(true);
            }
        }

    }

    public void deleteCart() {
        if (isNull()) {
            return;
        }

        //这样删除会有问题,遍历的时候,删除数据会引起遍历后数组发生变化
//        for (ShoppingCart cart : datas){
////
////            if(cart.isChecked()){
////                int position = datas.indexOf(cart);
////                cartProvider.delete(cart);
////                datas.remove(cart);
////                notifyItemRemoved(position);
////            }
////        }
        for (Iterator<ShoppingCart> iterator = datas.iterator(); iterator.hasNext(); ) {
            ShoppingCart cart = iterator.next();
            if (cart.isChecked()) {
                int position = datas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
    }
}
