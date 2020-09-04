package com.framework.ui;

import android.view.View;

import com.framework.http.HttpRequestManager;
import com.framework.model.result.BaseResult;
import com.framework.parse.BaseHttpResponseCallBackBlock;
import com.framework.ui.errorview.BaseErrorView;
import com.framework.ui.loadingview.BaseLoadingView;
import com.framework.ui.nodataview.BaseNoDataView;
import com.framework.ui.nonetview.BaseNoNetView;
import com.xframework_network.xhttp.XIBaseHttpRequestDelegate;
import com.xframework_network.xhttp.XIBaseHttpResponseDelegate;
import com.xframework_uicommon.xview.XBaseFragment;
import com.xframework_uicommon.xview.xerrorview.XIBaseErrorViewDelegate;
import com.xframework_uicommon.xview.xloadingview.XIBaseLoadingViewDelegate;
import com.xframework_uicommon.xview.xnodataview.XIBaseNoDataViewDelegate;
import com.xframework_uicommon.xview.xnonetview.XIBaseNoNetViewDelegate;
import com.personal.xabstractionlibrary.R;

import java.util.Map;

/**
 * 实现层BaseFragment
 */
public abstract class BaseFragment extends XBaseFragment{

    @Override
    protected XIBaseLoadingViewDelegate loadLoadingView() {
        BaseLoadingView loadingView = (BaseLoadingView)View.inflate(getContext(), R.layout.base_loading_view_layout,null);
        if(loadingView != null){
            loadingView.initView();
        }
        return loadingView;
    }

    @Override
    protected XIBaseErrorViewDelegate loadErrorView() {
        BaseErrorView errorView = (BaseErrorView)View.inflate(getContext(), R.layout.base_error_view_layout,null);
        if(errorView != null){
            errorView.setDelegate(this);
            errorView.initView();
        }
        return errorView;
    }

    @Override
    protected XIBaseNoNetViewDelegate loadNotNetView() {
        BaseNoNetView noNetView = (BaseNoNetView)View.inflate(getContext(),R.layout.base_nonet_view_layout,null);
        if(noNetView != null){
            noNetView.setDelegate(this);
            noNetView.initView();
        }
        return noNetView;
    }

    @Override
    protected XIBaseNoDataViewDelegate loadNotDataView() {
        BaseNoDataView noDataView = (BaseNoDataView) View.inflate(getContext(),R.layout.base_nodata_view_layout,null);
        if(noDataView != null){
            noDataView.setDelegate(this);
            noDataView.initView();
        }
        return noDataView;
    }

    /**
     * post请求，不带header信息，异步
     * @param requestParams 请求参数集合
     * @param responseDelegate 请求过程中的状态回调
     * @param responseCallBackBlock 请求结果回调
     * @param <T> 要解析的数据模型
     * @return 返回请求对象
     */
    public <T extends BaseResult> XIBaseHttpRequestDelegate postRequest(Map<String,String> requestParams,
                                                                        XIBaseHttpResponseDelegate responseDelegate,
                                                                        BaseHttpResponseCallBackBlock<T> responseCallBackBlock){
        return postRequest((Map<String, String>) null,requestParams,responseDelegate,responseCallBackBlock);
    }

    /**
     * post请求，自定义header信息，异步
     * @param requestHeaders 请求header信息
     * @param requestParams 请求参数信息
     * @param responseDelegate 请求过程中的状态回调代理
     * @param responseCallBackBlock 请求结果业务回调
     * @param <T> 要解析的数据模型
     * @return 返回请求对象
     */
    protected <T extends BaseResult> XIBaseHttpRequestDelegate postRequest(Map<String,String> requestHeaders,
                                                                    Map<String,String> requestParams,
                                                                    XIBaseHttpResponseDelegate responseDelegate,
                                                                    final BaseHttpResponseCallBackBlock<T> responseCallBackBlock){
        return HttpRequestManager.getInstance().postRequest(requestHeaders, requestParams, responseDelegate, new BaseHttpResponseCallBackBlock<T>(responseCallBackBlock.getClazz()) {
            @Override
            public void callBack(XIBaseHttpRequestDelegate request, T result, Object resultObj) {

                if(result.isSuccess()){
                    if(responseCallBackBlock != null){
                        Boolean existsData = responseCallBackBlock.existsData(request,result,resultObj);
                        if(!existsData){
                            setbNotData(true);
                        }else {
                            setbIgnoreShowError(true);
                        }
                        responseCallBackBlock.callBack(request,result,resultObj);
                    }
                }else {
                    if(responseCallBackBlock != null){
                        responseCallBackBlock.callBack(request,result,resultObj);
                    }
                }
            }

            @Override
            public Boolean existsData(XIBaseHttpRequestDelegate request, T result, Object resultObj) {
                Boolean existsData = false;
                if(responseCallBackBlock != null){
                    existsData = responseCallBackBlock.existsData(request,result,resultObj);
                }
                return existsData;
            }
        });
    }

    /**
     * get请求，不带header，异步
     * @param requestParams 请求参数集合
     * @param responseDelegate 请求过程中的状态回调代理
     * @param responseCallBackBlock 请求结果业务回调
     * @param <T> 要解析的数据模型
     * @return 返回请求对象
     */
    public <T extends BaseResult> XIBaseHttpRequestDelegate getRequest(Map<String,String> requestParams,
                                                                       XIBaseHttpResponseDelegate responseDelegate,
                                                                       BaseHttpResponseCallBackBlock<T> responseCallBackBlock){
        return getRequest((Map<String, String>) null,requestParams,responseDelegate,responseCallBackBlock);
    }

    /**
     * get请求,可以自定义header信息，异步
     * @param requestHeaders 自定义的header信息集合
     * @param requestParams 请求参数集合
     * @param responseDelegate 请求过程中状态回调代理
     * @param responseCallBackBlock 请求结果业务回调
     * @param <T> 要解析的数据模型
     * @return 返回请求的对象
     */
    public <T extends BaseResult> XIBaseHttpRequestDelegate getRequest(Map<String,String> requestHeaders,
                                                                       Map<String,String> requestParams,
                                                                       XIBaseHttpResponseDelegate responseDelegate,
                                                                       final BaseHttpResponseCallBackBlock<T> responseCallBackBlock){
        return HttpRequestManager.getInstance().getRequest(requestHeaders, requestParams, responseDelegate, new BaseHttpResponseCallBackBlock<T>(responseCallBackBlock.getClazz()) {
            @Override
            public void callBack(XIBaseHttpRequestDelegate request, T result, Object resultObj) {
                if(result.isSuccess()){
                    if(responseCallBackBlock != null){
                        Boolean existsData = responseCallBackBlock.existsData(request,result,resultObj);
                        if(!existsData){
                            setbNotData(true);
                        }else {
                            setbIgnoreShowError(true);
                        }
                        responseCallBackBlock.callBack(request,result,resultObj);
                    }
                }else {
                    if(responseCallBackBlock != null){
                        responseCallBackBlock.callBack(request,result,resultObj);
                    }
                }
            }

            @Override
            public Boolean existsData(XIBaseHttpRequestDelegate request, T result, Object resultObj) {
                Boolean existsData = true;
                if(responseCallBackBlock != null){
                    existsData = responseCallBackBlock.existsData(request,result,resultObj);
                }
                return existsData;
            }
        });
    }
}
