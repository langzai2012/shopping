package com.zliang.shopping.adapter;

import android.content.Context;
import android.widget.TextView;

import com.zliang.shopping.R;
import com.zliang.shopping.bean.Category;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
public class CategoryAdapter extends SimpleAdapter<Category> {


    public CategoryAdapter(Context context, List datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHoloder viewHolder, Category item) {
        TextView textView = viewHolder.getView(R.id.textView);
        textView.setText(item.getName());
    }
}
