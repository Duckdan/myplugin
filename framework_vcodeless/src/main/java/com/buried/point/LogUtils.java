package com.buried.point;


import android.util.Log;

import com.vivo.ai.ime.vcodeless.BuildConfig;

public class LogUtils {

    private static final String TAG_PREFIX = "VivoIME-";
    private static final String KEY_VIVO_LOG_CTRL = "persist.sys.log.ctrl";
    private static boolean IS_LOG_CTRL_OPEN = true;

    private static final ThreadLocal<StringBuilder> gStringBuilderCache = new ThreadLocal();

    public static boolean enableDebug() {
        // return true;//engine 分支 打开。
        return BuildConfig.DEBUG || IS_LOG_CTRL_OPEN;
    }

    public static void resetEnableDebug() {
        IS_LOG_CTRL_OPEN = true;
    }

    public static String buildTag(String preffix, String tag) {
        StringBuilder newTag = gStringBuilderCache.get();
        if (newTag == null) {
            newTag = new StringBuilder();
            gStringBuilderCache.set(newTag);
        } else {
            newTag.delete(0, newTag.length());
        }
        return newTag.append(preffix).append(tag).toString();
    }

    public static void v(String tag, String msg) {
        if (enableDebug()) {
            Log.v(buildTag(TAG_PREFIX, tag), msg);
        }
    }

    // Debug
    public static void d(String tag, String msg) {
        if (enableDebug()) {
            Log.d(buildTag(TAG_PREFIX, tag), msg);
        }
    }

    public static void d(final String tag, final String msg, Throwable tr) {
        if (enableDebug()) {
            Log.d(buildTag(TAG_PREFIX, tag), msg, tr);
        }
    }

    // Info
    public static void i(String tag, String msg) {
        Log.i(buildTag(TAG_PREFIX, tag), msg);
    }

    // Warn
    public static void w(String tag, String msg) {
        Log.w(buildTag(TAG_PREFIX, tag), msg);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        Log.w(buildTag(TAG_PREFIX, tag), msg, throwable);
    }

    // Error
    public static void e(String tag, String msg) {
        Log.e(buildTag(TAG_PREFIX, tag), msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        Log.e(buildTag(TAG_PREFIX, tag), msg, throwable);
    }

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }
}