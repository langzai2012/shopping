package com.zliang.shopping;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
public class WaresListActivity extends AppCompatActivity {

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        ViewUtils.inject(this);
        initTab();
    }

    private void initTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        mTabLayout.addTab(tab);

        TabLayout.Tab priceTab = mTabLayout.newTab();
        priceTab.setText("价格");
        mTabLayout.addTab(priceTab);

        TabLayout.Tab saleNumTab = mTabLayout.newTab();
        saleNumTab.setText("销量");
        mTabLayout.addTab(saleNumTab);

    }
}
