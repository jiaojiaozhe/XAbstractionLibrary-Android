package com.framework.http.http2;

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
        //http://sjz.app2.aipark.com/api/v1/park/queryByDistanceCountGplus
        httpHostAddress = "http://sjz.app2.aipark.com";
    }
}
