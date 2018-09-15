package com.framework.http;

import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.framework.http.request.BaseRequest;
import com.framework.model.result.BaseResult;
import com.framework.model.result.HomeResult;
import com.framework.parse.BaseHttpResponseCallBackBlock;
import com.framework.parse.BaseJSON;
import com.framework.utils.SDCardUtils;
import com.xframework_network.xhttp.XHttpRequest;
import com.xframework_network.xhttp.XHttpRequestManager;
import com.xframework_network.xhttp.XIBaseHttpRequestDelegate;
import com.xframework_network.xhttp.XIBaseHttpResponseCallBackBlock;
import com.xframework_network.xhttp.XIBaseHttpResponseDelegate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lanbiao on 2018/5/31
 * 项目的http基础请求管理器
 */
public class BaseHttpRequestManager extends XHttpRequestManager {

    /**
     * 接口名存放的字段名
     */
    final static String command = "command";

    /**
     * 加密解密对应的业务参数
     */
    final static String param = "param";

    /**
     * 接口请求主机地址或域名
     */
    protected String httpHostAddress;

    protected BaseHttpRequestManager(int connectTimeout, int readTimeout, int writeTimeout, long httpCacheMaxSize){
        super(connectTimeout,readTimeout,writeTimeout,httpCacheMaxSize);
    }

    /**
     * App公共业务参数
     * @param params 待添加公共参数到的参数集合
     */
    private void addDefaultParams(Map<String, String> params) {
        if(params == null)
            return;
//        params.put("account", Us);
//        params.put("token", TokenData.getToken());
        params.put("source", "2");
        params.put("channel", "huawei");
        params.put("clientId", "123456789");
        params.put("version", "1.0.0");
        params.put("osVersion", Build.VERSION.RELEASE);
        params.put("device", Build.MODEL);
    }


    @Override
    public String getAppCachePath() {
        return SDCardUtils.SDCARD_PATH;
    }

    /**
     * 处理请求签的业务逻辑
     * @param requestHeaders 请求header信息集合
     * @param requestParams 请求业务参数集合
     * @param responseDelegate 请求过程中状态回调代理
     * @param responseCallBackBlock 请求结果业务回调
     * @param <Result> 要解析的数据类型
     * @return 返回请求对象
     */
    private <Result extends BaseResult> String processStartRequestLogic(Map<String,String> requestHeaders,
                                                                   Map<String,Object> requestParams,
                                                                   XIBaseHttpResponseDelegate responseDelegate,
                                                                   final IBaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        if(requestHeaders == null){
            requestHeaders = new HashMap<>();
        }
        addDefaultParams(requestHeaders);

        String requestUrl = null;
        if(requestParams != null){
            String command = requestParams.get(BaseHttpRequestManager.command).toString();
            if(command != null){
                requestUrl = httpHostAddress + command;
            }
        }

        if(TextUtils.isEmpty(requestUrl)){
            //缺失command
            if(responseCallBackBlock != null){
                responseCallBackBlock.callBack(null,(Result)BaseResult.loseParamError(),true);
            }

            if(responseDelegate != null){
                responseDelegate.completeDidRequest(null,null,true);
            }
            return null;
        }

        String contentParams = (String) requestParams.get(BaseHttpRequestManager.param);
        try {
            //String encryptContentParams = AESUtil.encryptAES(contentParams,AESUtil.AES_KEY);
            //requestParams.put(BaseHttpRequestManager.param,encryptContentParams);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return requestUrl;
        }
    }

    private  <Result> Class<Result> getTClass(Object obj) {
        Type[] types = obj.getClass().getGenericInterfaces();
        Type[] actualTypeArguments = ((ParameterizedType) types[0]).getActualTypeArguments();
        Class<Result> tClass = (Class<Result>) (actualTypeArguments[0]);
        return tClass;
    }

    private <Result extends BaseResult> Result getObject(final Integer errorCode,
                                                         final String serverMsg,
                                                         final IBaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        Class<Result> cls = null;
        if(responseCallBackBlock instanceof BaseHttpResponseCallBackBlock){
            BaseHttpResponseCallBackBlock callBackBlock = (BaseHttpResponseCallBackBlock)responseCallBackBlock;
            cls = callBackBlock.getClazz();
        }else {
            cls = getTClass(responseCallBackBlock);
        }

        Object object = null;
        try {
            object = cls.newInstance();
            if(object instanceof BaseResult){
                BaseResult baseResult = (BaseResult)object;
                baseResult.setErrorCode(errorCode);
                baseResult.setServerMsg(serverMsg);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }finally {
            return (Result) object;
        }
    }

    /**
     * 请求完成后的结果处理
     * @param request 请求对象
     * @param response 请求原始结果
     * @param bError 是否存在错误
     * @param responseCallBackBlock 请求结果业务回调
     * @param <Result> 需要解析成的数据模型
     */
    private <Result extends BaseResult> void processRequestResultLogic(XIBaseHttpRequestDelegate request,
                                                                  Object response,
                                                                  boolean bError,
                                                                  final IBaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        if(bError){
            if(responseCallBackBlock != null){
                Result result = getObject(10002,"请求发生错误",responseCallBackBlock);
                responseCallBackBlock.callBack(request,result,response);
            }
        }else {
            String deencryContentParams = null;
            if(response != null){
                if(response instanceof String){
                    Result responseObj = null;
                    try {
                        //deencryContentParams = AESUtil.decryptAES(response.toString(),AESUtil.AES_KEY);
                        Class<Result> cls = null;
                        if(responseCallBackBlock instanceof BaseHttpResponseCallBackBlock){
                            BaseHttpResponseCallBackBlock callBackBlock = (BaseHttpResponseCallBackBlock)responseCallBackBlock;
                            cls = callBackBlock.getClazz();
                        }else {
                            cls = getTClass(responseCallBackBlock);
                        }

                        //responseObj = BaseJSON.getBaseInstence().parseObject(deencryContentParams,cls);
                    }catch (Exception e) {
                        e.printStackTrace();
                        if(responseCallBackBlock != null) {
                            Result result = getObject(10001,"请求成功,解析异常",responseCallBackBlock);
                            responseCallBackBlock.callBack(request, result, response);
                        }
                    }finally {
                    }

                    if(responseCallBackBlock != null){
                        responseCallBackBlock.callBack(request,responseObj,response);
                    }
                }
            }else {
                if(responseCallBackBlock != null){
                    Result result = getObject(0,"请求成功",responseCallBackBlock);
                    responseCallBackBlock.callBack(request,result,response);
                }
            }
        }
    }

    /**
     * get请求，不带header，异步
     * @param requestParams 请求参数集合
     * @param responseDelegate 请求过程中的状态回调代理
     * @param responseCallBackBlock 请求结果业务回调
     * @param <Result> 要解析的数据模型
     * @return 返回请求对象
     */
    public <Result extends BaseResult> XIBaseHttpRequestDelegate getRequest(Map<String,Object> requestParams,
                                                                       XIBaseHttpResponseDelegate responseDelegate,
                                                                       IBaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        return getRequest((Map<String, String>) null,requestParams,responseDelegate,responseCallBackBlock);
    }

    /**
     * get请求,可以自定义header信息，异步
     * @param requestHeaders 自定义的header信息集合
     * @param requestParams 请求参数集合
     * @param responseDelegate 请求过程中状态回调代理
     * @param responseCallBackBlock 请求结果业务回调
     * @param <Result> 要解析的数据模型
     * @return 返回请求的对象
     */
    public <Result extends BaseResult> XIBaseHttpRequestDelegate getRequest(Map<String,String> requestHeaders,
                                                                       Map<String,Object> requestParams,
                                                                       XIBaseHttpResponseDelegate responseDelegate,
                                                                       final IBaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        String requestUrl = processStartRequestLogic(requestHeaders,requestParams,responseDelegate,responseCallBackBlock);
        if(TextUtils.isEmpty(requestUrl)){
            return null;
        }

        XIBaseHttpRequestDelegate request = getRequest(requestUrl, requestParams, requestHeaders, responseDelegate, new XIBaseHttpResponseCallBackBlock() {
            @Override
            public void callBack(XIBaseHttpRequestDelegate request, Object response, boolean bError) {
                processRequestResultLogic(request,response,bError,responseCallBackBlock);
            }
        });

        String command = null;
        if(requestParams != null){
            command = requestParams.get(BaseHttpRequestManager.command).toString();
            if(request instanceof XHttpRequest){
                XHttpRequest httpRequest = (XHttpRequest)request;
                httpRequest.setCommand(command);
            }
        }

        return request;
    }

    /**
     * get请求
     * @param requestModel 请求参数集合
     * @param responseDelegate  请求过程中的状态回调代理
     * @param responseCallBackBlock  请求结果业务回调
     * @param <Result> 要解析的数据模型
     * @return 返回请求对象
     */
    public <Result extends BaseResult> XIBaseHttpRequestDelegate getRequest(BaseRequest requestModel,
                                                                             XIBaseHttpResponseDelegate responseDelegate,
                                                                             final IBaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        if(requestModel == null){
            return null;
        }

        return getRequest(requestModel.getRequestHeaders(),requestModel.getRequestParams(),responseDelegate,responseCallBackBlock);
    }

    /**
     * post请求，不带header信息，异步
     * @param requestParams 请求参数集合
     * @param responseDelegate 请求过程中的状态回调
     * @param responseCallBackBlock 请求结果回调
     * @param <Result> 要解析的数据模型
     * @return 返回请求对象
     */
    public <Result extends BaseResult> XIBaseHttpRequestDelegate postRequest(Map<String,Object> requestParams,
                                                                         XIBaseHttpResponseDelegate responseDelegate,
                                                                         IBaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        return postRequest((Map<String, String>) null,requestParams,responseDelegate,responseCallBackBlock);
    }

    /**
     * post请求，自定义header信息，异步
     * @param requestHeaders 请求header信息
     * @param requestParams 请求参数信息
     * @param responseDelegate 请求过程中的状态回调代理
     * @param responseCallBackBlock 请求结果业务回调
     * @param <Result> 要解析的数据模型
     * @return 返回请求对象
     */
    public <Result extends BaseResult> XIBaseHttpRequestDelegate postRequest(Map<String,String> requestHeaders,
                                                                        Map<String,Object> requestParams,
                                                                        XIBaseHttpResponseDelegate responseDelegate,
                                                                        final IBaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        String requestUrl = processStartRequestLogic(requestHeaders,requestParams,responseDelegate,responseCallBackBlock);
        if(TextUtils.isEmpty(requestUrl)){
            return null;
        }

        XIBaseHttpRequestDelegate request = postRequest(requestUrl, requestParams, requestHeaders, responseDelegate, new XIBaseHttpResponseCallBackBlock() {
            @Override
            public void callBack(XIBaseHttpRequestDelegate request, Object response, boolean bError) {
                processRequestResultLogic(request,response,bError,responseCallBackBlock);
            }
        });

        String command = null;
        if(requestParams != null){
            command = requestParams.get(BaseHttpRequestManager.command).toString();
            if(request instanceof XHttpRequest){
                XHttpRequest httpRequest = (XHttpRequest)request;
                httpRequest.setCommand(command);
            }
        }

        return request;
    }

    /**
     * post请求
     * @param requestModel 请求参数集合
     * @param responseDelegate  请求过程中的状态回调代理
     * @param responseCallBackBlock  请求结果业务回调
     * @param <Result> 要解析的数据模型
     * @return 返回请求对象
     */
    public <Result extends BaseResult> XIBaseHttpRequestDelegate postRequest(BaseRequest requestModel,
                                                                             XIBaseHttpResponseDelegate responseDelegate,
                                                                             final IBaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        if(requestModel == null){
            return null;
        }

        return postRequest(requestModel.getRequestHeaders(),requestModel.getRequestParams(),responseDelegate,responseCallBackBlock);
    }
}
