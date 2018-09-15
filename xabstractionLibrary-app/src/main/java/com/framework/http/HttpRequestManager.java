package com.framework.http;

/**
 * created by lanbiao on 2018/5/31
 * http请求管理器单例对象
 */
public class HttpRequestManager extends BaseHttpRequestManager {

    private static HttpRequestManager httpRequestManager = null;

    public static HttpRequestManager getInstance(){
        if(httpRequestManager == null){
            synchronized (HttpRequestManager.class){
                if(httpRequestManager == null){
                    httpRequestManager = new HttpRequestManager(20,20,20,100 * 1024 * 1024);
                }
            }
        }
        return httpRequestManager;
    }

    protected HttpRequestManager(int connectTimeout, int readTimeout, int writeTimeout, long httpCacheMaxSize) {
        super(connectTimeout, readTimeout, writeTimeout, httpCacheMaxSize);
        httpHostAddress = "http://tobcs.app.aipark.com/api/v1/encrypt/";
    }


}
