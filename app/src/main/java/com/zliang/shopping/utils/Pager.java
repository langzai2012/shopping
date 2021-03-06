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
import java.util.HashMap;
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

                if (builder.pageIndex < builder.totalPage) {
                    loadMore();
                } else {
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

    public void putParam(String key, String value) {
        builder.params.put(key, value);
    }

    public void request() {
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {

        String url = buildUrl();
        LogUtils.e("url:" + url);
        httpHelper.get(url, new RequestCallback());

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
                T t = datas.get(0);
                LogUtils.e("t:" + t.getClass());
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

    public static class Builder {
        private String url;

        private MaterialRefreshLayout mRefreshLayout;

        private boolean canLoadMore;


        private int totalPage = 1;
        private int pageIndex = 1;
        private int pageSize = 10;

        private Map<String, String> params = new HashMap<>();

        private Type type;
        private Context mContext;

        private OnPageListener onPageListener;


        public Builder setUrl(String url) {
            this.url = url;
            return builder;
        }

        public Builder putParams(String key, String value) {
            params.put(key, value);
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

    private String buildUrl() {
        return builder.url + "?" + buildParams();
    }

    /**
     * 构建参数
     *
     * @return
     */
    private String buildParams() {
        Map<String, String> paramsMap = builder.params;
        paramsMap.put("curPage", builder.pageIndex + "");
        paramsMap.put("pageSize", builder.pageSize + "");
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            builder.append(entry.getKey() + "=" + entry.getValue());
            builder.append("&");
        }

        String params = builder.toString();

        if (params.endsWith("&")) {
            params = params.substring(0, params.length() - 1);
        }

        return params;
    }


    public interface OnPageListener<T> {

        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas, int totalPage, int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);

    }


    class RequestCallback<T> extends BaseCallBack<Page<T>> {

        public RequestCallback() {
            super();
            super.type = builder.type;
        }

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
            LogUtils.e("page:" + page);
            showData(page.getList(), page.getTotalPage(), page.getTotalCount());
        }

        @Override
        public void onError(Response response, int responseCode, Exception e) {

        }
    }

}
