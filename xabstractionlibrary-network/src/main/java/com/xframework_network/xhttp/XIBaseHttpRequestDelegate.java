package com.xframework_network.xhttp;

import java.lang.ref.WeakReference;

/**
 * Create by lanbiao on 2018/5/29
 * 基础请求对象，表示唯一一次请求，可以是上传、下载、短连接
 */
public interface XIBaseHttpRequestDelegate {

    /**
     * 接口名
     */
    String command = null;

    /**
     * 重试次数
     */
    int retryCount = 3;

    /**
     * 请求的身份id，多个相同的请求次id相同
     */
    String authId = null;

    /**
     * 请求拥有者，弱引用对象，在gc时无论内存是否紧张都会回收
     */
    WeakReference<XIBaseHttpResponseDelegate> owner = null;

    /**
     * 请求对应的业务回调执行体
     */
    XIBaseHttpResponseCallBackBlock ownerCallBack = null;

    /**
     * 取消请求
     */
    void cancel();

    /**
     * 重试请求
     * @return 返回请求对象
     */
    XIBaseHttpRequestDelegate retry();

    /**
     * 添加接口与代理之间的依赖关系
     * @param requestCallBack 请求回调代理
     */
    void addDelegate(XIBaseHttpResponseDelegate requestCallBack);

    /**
     * 删除接口与代理之间的依赖关系
     * @param requestCallBack 待删除的请求回调代理
     */
    void removeDeleagate(XIBaseHttpResponseDelegate requestCallBack);

    /**
     * 添加回调执行体到请求相关的对象
     * @param responseCallBackBlock 请求回调执行体，业务有关的
     */
    void addCallBlock(XIBaseHttpResponseCallBackBlock responseCallBackBlock);

    /**
     * 删除回调执行体到请求相关的对象
     * @param responseCallBackBlock 待删除的请求执行体，业务有关的
     */
    void removeCallBlock(XIBaseHttpResponseCallBackBlock responseCallBackBlock);

    /**
     * 即将开始准备请求
     * @param request 请求对象
     */
    void willStartRequest(XIBaseHttpRequestDelegate request);

    /**
     * 请求重试
     * @param oldRequest 旧的请求
     * @param newRequest 新的请求
     */
    void willRetry(XIBaseHttpRequestDelegate oldRequest, XIBaseHttpRequestDelegate newRequest);

    /**
     * 请求进度
     * @param request 当前的请求对象
     * @param progress  请求进度
     * @param totalProgree 总的进度大小
     */
    void execRequest(XIBaseHttpRequestDelegate request, long progress, long totalProgree);

    /**
     * 请求回调
     * @param request   请求对象
     * @param responseString    请求回调结果，失败为空
     * @param bError    是否失败
     */
    void completeDidRequest(XIBaseHttpRequestDelegate request, String responseString, boolean bError);
}
