package com.xframework_network.xhttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.xframework_base.xmodel.XBaseModel;
import com.xframework_base.xutils.XMD5;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by lanbiao on 2018/5/31
 * 抽象http请求管理器，与业务完全脱离，并为后续开发预埋设计
 */
public abstract class XHttpRequestManager extends XBaseModel {

    public enum ContentType{
        appxformUrlencoded(0,"application/x-www-form-urlencoded"),
        applicationJson(1,"application/json"),
        multipartFormData(2,"multipart/form-data"),
        textXml(3,"multipart/form-data");
        private int contentTypeIndex;
        private String contentType;

        ContentType(int contentTypeIndex, String contentType) {
            this.contentTypeIndex = contentTypeIndex;
            this.contentType = contentType;
        }

        public int getContentTypeIndex() {
            return contentTypeIndex;
        }

        public void setContentTypeIndex(int contentTypeIndex) {
            this.contentTypeIndex = contentTypeIndex;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }

    /**
     * 三方请求对象
     */
    private OkHttpClient client = null;

    /**
     * 主线程Handler
     */
    private Handler mainHandler = null;

    /**
     * 链接超时时间，默认30秒
     */
    private int connectTimeOut = 30;

    /**
     * 写超时时间，默认30秒
     */
    private int writeTimeOut = 30;

    /**
     * 读超时时间
     */
    private int readTimeOut = 30;

    /**
     * http请求缓存路径
     */
    private String httpCachePath = null;

    /**
     * http请求缓存的最大大小，默认100M
     */
    private long httpCacheMaxSize = 100 * 1024 * 1024;

    /**
     * 请求集合，id对应request
     */
    private WeakHashMap<String, XIBaseHttpRequestDelegate> requestMaps = new WeakHashMap<>();

    /**
     * requestMaps数据源请求的原子锁对象
     */
    private Serializable requestMapsLock = new XBaseModel() {
    };

    /**
     * 请求集合，authId对应request的集合
     */
    private HashMap<String, WeakHashMap<String, XIBaseHttpRequestDelegate>> authRequestMaps = new HashMap<>();

    protected XHttpRequestManager() {
        super();
        httpCachePath = getAppCachePath() + "httpCache" + File.separator;
        init();
    }

    protected XHttpRequestManager(int connectTimeOut,
                                  int readTimeOut,
                                  int writeTimeOut,
                                  long httpCacheMaxSize) {
        super();
        this.connectTimeOut = connectTimeOut;
        this.readTimeOut = readTimeOut;
        this.writeTimeOut = writeTimeOut;
        this.httpCacheMaxSize = httpCacheMaxSize;
        this.httpCachePath = getAppCachePath() + "httpCache" + File.separator;
        init();
    }

    private void init() {
        OkHttpClient.Builder builder = null;
        try {
            builder = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new HttpLoggingInterceptor(new XHttpDefaultLogger()).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .sslSocketFactory(new SSLFactory(), new TrustAllManager())
                    .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
                    .readTimeout(readTimeOut, TimeUnit.SECONDS)
                    .cache(new Cache(new File(httpCachePath), httpCacheMaxSize));
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (builder != null) {
            client = builder.build();
        }

        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * app相关缓存的文件夹路径
     *
     * @return 绝对路径
     */
    public abstract String getAppCachePath();

    /**
     * 添加即将发起的请求到集合，并判断是否有老的请求在队列
     *
     * @param request 即将发起的请求
     * @return true表示需要发起新请求，否则等待回调即可
     */
    private Boolean appendRequest(XIBaseHttpRequestDelegate request) {
        Boolean bNeedRequest = false;
        if (request == null) {
            return bNeedRequest;
        }

        if (request instanceof XHttpRequest) {
            final XHttpRequest httpRequest = (XHttpRequest) request;
            if (httpRequest.getAuthId() == null || httpRequest.getId() == null) {
                return bNeedRequest;
            }

            WeakHashMap<String, XIBaseHttpRequestDelegate> authRequests = null;
            synchronized (requestMapsLock) {
                if (authRequestMaps.containsKey(httpRequest.getAuthId())) {
                    authRequests = authRequestMaps.get(httpRequest.getAuthId());
                } else {
                    authRequests = new WeakHashMap<>();
                    bNeedRequest = true;
                }

                final WeakHashMap<String, XIBaseHttpRequestDelegate> finalAuthRequests = authRequests;
                if (!requestMaps.containsKey(httpRequest.getId())) {
                    requestMaps.put(httpRequest.getId(), httpRequest);
                }

                finalAuthRequests.put(httpRequest.getId(), httpRequest);
                authRequestMaps.put(httpRequest.getAuthId(), finalAuthRequests);
            }

//            final WeakHashMap<String, XIBaseHttpRequestDelegate> finalAuthRequests = authRequests;
//            mainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    synchronized (requestMapsLock) {
//                        if (!requestMaps.containsKey(httpRequest.getId())) {
//                            requestMaps.put(httpRequest.getId(), httpRequest);
//                        }
//
//                        finalAuthRequests.put(httpRequest.getId(), httpRequest);
//                        authRequestMaps.put(httpRequest.getAuthId(), finalAuthRequests);
//                    }
//                }
//            });
        }
        return bNeedRequest;
    }

    private void removeRequest(XIBaseHttpRequestDelegate request) {
        if (request == null) {
            return;
        }

        if (request instanceof XHttpRequest) {
            final XHttpRequest httpRequest = (XHttpRequest) request;
            if (httpRequest.getId() == null || httpRequest.getAuthId() == null) {
                return;
            }


            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    synchronized (requestMapsLock) {
                        if (requestMaps.containsKey(httpRequest.getId())) {
                            requestMaps.remove(httpRequest.getId());
                        }

                        WeakHashMap<String, XIBaseHttpRequestDelegate> authRequests = null;
                        if (authRequestMaps.containsKey(httpRequest.getAuthId())) {
                            authRequests = authRequestMaps.get(httpRequest.getAuthId());

                            if (authRequests != null) {
                                authRequests.remove(httpRequest.getId());
                            }

                            if (authRequests.size() <= 0) {
                                authRequestMaps.remove(httpRequest.getAuthId());
                            }
                        }
                    }
                }
            });
        }
    }

    private void removeRequestWithRequestId(String requestId) {
        if (TextUtils.isEmpty(requestId)) {
            return;
        }

        XIBaseHttpRequestDelegate request = null;
        synchronized (requestMapsLock) {
            if (requestMaps.containsKey(requestId)) {
                request = requestMaps.get(requestId);

                if (request instanceof XHttpRequest) {
                    XHttpRequest httpRequest = (XHttpRequest) request;
                    if (httpRequest.getId() == null || httpRequest.getAuthId() == null) {
                        return;
                    }

                    if (requestMaps.containsKey(httpRequest.getId())) {
                        requestMaps.remove(httpRequest.getId());
                    }

                    WeakHashMap<String, XIBaseHttpRequestDelegate> authRequests = null;
                    if (authRequestMaps.containsKey(httpRequest.getAuthId())) {
                        authRequests = authRequestMaps.get(httpRequest.getAuthId());

                        if (authRequests != null) {
                            authRequests.remove(httpRequest.getId());
                        }

                        if (authRequests.size() <= 0) {
                            authRequestMaps.remove(httpRequest.getAuthId());
                        }
                    }
                }
            }
        }
    }

    private void removeRequestWithAuthId(String authId) {
        if (TextUtils.isEmpty(authId)) {
            return;
        }

        ArrayList<XIBaseHttpRequestDelegate> requestList = null;
        synchronized (requestMapsLock) {
            if (authRequestMaps.containsKey(authId)) {
                WeakHashMap<String, XIBaseHttpRequestDelegate> requestMaps = authRequestMaps.get(authId);
                if (requestMaps != null) {
                    requestList = new ArrayList<>(requestMaps.values());
                }

                for (XIBaseHttpRequestDelegate request : requestList) {
                    if (request instanceof XHttpRequest) {
                        XHttpRequest httpRequest = (XHttpRequest) request;
                        if (httpRequest.getId() == null || httpRequest.getAuthId() == null) {
                            return;
                        }

                        if (requestMaps.containsKey(httpRequest.getId())) {
                            requestMaps.remove(httpRequest.getId());
                        }

                        WeakHashMap<String, XIBaseHttpRequestDelegate> authRequests = null;
                        if (authRequestMaps.containsKey(httpRequest.getAuthId())) {
                            authRequests = authRequestMaps.get(httpRequest.getAuthId());

                            if (authRequests != null) {
                                authRequests.remove(httpRequest.getId());
                            }

                            if (authRequests.size() <= 0) {
                                authRequestMaps.remove(httpRequest.getAuthId());
                            }
                        }
                    }
                }
            }
        }
    }

    private void clearRequests() {
        synchronized (requestMapsLock) {
            requestMaps.clear();
            authRequestMaps.clear();
        }
    }

    /**
     * 处理请求对象，集中管理
     *
     * @param mothod        请求类型: get、post等
     * @param requestUrl    请求链接
     * @param requestParams 请求参数
     * @return 返回true表示需要发起新请求，否则等待回调
     */

    private Boolean processRequest(String mothod,
                                   String requestUrl,
                                   Map<String, String> requestParams,
                                   XIBaseHttpRequestDelegate request) {

        Iterator iterator = requestParams.entrySet().iterator();
        if (iterator.hasNext()) {
            requestUrl += "?";
        }

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            String param = key + "=" + value;
            requestUrl += param;
            if (iterator.hasNext()) {
                requestUrl += "&";
            }
        }

        requestUrl += mothod;
        String authId = XMD5.md5s(requestUrl);

        if (request != null) {
            if (request instanceof XHttpRequest) {
                XHttpRequest httpRequest = (XHttpRequest) request;
                httpRequest.setAuthId(authId);
            }
        }
        return appendRequest(request);
    }


    private Request getRequestObject(String requestUrl,
                                     Map<String, String> requestParams,
                                     Map<String, String> requestHeads) {
        Iterator iterator = requestParams.entrySet().iterator();
        if (iterator.hasNext()) {
            requestUrl += "?";
        }

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            String param = key + "=" + value;
            requestUrl += param;
            if (iterator.hasNext()) {
                requestUrl += "&";
            }
        }

        CacheControl.Builder builder = new CacheControl.Builder();
        CacheControl cacheControl = builder.noCache().noStore().build();
        Request.Builder requestBuilder = new Request.Builder().cacheControl(cacheControl).url(requestUrl).get();
        if (requestHeads != null && requestHeads.size() > 0) {
            requestBuilder.headers(Headers.of(requestHeads));
        }


        Request request = requestBuilder.build();
        return request;
    }

    protected ContentType getRequestContentType(){
        return ContentType.appxformUrlencoded;
    }

    private Request postRequestObject(String requestUrl,
                                      Map<String, String> requestParams,
                                      Map<String, String> requestHeads) {
        RequestBody requestBody = null;
        String mediaTypeStr = null;

        if (requestHeads != null) {
            Iterator iteratorHeader = requestHeads.entrySet().iterator();
            while (iteratorHeader.hasNext()) {
                Map.Entry entryHeader = (Map.Entry) iteratorHeader.next();
                Object keyHeader = entryHeader.getKey();
                Object valueHeader = entryHeader.getValue();

                if ((keyHeader + "").toLowerCase().equals("Content-Type".toLowerCase())) {
                    mediaTypeStr = valueHeader + "";
                    requestHeads.remove(keyHeader);
                    break;
                }
            }
        }

        int contentTypeIndex = 0;
        if(TextUtils.isEmpty(mediaTypeStr)){
            contentTypeIndex = getRequestContentType().getContentTypeIndex();
        }else {
            if(ContentType.applicationJson.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.applicationJson.getContentTypeIndex();
            }else if(ContentType.appxformUrlencoded.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.appxformUrlencoded.getContentTypeIndex();
            }else if(ContentType.multipartFormData.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.multipartFormData.getContentTypeIndex();
            }else  if(ContentType.textXml.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.textXml.getContentTypeIndex();
            }
        }

        if(contentTypeIndex == ContentType.appxformUrlencoded.getContentTypeIndex()){

            FormBody.Builder formBuilder = new FormBody.Builder();
            Iterator iterator = requestParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                formBuilder.add(key + "", value + "");
            }
            requestBody = formBuilder.build();
            requestHeads.put("Content-Type", ContentType.appxformUrlencoded.getContentType());

        }else if(contentTypeIndex == ContentType.applicationJson.getContentTypeIndex()){
            MediaType mediaType = MediaType.parse(mediaTypeStr + "; charset=utf-8");
            requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(requestParams));

        }else  if(contentTypeIndex == ContentType.multipartFormData.getContentTypeIndex()){

            MediaType mediaType = MediaType.parse(mediaTypeStr + "; charset=utf-8");
            requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(requestParams));

        }else if(contentTypeIndex == ContentType.textXml.getContentTypeIndex()){

            MediaType mediaType = MediaType.parse(mediaTypeStr + "; charset=utf-8");
            requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(requestParams));

        }

        CacheControl.Builder builder = new CacheControl.Builder();
        CacheControl cacheControl = builder.noCache().noStore().build();
        Request.Builder requestBuilder = new Request.Builder()
                .cacheControl(cacheControl)
                .url(requestUrl)
                .post(requestBody);
        if (requestHeads != null && requestHeads.size() > 0) {
            requestBuilder.headers(Headers.of(requestHeads));
        }

        Request request = requestBuilder.build();
        return request;
    }


    private Request putRequestObject(String requestUrl,
                                     Map<String, String> requestParams,
                                     Map<String, String> requestHeads) {
        RequestBody requestBody = null;
        String mediaTypeStr = null;
        if (requestHeads != null) {
            Iterator iteratorHeader = requestHeads.entrySet().iterator();
            while (iteratorHeader.hasNext()) {
                Map.Entry entryHeader = (Map.Entry) iteratorHeader.next();
                Object keyHeader = entryHeader.getKey();
                Object valueHeader = entryHeader.getValue();
                if ((keyHeader + "").toLowerCase().equals("Content-Type".toLowerCase())) {
                    mediaTypeStr = valueHeader + "";
                    requestHeads.remove(keyHeader);
                    break;
                }
            }
        }

        int contentTypeIndex = 0;
        if(TextUtils.isEmpty(mediaTypeStr)){
            contentTypeIndex = getRequestContentType().getContentTypeIndex();
        }else {
            if(ContentType.applicationJson.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.applicationJson.getContentTypeIndex();
            }else if(ContentType.appxformUrlencoded.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.appxformUrlencoded.getContentTypeIndex();
            }else if(ContentType.multipartFormData.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.multipartFormData.getContentTypeIndex();
            }else  if(ContentType.textXml.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.textXml.getContentTypeIndex();
            }
        }

        if(contentTypeIndex == ContentType.appxformUrlencoded.getContentTypeIndex()){

            FormBody.Builder formBuilder = new FormBody.Builder();
            Iterator iterator = requestParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                formBuilder.add(key + "", value + "");
            }
            requestBody = formBuilder.build();
            requestHeads.put("Content-Type", ContentType.appxformUrlencoded.getContentType());

        }else if(contentTypeIndex == ContentType.applicationJson.getContentTypeIndex()){

            MediaType mediaType = MediaType.parse(mediaTypeStr + "; charset=utf-8");
            requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(requestParams));

        }else  if(contentTypeIndex == ContentType.multipartFormData.getContentTypeIndex()){

            MediaType mediaType = MediaType.parse(mediaTypeStr + "; charset=utf-8");
            requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(requestParams));

        }else if(contentTypeIndex == ContentType.textXml.getContentTypeIndex()){

            MediaType mediaType = MediaType.parse(mediaTypeStr + "; charset=utf-8");
            requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(requestParams));

        }

        CacheControl.Builder builder = new CacheControl.Builder();
        CacheControl cacheControl = builder.noCache().noStore().build();
        Request.Builder requestBuilder = new Request.Builder()
                .cacheControl(cacheControl)
                .url(requestUrl)
                .put(requestBody);
        if (requestHeads != null && requestHeads.size() > 0) {
            requestBuilder.headers(Headers.of(requestHeads));
        }

        Request request = requestBuilder.build();
        return request;
    }

    private Request deleteRequestObject(String requestUrl,
                                        Map<String, String> requestParams,
                                        Map<String, String> requestHeads) {
        RequestBody requestBody = null;
        String mediaTypeStr = null;
        if (requestHeads != null) {
            Iterator iteratorHeader = requestHeads.entrySet().iterator();
            while (iteratorHeader.hasNext()) {
                Map.Entry entryHeader = (Map.Entry) iteratorHeader.next();
                Object keyHeader = entryHeader.getKey();
                Object valueHeader = entryHeader.getValue();
                if ((keyHeader + "").toLowerCase().equals("Content-Type".toLowerCase())) {
                    mediaTypeStr = valueHeader + "";
                    requestHeads.remove(keyHeader);
                    break;
                }
            }
        }

        int contentTypeIndex = 0;
        if(TextUtils.isEmpty(mediaTypeStr)){
            contentTypeIndex = getRequestContentType().getContentTypeIndex();
        }else {
            if(ContentType.applicationJson.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.applicationJson.getContentTypeIndex();
            }else if(ContentType.appxformUrlencoded.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.appxformUrlencoded.getContentTypeIndex();
            }else if(ContentType.multipartFormData.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.multipartFormData.getContentTypeIndex();
            }else  if(ContentType.textXml.getContentType().toLowerCase().equals(mediaTypeStr.toLowerCase())){
                contentTypeIndex = ContentType.textXml.getContentTypeIndex();
            }
        }

        if(contentTypeIndex == ContentType.appxformUrlencoded.getContentTypeIndex()){

            FormBody.Builder formBuilder = new FormBody.Builder();
            Iterator iterator = requestParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                formBuilder.add(key + "", value + "");
            }
            requestBody = formBuilder.build();
            requestHeads.put("Content-Type", ContentType.appxformUrlencoded.getContentType());

        }else if(contentTypeIndex == ContentType.applicationJson.getContentTypeIndex()){

            MediaType mediaType = MediaType.parse(mediaTypeStr + "; charset=utf-8");
            requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(requestParams));

        }else  if(contentTypeIndex == ContentType.multipartFormData.getContentTypeIndex()){

            MediaType mediaType = MediaType.parse(mediaTypeStr + "; charset=utf-8");
            requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(requestParams));

        }else if(contentTypeIndex == ContentType.textXml.getContentTypeIndex()){

            MediaType mediaType = MediaType.parse(mediaTypeStr + "; charset=utf-8");
            requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(requestParams));

        }

        CacheControl.Builder builder = new CacheControl.Builder();
        CacheControl cacheControl = builder.noCache().noStore().build();
        Request.Builder requestBuilder = new Request.Builder()
                .cacheControl(cacheControl)
                .url(requestUrl)
                .delete(requestBody);
        if (requestHeads != null && requestHeads.size() > 0) {
            requestBuilder.headers(Headers.of(requestHeads));
        }

        Request request = requestBuilder.build();
        return request;
    }


    /**
     * 准备发起请求
     *
     * @param request 准备发起请求的对象
     */
    private void processRequestStartCallBack(final XIBaseHttpRequestDelegate request) {
        if (request != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    request.willStartRequest(request);
                }
            });
        }
    }

    /**
     * 请求过程中的进度回调处理
     *
     * @param request       当前请求的对象
     * @param progress      请求进度
     * @param totalProgress 总的请求进度
     */
    private void processRequestLodingCallBack(final XIBaseHttpRequestDelegate request, final long progress, final long totalProgress) {
        if (request != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    request.execRequest(request, progress, totalProgress);
                }
            });
        }
    }

    /**
     * 请求错误的回调
     *
     * @param request        当前请求错误的对象
     * @param responseString 请求结果
     * @param bError         true请求错误，否则成功
     */
    private void processRequestFailCallBack(final XIBaseHttpRequestDelegate request, final String responseString, final Boolean bError) {
        if (request != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    synchronized (requestMapsLock) {
                        if (request instanceof XHttpRequest) {
                            XHttpRequest httpRequest = (XHttpRequest) request;
                            if (authRequestMaps.containsKey(httpRequest.getAuthId())) {
                                WeakHashMap<String, XIBaseHttpRequestDelegate> requests = authRequestMaps.get(httpRequest.getAuthId());
                                for (XIBaseHttpRequestDelegate requestDelegate : requests.values()) {
                                    if (requestDelegate instanceof XHttpRequest) {
                                        XHttpRequest httpRequestDelegate = (XHttpRequest) requestDelegate;
                                        if (httpRequestDelegate.getCallBackBlock() != null) {
                                            httpRequestDelegate.getCallBackBlock().callBack(httpRequestDelegate, responseString, bError);
                                        }

                                        if (httpRequestDelegate.getDelegate() != null) {
                                            httpRequestDelegate.getDelegate().completeDidRequest(httpRequestDelegate, responseString, bError);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (request instanceof XHttpRequest) {
                        XHttpRequest httpRequest = (XHttpRequest) request;
                        removeRequestWithAuthId(httpRequest.getAuthId());
                    }
                }
            });
        }
    }

    /**
     * 请求成功的回调
     *
     * @param request        当前请求错误的对象
     * @param responseString 请求结果
     * @param bError         true请求错误，否则成功
     */
    private void processRequestSuccessCallBack(final XIBaseHttpRequestDelegate request, final String responseString, final Boolean bError) {
        if (request != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    synchronized (requestMapsLock) {
                        if (request instanceof XHttpRequest) {
                            XHttpRequest httpRequest = (XHttpRequest) request;
                            if (authRequestMaps.containsKey(httpRequest.getAuthId())) {
                                WeakHashMap<String, XIBaseHttpRequestDelegate> requests = authRequestMaps.get(httpRequest.getAuthId());
                                for (XIBaseHttpRequestDelegate requestDelegate : requests.values()) {
                                    if (requestDelegate instanceof XHttpRequest) {
                                        XHttpRequest httpRequestDelegate = (XHttpRequest) requestDelegate;
                                        if (httpRequest.getCallBackBlock() != null) {
                                            httpRequestDelegate.getCallBackBlock().callBack(httpRequestDelegate, responseString, bError);
                                        }

                                        if (httpRequest.getDelegate() != null) {
                                            httpRequestDelegate.getDelegate().completeDidRequest(httpRequestDelegate, responseString, bError);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (request instanceof XHttpRequest) {
                        XHttpRequest httpRequest = (XHttpRequest) request;
                        removeRequestWithAuthId(httpRequest.getAuthId());
                    }
                }
            });
        }
    }

    /**
     * 请求重试状态回调
     *
     * @param oldRequest 旧的请求
     * @param newRequest 新的请求
     */
    private void processRequestRetryCallBack(final XIBaseHttpRequestDelegate oldRequest, final XIBaseHttpRequestDelegate newRequest) {
        if (oldRequest != null && newRequest != null) {
            removeRequest(oldRequest);
            appendRequest(newRequest);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    synchronized (requestMapsLock) {
                        if (newRequest instanceof XHttpRequest) {
                            XHttpRequest httpRequest = (XHttpRequest) newRequest;
                            if (authRequestMaps.containsKey(httpRequest.getAuthId())) {
                                WeakHashMap<String, XIBaseHttpRequestDelegate> requests = authRequestMaps.get(httpRequest.getAuthId());
                                for (XIBaseHttpRequestDelegate requestDelegate : requests.values()) {
                                    if (requestDelegate instanceof XHttpRequest) {
                                        XHttpRequest httpRequestDelegate = (XHttpRequest) requestDelegate;
                                        httpRequestDelegate.retryCount = httpRequest.retryCount;
                                        httpRequestDelegate.willRetry(oldRequest, requestDelegate);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 同步相同请求的同步操作
     *
     * @param request 当前请求的超时重试达到上限依然超时，同步各请求的请求次数
     */
    private void synchronousRetryCount(final XIBaseHttpRequestDelegate request) {
        if (request != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    synchronized (requestMapsLock) {
                        if (request instanceof XHttpRequest) {
                            XHttpRequest httpRequest = (XHttpRequest) request;
                            if (authRequestMaps.containsKey(httpRequest.getAuthId())) {
                                WeakHashMap<String, XIBaseHttpRequestDelegate> requests = authRequestMaps.get(httpRequest.getAuthId());
                                for (XIBaseHttpRequestDelegate requestDelegate : requests.values()) {
                                    if (requestDelegate instanceof XHttpRequest) {
                                        XHttpRequest httpRequestDelegate = (XHttpRequest) requestDelegate;
                                        httpRequestDelegate.retryCount = httpRequest.retryCount;
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * get短连接请求，异步请求
     *
     * @param requestUrl            请求url
     * @param requestParams         请求参数
     * @param responseDelegate      请求过程中的转态回调
     * @param responseCallBackBlock 请求结果的回调，主要用于业务处理
     * @return 返回当前请求的对象
     */
    public XIBaseHttpRequestDelegate getRequest(String requestUrl,
                                                Map<String, String> requestParams,
                                                XIBaseHttpResponseDelegate responseDelegate,
                                                XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        return getRequest(requestUrl, requestParams, null, responseDelegate, responseCallBackBlock);
    }

    /**
     * get短连接请求对象，可以支持自定义自定义头部，异步请求
     *
     * @param requestUrl            请求url
     * @param requestParams         请求参数集合
     * @param requestHeads          请求头部参数集合
     * @param responseDelegate      请求过程中的状态回调
     * @param responseCallBackBlock 请求结果回调，主要用于业务处理
     * @return 返回当前请求的请求对象
     */
    public XIBaseHttpRequestDelegate getRequest(final String requestUrl,
                                                Map<String, String> requestParams,
                                                Map<String, String> requestHeads,
                                                XIBaseHttpResponseDelegate responseDelegate,
                                                final XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        if(requestParams == null){
            requestParams = new HashMap<>();
        }
        if(requestHeads == null){
            requestHeads = new HashMap<>();
        }

        Request request = getRequestObject(requestUrl, requestParams, requestHeads);
        final Call call = client.newCall(request);
        final XHttpRequest httpRequest = new XHttpRequest(call, responseDelegate, responseCallBackBlock);

        //即将开始请求
        processRequestStartCallBack(httpRequest);
        //即将来请求进度
        processRequestLodingCallBack(httpRequest, 0, 100);

        final Map<String, String> finalRequestParams = requestParams;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean bNeedRequest = processRequest("get", requestUrl, finalRequestParams, httpRequest);

                if (!bNeedRequest) {
                    return ;
                }

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (e instanceof SocketTimeoutException) {
                            //超时
                            final XIBaseHttpRequestDelegate newRequestDelegate = httpRequest.retry();
                            if (newRequestDelegate != null) {
                                //准备重试请求
                                processRequestRetryCallBack(httpRequest, newRequestDelegate);

                                XHttpRequest newRequest = (XHttpRequest) newRequestDelegate;
                                client.newCall(newRequest.getRequest().request()).enqueue(this);
                            } else {
                                //重试次数到达上限
                                if (newRequestDelegate == null) {
                                    synchronousRetryCount(httpRequest);
                                } else {
                                    synchronousRetryCount(newRequestDelegate);
                                }
                                processRequestFailCallBack(httpRequest, null, true);
                            }
                        } else if (e instanceof SocketException) {
                            processRequestFailCallBack(httpRequest, null, true);
                        } else {
                            processRequestFailCallBack(httpRequest, null, true);
                        }
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response != null) {
                            final String responseString = response.body().string();
                            processRequestSuccessCallBack(httpRequest, responseString, false);
                        } else {
                            processRequestFailCallBack(httpRequest, null, true);
                        }
                    }
                });
            }
        }).start();

        return httpRequest;
    }

    /**
     * post短连接请求，异步操作
     *
     * @param requestUrl            请求链接
     * @param requestParams         请求参数
     * @param responseDelegate      请求过程中的状态回调代理
     * @param responseCallBackBlock 请求过程的业务回调
     * @return 返回请求对象
     */
    public XIBaseHttpRequestDelegate postRequest(String requestUrl,
                                                 Map<String, String> requestParams,
                                                 XIBaseHttpResponseDelegate responseDelegate,
                                                 XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        return postRequest(requestUrl, requestParams, null, responseDelegate, responseCallBackBlock);
    }

    /**
     * post短连接请求，且支持自定义header信息，异步操作
     *
     * @param requestUrl            请求链接
     * @param requestParam          请求参数
     * @param requestHeads          请求头部信息
     * @param responseDelegate      请求过程中的状态回调代理
     * @param responseCallBackBlock 请求结果的业务回调
     * @return 返回请求对象
     */
    public XIBaseHttpRequestDelegate postRequest(final String requestUrl,
                                                 Map<String, String> requestParam,
                                                 Map<String, String> requestHeads,
                                                 XIBaseHttpResponseDelegate responseDelegate,
                                                 final XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        if(requestParam == null){
            requestParam = new HashMap<>();
        }
        if(requestHeads == null){
            requestHeads = new HashMap<>();
        }

        Request request = postRequestObject(requestUrl, requestParam, requestHeads);
        final Call call = client.newCall(request);
        final XHttpRequest httpRequest = new XHttpRequest(call, responseDelegate, responseCallBackBlock);

        processRequestStartCallBack(httpRequest);
        processRequestLodingCallBack(httpRequest, 0, 100);

        final Map<String, String> finalRequestParam = requestParam;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean bNeedReuqest = processRequest("post", requestUrl, finalRequestParam, httpRequest);
                if (!bNeedReuqest) {
                    return ;
                }

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (e instanceof SocketTimeoutException) {
                            //超时
                            XIBaseHttpRequestDelegate newRequestDelegate = httpRequest.retry();
                            if (newRequestDelegate != null) {
                                //准备重试请求
                                processRequestRetryCallBack(httpRequest, newRequestDelegate);

                                XHttpRequest newRequest = (XHttpRequest) newRequestDelegate;
                                client.newCall(newRequest.getRequest().request()).enqueue(this);
                            } else {
                                //重试次数到达上限
                                if (newRequestDelegate == null) {
                                    synchronousRetryCount(httpRequest);
                                } else {
                                    synchronousRetryCount(newRequestDelegate);
                                }
                                processRequestFailCallBack(httpRequest, null, true);
                            }
                        } else if (e instanceof SocketException) {
                            processRequestFailCallBack(httpRequest, null, true);
                        } else {
                            processRequestFailCallBack(httpRequest, null, true);
                        }
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response != null) {
                            final String responseString = response.body().string();
                            processRequestSuccessCallBack(httpRequest, responseString, false);
                        } else {
                            processRequestFailCallBack(httpRequest, null, true);
                        }
                    }
                });
            }
        }).start();

        return httpRequest;
    }

    /**
     * 上传请求接口
     *
     * @param requestUrl            接口路径
     * @param filePath              待上传的文件路径
     * @param reqeustParams         请求参数
     * @param responseDelegate      请求过程中状态回调代理
     * @param responseCallBackBlock 请求结果业务回调
     * @return 返回请求对象
     */
    public XIBaseHttpRequestDelegate uploadRequest(String requestUrl,
                                                   String filePath,
                                                   Map<String, String> reqeustParams,
                                                   XIBaseHttpResponseDelegate responseDelegate,
                                                   XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        return uploadRequest(requestUrl, filePath, reqeustParams, null, responseDelegate, responseCallBackBlock);
    }

    /**
     * 上传请求接口
     *
     * @param requestUrl            请求接口路径
     * @param filePath              待上传的文件路径
     * @param requestParams         请求参数
     * @param requestHeaders        请求头信息
     * @param responseDelegate      请求过程中的状态回调
     * @param responseCallBackBlock 请求结果业务回调
     * @return 返回请求对象
     */
    public XIBaseHttpRequestDelegate uploadRequest(String requestUrl,
                                                   String filePath,
                                                   Map<String, String> requestParams,
                                                   Map<String, String> requestHeaders,
                                                   XIBaseHttpResponseDelegate responseDelegate,
                                                   XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        return null;
    }

    /**
     * 下载请求接口
     *
     * @param requestUrl            下载请求的地址
     * @param downloadFilePath      下载文件存储地址
     * @param requestParams         请求参数
     * @param responseDelegate      请求过程中的状态回调
     * @param responseCallBackBlock 请求结果业务回调
     * @return 返回请求对象
     */
    public XIBaseHttpRequestDelegate downloadRequest(String requestUrl,
                                                     String downloadFilePath,
                                                     Map<String, String> requestParams,
                                                     XIBaseHttpResponseDelegate responseDelegate,
                                                     XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        return downloadRequest(requestUrl, downloadFilePath, requestParams, null, responseDelegate, responseCallBackBlock);
    }

    /**
     * 下载请求接口
     *
     * @param requestUrl
     * @param downloadFilePath
     * @param requestParams
     * @param requestHeaders
     * @param responseDelegate
     * @param responseCallBackBlock
     * @return
     */
    public XIBaseHttpRequestDelegate downloadRequest(String requestUrl,
                                                     String downloadFilePath,
                                                     Map<String, String> requestParams,
                                                     Map<String, String> requestHeaders,
                                                     XIBaseHttpResponseDelegate responseDelegate,
                                                     XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        return null;
    }


    /**
     * put短连接请求，异步操作
     *
     * @param requestUrl            请求链接
     * @param requestParams         请求参数
     * @param responseDelegate      请求过程中的状态回调代理
     * @param responseCallBackBlock 请求过程的业务回调
     * @return 返回请求对象
     */
    public XIBaseHttpRequestDelegate putRequest(String requestUrl,
                                                Map<String, String> requestParams,
                                                XIBaseHttpResponseDelegate responseDelegate,
                                                XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        return putRequest(requestUrl, requestParams, null, responseDelegate, responseCallBackBlock);
    }

    /**
     * put短连接请求，且支持自定义header信息，异步操作
     *
     * @param requestUrl            请求链接
     * @param requestParam          请求参数
     * @param requestHeads          请求头部信息
     * @param responseDelegate      请求过程中的状态回调代理
     * @param responseCallBackBlock 请求结果的业务回调
     * @return 返回请求对象
     */
    public XIBaseHttpRequestDelegate putRequest(final String requestUrl,
                                                Map<String, String> requestParam,
                                                Map<String, String> requestHeads,
                                                XIBaseHttpResponseDelegate responseDelegate,
                                                final XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        if(requestParam == null){
            requestParam = new HashMap<>();
        }
        if(requestHeads == null){
            requestHeads = new HashMap<>();
        }

        Request request = putRequestObject(requestUrl, requestParam, requestHeads);
        final Call call = client.newCall(request);
        final XHttpRequest httpRequest = new XHttpRequest(call, responseDelegate, responseCallBackBlock);

        processRequestStartCallBack(httpRequest);
        processRequestLodingCallBack(httpRequest, 0, 100);

        final Map<String, String> finalRequestParam = requestParam;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean bNeedReuqest = processRequest("put", requestUrl, finalRequestParam, httpRequest);
                if (!bNeedReuqest) {
                    return ;
                }

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (e instanceof SocketTimeoutException) {
                            //超时
                            XIBaseHttpRequestDelegate newRequestDelegate = httpRequest.retry();
                            if (newRequestDelegate != null) {
                                //准备重试请求
                                processRequestRetryCallBack(httpRequest, newRequestDelegate);

                                XHttpRequest newRequest = (XHttpRequest) newRequestDelegate;
                                client.newCall(newRequest.getRequest().request()).enqueue(this);
                            } else {
                                //重试次数到达上限
                                if (newRequestDelegate == null) {
                                    synchronousRetryCount(httpRequest);
                                } else {
                                    synchronousRetryCount(newRequestDelegate);
                                }
                                processRequestFailCallBack(httpRequest, null, true);
                            }
                        } else if (e instanceof SocketException) {
                            processRequestFailCallBack(httpRequest, null, true);
                        } else {
                            processRequestFailCallBack(httpRequest, null, true);
                        }
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response != null) {
                            final String responseString = response.body().string();
                            processRequestSuccessCallBack(httpRequest, responseString, false);
                        } else {
                            processRequestFailCallBack(httpRequest, null, true);
                        }
                    }
                });
            }
        }).start();

        return httpRequest;
    }

    /**
     * delete短连接请求，异步操作
     *
     * @param requestUrl            请求链接
     * @param requestParams         请求参数
     * @param responseDelegate      请求过程中的状态回调代理
     * @param responseCallBackBlock 请求过程的业务回调
     * @return 返回请求对象
     */
    public XIBaseHttpRequestDelegate deleteRequest(String requestUrl,
                                                   Map<String, String> requestParams,
                                                   XIBaseHttpResponseDelegate responseDelegate,
                                                   XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        return deleteRequest(requestUrl, requestParams, null, responseDelegate, responseCallBackBlock);
    }

    /**
     * delete短连接请求，且支持自定义header信息，异步操作
     *
     * @param requestUrl            请求链接
     * @param requestParam          请求参数
     * @param requestHeads          请求头部信息
     * @param responseDelegate      请求过程中的状态回调代理
     * @param responseCallBackBlock 请求结果的业务回调
     * @return 返回请求对象
     */
    public XIBaseHttpRequestDelegate deleteRequest(final String requestUrl,
                                                   Map<String, String> requestParam,
                                                   Map<String, String> requestHeads,
                                                   XIBaseHttpResponseDelegate responseDelegate,
                                                   final XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        if(requestParam == null){
            requestParam = new HashMap<>();
        }
        if(requestHeads == null){
            requestHeads = new HashMap<>();
        }

        Request request = deleteRequestObject(requestUrl, requestParam, requestHeads);
        final Call call = client.newCall(request);
        final XHttpRequest httpRequest = new XHttpRequest(call, responseDelegate, responseCallBackBlock);

        processRequestStartCallBack(httpRequest);
        processRequestLodingCallBack(httpRequest, 0, 100);

        final Map<String, String> finalRequestParam = requestParam;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean bNeedReuqest = processRequest("delete", requestUrl, finalRequestParam, httpRequest);
                if (!bNeedReuqest) {
                    return ;
                }

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (e instanceof SocketTimeoutException) {
                            //超时
                            XIBaseHttpRequestDelegate newRequestDelegate = httpRequest.retry();
                            if (newRequestDelegate != null) {
                                //准备重试请求
                                processRequestRetryCallBack(httpRequest, newRequestDelegate);

                                XHttpRequest newRequest = (XHttpRequest) newRequestDelegate;
                                client.newCall(newRequest.getRequest().request()).enqueue(this);
                            } else {
                                //重试次数到达上限
                                if (newRequestDelegate == null) {
                                    synchronousRetryCount(httpRequest);
                                } else {
                                    synchronousRetryCount(newRequestDelegate);
                                }
                                processRequestFailCallBack(httpRequest, null, true);
                            }
                        } else if (e instanceof SocketException) {
                            processRequestFailCallBack(httpRequest, null, true);
                        } else {
                            processRequestFailCallBack(httpRequest, null, true);
                        }
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response != null) {
                            final String responseString = response.body().string();
                            processRequestSuccessCallBack(httpRequest, responseString, false);
                        } else {
                            processRequestFailCallBack(httpRequest, null, true);
                        }
                    }
                });
            }
        }).start();

        return httpRequest;
    }

}
