package com.zliang.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zliang.shopping.Config.ConstantsValues;
import com.zliang.shopping.adapter.DividerItemDecoration;
import com.zliang.shopping.adapter.HotGoodAdapter2;
import com.zliang.shopping.bean.Page;
import com.zliang.shopping.bean.Ware;
import com.zliang.shopping.utils.LogUtils;
import com.zliang.shopping.utils.Pager;
import com.zliang.shopping.widget.CnToolBar;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
public class WaresListActivity extends AppCompatActivity implements Pager.OnPageListener<Ware>, TabLayout.OnTabSelectedListener, View.OnClickListener {

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mMaterialLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecylerView;
    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;

    @ViewInject(R.id.toolbar)
    private CnToolBar mToolBar;

    private long campaignId = 0;
    private int orderBy = 0;
    private HotGoodAdapter2 mWaresAdapter;

    private Pager pager;

    public final int DEFAULT = 0;
    public final int SALE = 1;
    public final int PRICE = 2;

    public final int ACTION_LIST = 0;
    public final int ACTION_GRID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        ViewUtils.inject(this);
        intToolBar();
        initTab();
        getData();
    }

    private void intToolBar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolBar.setRightButtonIcon(R.drawable.icon_list_32);
        mToolBar.getRightButton().setTag(ACTION_LIST);//默认是列表
        mToolBar.setRightButtonIconListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            campaignId = intent.getLongExtra("campaignId", 0);
        }
        String url = ConstantsValues.WARES_CAMPAIN_LIST;
        pager = Pager.newBuilder().setUrl(url)
                .putParams("campaignId", campaignId + "")
                .putParams("orderBy", orderBy + "")
                .setMaterialRefreshLayout(mMaterialLayout)
                .setCanLoadMore(true)
                .setOnPageListener(this)
                .build(this, new TypeToken<Page<Ware>>() {
                }.getType());
        pager.request();
    }

    private void initTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(DEFAULT);
        mTabLayout.addTab(tab);

        TabLayout.Tab saleNumTab = mTabLayout.newTab();
        saleNumTab.setText("销量");
        saleNumTab.setTag(SALE);
        mTabLayout.addTab(saleNumTab);

        TabLayout.Tab priceTab = mTabLayout.newTab();
        priceTab.setText("价格");
        priceTab.setTag(PRICE);
        mTabLayout.addTab(priceTab);

        mTabLayout.setOnTabSelectedListener(this);

    }


    @Override
    public void load(List<Ware> datas, int totalPage, int totalCount) {
        mTxtSummary.setText("共有" + totalCount + "件商品");
        LogUtils.e("load datas.size:" + datas.size());
        if (mWaresAdapter == null) {
            mWaresAdapter = new HotGoodAdapter2(this, datas);
            mRecylerView.setAdapter(mWaresAdapter);
            mRecylerView.setLayoutManager(new LinearLayoutManager(this));
            mRecylerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecylerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }
    }

    @Override
    public void refresh(List<Ware> datas, int totalPage, int totalCount) {
        mWaresAdapter.refreshData(datas);
        mRecylerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Ware> datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy", orderBy + "");
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if (action == ACTION_LIST) {
            mToolBar.getRightButton().setTag(ACTION_GRID);
            mToolBar.setRightButtonIcon(R.drawable.icon_grid_32);
            mWaresAdapter.resetLayout(R.layout.templeate_grid_ware);
            mRecylerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (action == ACTION_GRID) {
            mToolBar.getRightButton().setTag(ACTION_LIST);
            mToolBar.setRightButtonIcon(R.drawable.icon_list_32);
            mWaresAdapter.resetLayout(R.layout.template_hot_wares);
            mRecylerView.setLayoutManager(new LinearLayoutManager(this));
            mRecylerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecylerView.setItemAnimator(new DefaultItemAnimator());
        }
    }
}
