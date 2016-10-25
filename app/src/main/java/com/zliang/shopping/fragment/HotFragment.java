package com.zliang.shopping.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zliang.shopping.Config.ConstantsValues;
import com.zliang.shopping.R;
import com.zliang.shopping.adapter.DividerItemDecoration;
import com.zliang.shopping.adapter.HotGoodAdapter2;
import com.zliang.shopping.bean.HotGoods;
import com.zliang.shopping.bean.Ware;
import com.zliang.shopping.http.BaseCallBack;
import com.zliang.shopping.http.OkHttpHelper;
import com.zliang.shopping.utils.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Ivan on 15/9/22.
 */
public class HotFragment extends Fragment {

    private RecyclerView recycler_view;
    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPage = 1;
    private MaterialRefreshLayout refreshLayout;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    //private HotGoodsAdapter mAdapter;
    private HotGoodAdapter2 mAdapter2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        initViews(view);
        initData();
        initRefreshLayout();
        return view;

    }

    private void initRefreshLayout() {
        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if (currentPage <= totalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_LONG).show();
                    refreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void loadMoreData() {
        currentPage = ++currentPage;
        state = STATE_MORE;
        initData();

    }

    private void refreshData() {
        currentPage = 1;
        state = STATE_REFRESH;
        initData();
    }

    private void initData() {
        String url = ConstantsValues.WARES_HOT;
        Map<String, String> params = new HashMap<>();
        params.put("curPage", currentPage + "");
        params.put("pageSize", pageSize + "");
        httpHelper.post(url, params, new BaseCallBack<HotGoods>() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, HotGoods hotGoods) {
                LogUtils.e("hotGoods:" + hotGoods);
                currentPage = hotGoods.getCurrentPage();
                pageSize = hotGoods.getPageSize();
                totalPage = hotGoods.getTotalPage();
                showData(hotGoods.getList());
            }

            @Override
            public void onError(Response response, int responseCode, Exception e) {

            }
        });
    }

    private void showData(List<Ware> list) {
        switch (state) {
            case STATE_NORMAL:
//                mAdapter = new HotGoodsAdapter(list);
                mAdapter2 = new HotGoodAdapter2(getContext(), list);
                recycler_view.setAdapter(mAdapter2);
                recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
                recycler_view.setItemAnimator(new DefaultItemAnimator());
                recycler_view.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                break;
            case STATE_REFRESH:
                mAdapter2.clearDatas();
                mAdapter2.addDatas(list);
                recycler_view.smoothScrollToPosition(0);
                refreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mAdapter2.addDatas(mAdapter2.getDatas().size(), list);
                refreshLayout.finishRefreshLoadMore();
                break;
        }

    }


    private void initViews(View view) {
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
    }

}
