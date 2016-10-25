package com.zliang.shopping.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zliang.shopping.bean.Page;
import com.zliang.shopping.http.BaseCallBack;
import com.zliang.shopping.http.OkHttpHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/23 0023.
 */
public class Pager {
    private static Builder builder;
    private OkHttpHelper httpHelper;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    private Pager() {
        httpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public static Builder newBuilder() {
        builder = new Builder();
        return builder;
    }

    private void initRefreshLayout() {

        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);

        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }


            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if (builder.pageIndex < builder.totalPage)
                    loadMore();
                else {
                    Toast.makeText(builder.mContext, "无更多数据", Toast.LENGTH_LONG).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }


    /**
     * 刷新数据
     */
    private void refresh() {

        state = STATE_REFREH;
        builder.pageIndex = 1;
        requestData();
    }

    /**
     * 隐藏数据
     */
    private void loadMore() {

        state = STATE_MORE;
        builder.pageIndex = ++builder.pageIndex;
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {

        String url = builder.url;

        httpHelper.post(url, builder.params, new RequestCallback());

    }

    /**
     * 显示数据
     */
    private <T> void showData(List<T> datas, int totalPage, int totalCount) {

        if (datas == null || datas.size() <= 0) {
            Toast.makeText(builder.mContext, "加载不到数据", Toast.LENGTH_LONG).show();
            return;
        }

        if (STATE_NORMAL == state) {

            if (builder.onPageListener != null) {
                builder.onPageListener.load(datas, totalPage, totalCount);
            }
        } else if (STATE_REFREH == state) {
            builder.mRefreshLayout.finishRefresh();
            if (builder.onPageListener != null) {
                builder.onPageListener.refresh(datas, totalPage, totalCount);
            }

        } else if (STATE_MORE == state) {

            builder.mRefreshLayout.finishRefreshLoadMore();
            if (builder.onPageListener != null) {
                builder.onPageListener.loadMore(datas, totalPage, totalCount);
            }

        }
    }

    private static class Builder {
        private String url;

        private MaterialRefreshLayout mRefreshLayout;

        private boolean canLoadMore;


        private int totalPage = 1;
        private int pageIndex = 1;
        private int pageSize = 10;

        private Map<String, String> params;

        private Type type;
        private Context mContext;

        private OnPageListener onPageListener;

        public Pager bulder() {
            return null;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return builder;
        }

        public Builder setParams(Map<String, String> params) {
            this.params = params;
            return builder;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return builder;
        }

        public Builder setCanLoadMore(boolean canLoadMore) {
            this.canLoadMore = canLoadMore;
            return builder;
        }

        public Builder setMaterialRefreshLayout(MaterialRefreshLayout layout) {
            this.mRefreshLayout = layout;
            return builder;
        }

        public Builder setOnPageListener(OnPageListener listener) {
            this.onPageListener = listener;
            return builder;
        }

        public Pager build(Context context, Type type) {
            this.mContext = context;
            this.type = type;
            valid();
            return new Pager();
        }

        private void valid() {
            if (mContext == null) {
                throw new RuntimeException("context can not be null");
            }

            if (TextUtils.isEmpty(url)) {
                throw new RuntimeException("url can not be null");
            }

            if (mRefreshLayout == null) {
                throw new RuntimeException("RefreshLayout can not be null");
            }
        }

    }


    public interface OnPageListener<T> {

        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas, int totalPage, int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);

    }


    class RequestCallback<T> extends BaseCallBack<Page<T>> {

        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onRequestBefore(Request request) {

        }

        @Override
        public void onSuccess(Response response, Page<T> page) {
            builder.pageIndex = page.getCurrentPage();
            builder.pageSize = page.getPageSize();
            builder.totalPage = page.getTotalPage();
            showData(page.getList(), page.getTotalPage(), page.getTotalCount());
        }

        @Override
        public void onError(Response response, int responseCode, Exception e) {

        }
    }

}
