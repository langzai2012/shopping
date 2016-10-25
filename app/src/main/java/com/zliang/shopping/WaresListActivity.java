package com.zliang.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zliang.shopping.Config.ConstantsValues;
import com.zliang.shopping.adapter.DividerItemDecoration;
import com.zliang.shopping.adapter.HotGoodAdapter2;
import com.zliang.shopping.bean.HotGoods;
import com.zliang.shopping.bean.Page;
import com.zliang.shopping.bean.Ware;
import com.zliang.shopping.utils.LogUtils;
import com.zliang.shopping.utils.Pager;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
public class WaresListActivity extends AppCompatActivity implements Pager.OnPageListener<Ware>, TabLayout.OnTabSelectedListener {

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mMaterialLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecylerView;
    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;

    private long campaignId = 0;
    private int orderBy = 0;
    private HotGoodAdapter2 mWaresAdapter;

    private Pager pager;

    public final int DEFAULT = 0;
    public final int SALE = 1;
    public final int PRICE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        ViewUtils.inject(this);
        initTab();
        getData();
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
        tab.setTag(PRICE);
        mTabLayout.addTab(priceTab);

        mTabLayout.setOnTabSelectedListener(this);

    }


    @Override
    public void load(List<Ware> datas, int totalPage, int totalCount) {
        mTxtSummary.setText("共有" + totalCount + "件商品");
        LogUtils.e("datas:" + datas.toString());
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
}
