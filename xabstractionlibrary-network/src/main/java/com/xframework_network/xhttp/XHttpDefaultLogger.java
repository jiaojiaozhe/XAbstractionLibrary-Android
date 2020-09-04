package com.xframework_network.xhttp;


import com.xframework_base.xutils.XLogUtils;

import okhttp3.logging.HttpLoggingInterceptor;

public class XHttpDefaultLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {

        XLogUtils.d(message);
    }
}
