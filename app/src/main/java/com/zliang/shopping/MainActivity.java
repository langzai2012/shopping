package com.zliang.shopping;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.zliang.shopping.bean.Tab;
import com.zliang.shopping.fragment.CartFragment;
import com.zliang.shopping.fragment.CategoryFragment;
import com.zliang.shopping.fragment.HomeFragment;
import com.zliang.shopping.fragment.HotFragment;
import com.zliang.shopping.fragment.MineFragment;
import com.zliang.shopping.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater mInflater;
    private FragmentTabHost mTabHost;
    private List<Tab> tabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabHost();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initTabHost() {

        Tab homeTab = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab hotTab = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab categoryTab = new Tab(R.string.catagory, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab cartTab = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab mineTab = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);
        tabs.add(homeTab);
        tabs.add(hotTab);
        tabs.add(categoryTab);
        tabs.add(cartTab);
        tabs.add(mineTab);

        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : tabs) {
            TabHost.TabSpec spec = mTabHost.newTabSpec(getString(tab.getTitle()));
            View view = buildIndicator(tab);
            spec.setIndicator(view);
            mTabHost.addTab(spec, tab.getFragment(), null);
        }
        //去除分割线
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);


    }

    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView homeImgView = (ImageView) view.findViewById(R.id.icon_tab);
        TextView homeTxtView = (TextView) view.findViewById(R.id.txt_indicator);
        homeImgView.setBackgroundResource(tab.getIcon());
        homeTxtView.setText(tab.getTitle());
        return view;
    }
}
