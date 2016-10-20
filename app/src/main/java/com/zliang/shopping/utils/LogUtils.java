package com.zliang.shopping.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class LogUtils {

    public static void e(String log) {
        e("zliang", log);
    }

    public static void e(String tag, String log) {
        Log.e(tag, log);
    }
}
