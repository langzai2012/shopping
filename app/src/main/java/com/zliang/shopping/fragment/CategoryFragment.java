package com.zliang.shopping.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zliang.shopping.Config.ConstantsValues;
import com.zliang.shopping.R;
import com.zliang.shopping.adapter.BaseAdapter;
import com.zliang.shopping.adapter.CategoryAdapter;
import com.zliang.shopping.adapter.DividerGridItemDecoration;
import com.zliang.shopping.adapter.DividerItemDecoration;
import com.zliang.shopping.adapter.WareAdapter;
import com.zliang.shopping.bean.Banner;
import com.zliang.shopping.bean.Category;
import com.zliang.shopping.bean.CategoryWare;
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
public class CategoryFragment extends Fragment {
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();


    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPage = 1;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;

    private CategoryAdapter mAdapter;
    private WareAdapter mWareAdapter;

    private long mCategoryId;

    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mWareRecyclerView;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ViewUtils.inject(this, view);
        requestCategoryData();
        requestSliderlayoutData();
        initRefresh();
        return view;
    }

    private void requestSliderlayoutData() {
        String url = ConstantsValues.BANNER + "?type=1";
        httpHelper.get(url, new BaseCallBack<List<Banner>>() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                if (banners != null && banners.size() > 0) {
                    showSliderDatas(banners);
                }
            }

            @Override
            public void onError(Response response, int responseCode, Exception e) {

            }
        });
    }

    private void showSliderDatas(List<Banner> banners) {
        for (Banner banner : banners) {
            //DefaultSliderview 没有显示
            DefaultSliderView sliderView = new DefaultSliderView(getContext());
            sliderView.image(banner.getImgUrl());
            sliderView.description(banner.getDescription());//就算设置也不会显示
            mSliderLayout.addSlider(sliderView);
        }
    }

    private void requestCategoryData() {
        String url = ConstantsValues.CATEGORY_LIST;
        httpHelper.get(url, new BaseCallBack<List<Category>>() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                if (categories != null && categories.size() > 0) {
                    showCategoryData(categories);
                    requestWares(categories.get(0).getId());
                    mCategoryId = categories.get(0).getId();
                    LogUtils.e("mCategoryId:" + mCategoryId);

                }
            }

            @Override
            public void onError(Response response, int responseCode, Exception e) {

            }
        });
    }

    private void initRefresh() {

        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if (currentPage <= totalPage) {
                    onRefreshLoadMOreData();
                } else {
                    Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_LONG).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void onRefreshLoadMOreData() {
        currentPage = ++currentPage;
        state = STATE_MORE;
        requestWares(mCategoryId);
    }

    private void refreshData() {
        currentPage = 1;
        state = STATE_REFRESH;
        requestWares(mCategoryId);
    }


    private void showCategoryData(final List<Category> categories) {
        mAdapter = new CategoryAdapter(getContext(), categories);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currentPage = 1;
                state = STATE_NORMAL;
                requestWares(categories.get(position).getId());
            }
        });
    }

    private void requestWares(long categoryId) {
        String url = ConstantsValues.WARES_LIST;
        Map<String, String> params = new HashMap<>();
        params.put("categoryId", "" + categoryId);
        params.put("curPage", currentPage + "");
        params.put("pageSize", pageSize + "");

        LogUtils.e("categoryId:" + categoryId);
        LogUtils.e("curPage:" + currentPage);
        LogUtils.e("pageSize:" + pageSize);

        httpHelper.post(url, params, new BaseCallBack<CategoryWare>() {

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, CategoryWare categoryWare) {
                if (categoryWare != null) {
                    totalPage = categoryWare.getTotalPage();
                    showWareData(categoryWare.getList());
                }
            }

            @Override
            public void onError(Response response, int responseCode, Exception e) {

            }
        });
    }


    private void showWareData(List<Ware> list) {
        LogUtils.e("showWareData.state:" + state);
        switch (state) {
            case STATE_NORMAL:
                if (mWareAdapter == null) {
                    mWareAdapter = new WareAdapter(getContext(), list);
                    mWareRecyclerView.setAdapter(mWareAdapter);
                    mWareRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mWareRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mWareRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
                } else {
                    mWareAdapter.clearDatas();
                    mWareAdapter.addDatas(list);
                }

                break;
            case STATE_REFRESH:
                mWareAdapter.clearDatas();
                mWareAdapter.addDatas(list);
                mWareRecyclerView.smoothScrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mWareAdapter.addDatas(mWareAdapter.getDatas().size(), list);
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }

}



