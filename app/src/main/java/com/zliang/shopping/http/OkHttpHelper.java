package com.zliang.shopping.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zliang.shopping.utils.LogUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class OkHttpHelper {

    private OkHttpClient okHttpClient;
    private Gson gson;
    private Handler mHandler;

    private OkHttpHelper() {
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
        mHandler = new Handler(Looper.getMainLooper());
    }

    private static class ViewHolder {
        static OkHttpHelper helper = new OkHttpHelper();
    }

    public static OkHttpHelper getInstance() {
        return ViewHolder.helper;
    }

    public void get(String url, BaseCallBack callBack) {
        Request request = buildeRequest(url, null, HttpMethodType.GET);
        doRequest(request, callBack);
    }


    public void post(String url, Map<String, String> params, BaseCallBack callBack) {
        Request request = buildeRequest(url, params, HttpMethodType.POST);
        doRequest(request, callBack);
    }

    public void doRequest(Request request, final BaseCallBack callBack) {
        callBack.onRequestBefore(request);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBack.onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    LogUtils.e("result:" + result);
                    if (callBack.type == String.class) {//结果需要字符串类型
                        onSuccess(response, result, callBack);
                    } else {
                        Object obj = gson.fromJson(result, callBack.type);
                        onSuccess(response, obj, callBack);
                    }

                } else {
                    callBack.onError(response, response.code(), null);
                }
            }
        });
    }

    private Request buildeRequest(String url, Map<String, String> params, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (methodType == HttpMethodType.GET) {
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();
    }

    private RequestBody buildFormData(Map<String, String> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    private void onSuccess(final Response response, final Object obj, final BaseCallBack callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }

    enum HttpMethodType {
        GET, POST
    }
}
