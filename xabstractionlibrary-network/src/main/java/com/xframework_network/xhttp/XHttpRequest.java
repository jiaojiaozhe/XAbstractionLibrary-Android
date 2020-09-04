package com.xframework_network.xhttp;


import com.xframework_base.xmodel.XBaseModel;

import java.lang.ref.WeakReference;

import okhttp3.Call;

/**
 * Create by lanbiao on 2018/5/29
 * http请求抽象基础对象，唯一表示一次请求，可以是短连接、上传、下载等
 */
public class XHttpRequest extends XBaseModel implements XIBaseHttpRequestDelegate {

    /**
     * 请求对象
     */
    private Call request;

    /**
     * 接口名
     */
    public String command = null;

    /**
     * 重试次数
     */
    public int retryCount = 3;

    /**
     * 请求的身份id，多个相同的请求次id相同
     */
    private String authId = null;

    /**
     * 请求拥有者，弱引用对象，在gc时无论内存是否紧张都会回收
     */
    WeakReference<XIBaseHttpResponseDelegate> owner = null;

    /**
     * 请求对应的业务回调执行体，在gc时无论内存是否紧张都会回收
     */
    XIBaseHttpResponseCallBackBlock ownerCallBack = null;

    public XHttpRequest(){
        super();
    }


    public XHttpRequest(Call request, XIBaseHttpResponseDelegate newDelegate, XIBaseHttpResponseCallBackBlock newCallBaclBlock){
        this();
        this.request = request;
        if(newDelegate != null){
            owner = new WeakReference<>(newDelegate);
        }

        ownerCallBack = newCallBaclBlock;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setRequest(Call request) {
        this.request = request;
    }

    public Call getRequest() {
        return request;
    }

    public void setDelegate(XIBaseHttpResponseDelegate newDelegate){
        if(newDelegate != null){
            owner = new WeakReference<>(newDelegate);
        }else {
            if(owner != null){
                owner.clear();
                owner = null;
            }
        }
    }

    public XIBaseHttpResponseDelegate getDelegate(){
        if(owner == null){
            return null;
        }

        XIBaseHttpResponseDelegate oldDelegate = owner.get();
        return oldDelegate;
    }

    public void setCallBackBlock(XIBaseHttpResponseCallBackBlock newCallBackBlock){
        ownerCallBack = newCallBackBlock;
    }

    public XIBaseHttpResponseCallBackBlock getCallBackBlock(){
        return ownerCallBack;
    }

    @Override
    public void cancel() {
        if(request != null &&
                (!request.isCanceled() || request.isExecuted())){
            request.cancel();
        }

        XIBaseHttpResponseDelegate responseDelegate = getDelegate();
        if(responseDelegate != null){
            responseDelegate.cancelRequest(this);
        }

        setDelegate(null);
        setCallBackBlock(null);
        //TODO 是否需要去XHttpRequestManager删除对应对象
    }

    @Override
    public XIBaseHttpRequestDelegate retry() {
        retryCount --;
        if(retryCount > 0){
            return this;
        }
        return null;
    }

    @Override
    public void addDelegate(XIBaseHttpResponseDelegate requestCallBack) {
        setDelegate(requestCallBack);
    }

    @Override
    public void removeDeleagate(XIBaseHttpResponseDelegate requestCallBack) {
        XIBaseHttpResponseDelegate responseDelegate = getDelegate();
        if(responseDelegate == requestCallBack){
            setDelegate(null);
        }
    }

    @Override
    public void addCallBlock(XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        setCallBackBlock(responseCallBackBlock);
    }

    @Override
    public void removeCallBlock(XIBaseHttpResponseCallBackBlock responseCallBackBlock) {
        XIBaseHttpResponseCallBackBlock oldResponseCallBackBlock = getCallBackBlock();
        if(oldResponseCallBackBlock == responseCallBackBlock){
            setDelegate(null);
        }
    }

    @Override
    public void willStartRequest(XIBaseHttpRequestDelegate request) {
        XIBaseHttpResponseDelegate responseDelegate = getDelegate();
        if(responseDelegate != null){
            responseDelegate.willStartRequest(request);
        }
    }

    @Override
    public void willRetry(XIBaseHttpRequestDelegate oldRequest, XIBaseHttpRequestDelegate newRequest) {
        XIBaseHttpResponseDelegate responseDelegate = getDelegate();
        if(responseDelegate != null){
            responseDelegate.willRetryRequest(oldRequest,newRequest);
        }
    }

    @Override
    public void execRequest(XIBaseHttpRequestDelegate request, long progress, long totalProgree) {
        XIBaseHttpResponseDelegate responseDelegate = getDelegate();
        if(responseDelegate != null){
            responseDelegate.execRequest(request,progress,totalProgree);
        }
    }

    @Override
    public void completeDidRequest(XIBaseHttpRequestDelegate request, String responseString, boolean bError) {
        XIBaseHttpResponseDelegate responseDelegate = getDelegate();
        if(responseDelegate != null){
            responseDelegate.completeDidRequest(request,responseString,bError);
        }
    }
}
