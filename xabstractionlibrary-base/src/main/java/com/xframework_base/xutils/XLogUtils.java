package com.xframework_base.xutils;

import android.util.Log;

import com.xframework_base.BuildConfig;

public class XLogUtils {
    private static boolean isDebug = BuildConfig.DEBUG;

    private static String TAG = "XFramework";

    public static void setIsDebug(boolean bDebug){
        isDebug = bDebug;
    }

    public static void setTag(String tag){
        TAG = tag;
    }

    public static void v(String msg) {
        if (isDebug) {
            processLog("v", TAG + getFunctionLocation(), msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            processLog("v", tag + getFunctionLocation(), msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            processLog("d", TAG + getFunctionLocation(), msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            processLog("d", tag + getFunctionLocation(), msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            processLog("i", TAG + getFunctionLocation(), msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            processLog("i", tag + getFunctionLocation(), msg);
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            processLog("w", TAG + getFunctionLocation(), msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            processLog("w", tag + getFunctionLocation(), msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            processLog("e", TAG + getFunctionLocation(), msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            processLog("e", tag + getFunctionLocation(), msg);
        }
    }

    private static String getFunctionLocation() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts != null) {
            for (StackTraceElement st : sts) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (st.getClassName().equals(Thread.class.getName())) {
                    continue;
                }
                if (st.getClassName().equals(XLogUtils.class.getName())) {
                    continue;
                }
                return "(" + st.getFileName() + ":" + st.getLineNumber() + ")";
            }
        }
        return null;
    }

    private static void processLog(String sign, String tag, String msg) {
        if (msg == null || msg.length() == 0)
            return;
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            logout(sign, tag, msg);

        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                logout(sign, tag, logContent);
            }
            logout(sign, tag, msg);
        }

    }

    private static void logout(String sign, String tag, String msg) {
        if ("v".equals(sign)) {
            Log.v(tag, msg);
        } else if ("d".equals(sign)) {
            Log.d(tag, msg);
        } else if ("i".equals(sign)) {
            Log.i(tag, msg);
        } else if ("w".equals(sign)) {
            Log.w(tag, msg);
        } else if ("e".equals(sign)) {
            Log.e(tag, msg);
        }
    }
}
