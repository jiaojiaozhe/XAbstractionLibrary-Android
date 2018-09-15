package com.xframework_network.xhttp;

/**
 * Create by lanbiao on 2018/5/30
 * http请求回调代理，主要用于一些请求状态的回调状态
 */
public interface XIBaseHttpResponseDelegate {

    /**
     * 即将开始准备请求
     * @param request   请求对象
     */
    void willStartRequest(XIBaseHttpRequestDelegate request);

    /**
     * 即将重试请求
     * @param oldRequest   旧的请求对象
     * @param newRequest   新的请求对象
     */
    void willRetryRequest(XIBaseHttpRequestDelegate oldRequest, XIBaseHttpRequestDelegate newRequest);

    /**
     * 请求进度
     * @param request 请求对象
     * @param progress 请求进度量
     * @param totalProgress 请求总的进度量
     */
    void execRequest(XIBaseHttpRequestDelegate request, long progress, long totalProgress);

    /**
     * 请求结果
     * @param request   请求对象
     * @param responseString 请求结果字符串
     * @param bError   是否请求错误
     */
    void completeDidRequest(XIBaseHttpRequestDelegate request, String responseString, Boolean bError);

    /**
     * 取消请求，可能是系统行为，也可能是用户主动行为
     * @param request 待取消的请求对象
     */
    void cancelRequest(XIBaseHttpRequestDelegate request);
}
