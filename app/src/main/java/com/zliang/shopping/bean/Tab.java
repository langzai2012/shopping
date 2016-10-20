package com.zliang.shopping.bean;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Administrator on 2016/10/6 0006.
 */
public class Tab {
    private int title;
    private int icon;
    private Class<? extends Fragment> fragment;

    public Tab() {
    }

    public Tab(int title, int icon, Class<? extends Fragment> fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class<? extends Fragment> getFragment() {
        return fragment;
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }
}
