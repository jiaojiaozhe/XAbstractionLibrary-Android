package com.framework.http;

/**
 * Created by lanbiao on 2018/5/31
 * https网络请求管理器单例对象
 */
public class HttpsRequestManager extends BaseHttpRequestManager {

    private static HttpsRequestManager httpsRequestManager = null;

    public static HttpsRequestManager getInstance2(){
        if(httpsRequestManager == null){
            synchronized (HttpsRequestManager.class){
                if(httpsRequestManager == null){
                    httpsRequestManager = new HttpsRequestManager(20,20,20,100 * 1024 * 1024);
                }
            }
        }
        return httpsRequestManager;
    }

    protected HttpsRequestManager(int connectTimeout, int readTimeout, int writeTimeout, long httpCacheMaxSize) {
        super(connectTimeout, readTimeout, writeTimeout, httpCacheMaxSize);
        httpHostAddress = "https://123.56.240.237/";
    }
}
