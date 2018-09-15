package com.framework.http;

import com.framework.model.result.BaseResult;
import com.xframework_network.xhttp.XIBaseHttpRequestDelegate;

/**
 * 接口请求业务回调接口
 * @param <Result> 要解析的数据模型
 */
public interface IBaseHttpResponseCallBackBlock<Result extends BaseResult> {

    /**
     * 接口请求结果回调
     * @param request 接口请求对象
     * @param result 接口请求结果，数据模型
     * @param resultObj 接口请求结果，原始数据
     */
    void callBack(XIBaseHttpRequestDelegate request, Result result, Object resultObj);
}
