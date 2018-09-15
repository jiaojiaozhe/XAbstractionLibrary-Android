package com.xframework_network.xhttp;

/**
 * Created by lanbiao on 2018/5/30
 * 接口请求回调执行体，主要用于业务逻辑的处理
 */
public interface XIBaseHttpResponseCallBackBlock {

    /**
     * 接口请求结果业务回调
     * @param request   本次请求对象
     * @param response  请求结果
     * @param bError    是否请求错误
     */
    void callBack(XIBaseHttpRequestDelegate request, Object response, boolean bError);
}
