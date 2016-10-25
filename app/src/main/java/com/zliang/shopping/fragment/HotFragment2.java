package com.zliang.shopping.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.zliang.shopping.Config.ConstantsValues;
import com.zliang.shopping.R;
import com.zliang.shopping.adapter.HotGoodAdapter2;
import com.zliang.shopping.utils.Pager;


/**
 * Created by Ivan on 15/9/22.
 */
public class HotFragment2 extends Fragment {

    private RecyclerView recycler_view;
    private MaterialRefreshLayout refreshLayout;


    private HotGoodAdapter2 mAdapter2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        initViews(view);
        initData();
        return view;

    }

    private void initData() {
        String url = ConstantsValues.WARES_HOT;
        Pager.newBuilder().setUrl(url);
    }


    private void initViews(View view) {
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
    }

}
