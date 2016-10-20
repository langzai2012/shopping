package com.zliang.shopping.http;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public abstract class BaseCallBack<T> {
    public Type type;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallBack() {
        type = getSuperclassTypeParameter(getClass());
    }

    public abstract void onFailure(Request request, IOException e);

    public abstract void onRequestBefore(Request request);

    public abstract void onSuccess(Response response, T t);

    public abstract void onError(Response response, int responseCode, Exception e);
}
