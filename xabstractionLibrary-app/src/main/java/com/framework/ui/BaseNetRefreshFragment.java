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
import com.xframework_uicommon.xview.XBaseNetRefreshFragment;
import com.xframework_uicommon.xview.xerrorview.XIBaseErrorViewDelegate;
import com.xframework_uicommon.xview.xloadingview.XIBaseLoadingViewDelegate;
import com.xframework_uicommon.xview.xnodataview.XIBaseNoDataViewDelegate;
import com.xframework_uicommon.xview.xnonetview.XIBaseNoNetViewDelegate;
import com.zhht.xabstractionlibrary.R;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cn.appsdream.nestrefresh.base.AbsRefreshLayout;

/**
 * created by lanbiao 2018/06/26
 * @param <M> RecyclerView或ListView中item对应的数据源
 * @param <Result> 当前页面主接口的返回数据模型
 */
public abstract class BaseNetRefreshFragment<M,Result extends BaseNetRefreshResult> extends XBaseNetRefreshFragment<M> {

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

    @Override
    public AbsRefreshLayout.LoaderDecor refreshHeaderView() {
        return super.refreshHeaderView();
    }

    @Override
    public AbsRefreshLayout.LoaderDecor loadMoreView() {
        return super.loadMoreView();
    }

    /**
     * 填充接口刷新业务参数集合
     * @param requestParams 请求参数集合
     */
    public abstract void refreshRequestParams(HashMap requestParams);

    /**
     * 填充接口下拉业务参数集合
     * @param requestParams 请求参数集合
     */
    public abstract void loadMoreRequestParams(HashMap requestParams);

    /**
     * 获取解析类型
     * @return 返回需要解析的类型
     */
    private Class<Result> getParseClass(){
        Class<Result> tClass = null;
        Type type = getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType){
            Type[] types = ((ParameterizedType)type).getActualTypeArguments();
            if(types != null && types.length > 1){
                tClass = (Class<Result>) types[1];
            }

        }
        return tClass;
    }

    @Override
    protected void loadPage() {
        Class<Result> tClass =  getParseClass();
        HashMap<String,Object> requestParams = new HashMap<>();
        refreshRequestParams(requestParams);

        postRequest(requestParams, this, new BaseHttpResponseCallBackBlock <Result>(tClass) {
            @Override
            public void callBack(XIBaseHttpRequestDelegate request, Result result, Object resultObj) {
                if(result != null && result.isSuccess()){
                    if(result instanceof BaseNetRefreshResult){
                        BaseNetRefreshResult netRefreshResult = (BaseNetRefreshResult)result;
                        refreshControl(/*待设计*/netRefreshResult.getDataList(),false);
                    }
                }else {
                    refreshControl(null,true);
                }
            }

            @Override
            public Boolean existsData(XIBaseHttpRequestDelegate request, Result result, Object resultObj) {
                if(result != null){
                    if(result instanceof BaseNetRefreshResult){
                        BaseNetRefreshResult netRefreshResult = (BaseNetRefreshResult)result;
                        if(netRefreshResult.getDataList().size() > 0)
                            return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void loadMore() {
        Class<Result> tClass =  getParseClass();
        HashMap<String,Object> requestParams = new HashMap<>();
        loadMoreRequestParams(requestParams);
        postRequest(requestParams, this, new BaseHttpResponseCallBackBlock<Result>(tClass){
            @Override
            public void callBack(XIBaseHttpRequestDelegate request, Result result, Object resultObj) {
                if(result != null && result.isSuccess()){
                    if(result instanceof BaseNetRefreshResult){
                        BaseNetRefreshResult demoNetRefreshResult = (BaseNetRefreshResult)result;
                        loadMoreControl(/*待设计*/demoNetRefreshResult.getDataList(),false);
                    }
                }else {
                    loadMoreControl(null,true);
                }
            }

            @Override
            public Boolean existsData(XIBaseHttpRequestDelegate request, Result result, Object resultObj) {
                if(result != null){
                    if(result instanceof BaseNetRefreshResult){
                        BaseNetRefreshResult netRefreshResult = (BaseNetRefreshResult)result;
                        if(netRefreshResult.getDataList().size() > 0)
                            return true;
                    }
                }
                return false;
            }
        });
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
                                                                        BaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
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
    protected <Result extends BaseResult> XIBaseHttpRequestDelegate postRequest(Map<String,String> requestHeaders,
                                                                           Map<String,Object> requestParams,
                                                                           XIBaseHttpResponseDelegate responseDelegate,
                                                                           final BaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        return HttpRequestManager.getInstance().postRequest(requestHeaders, requestParams, responseDelegate, new BaseHttpResponseCallBackBlock<Result>(responseCallBackBlock.getClazz()) {
            @Override
            public void callBack(XIBaseHttpRequestDelegate request, Result result, Object resultObj) {
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
            public Boolean existsData(XIBaseHttpRequestDelegate request, Result result, Object resultObj) {
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
     * @param <Result> 要解析的数据模型
     * @return 返回请求对象
     */
    public <Result extends BaseResult> XIBaseHttpRequestDelegate getRequest(Map<String,Object> requestParams,
                                                                       XIBaseHttpResponseDelegate responseDelegate,
                                                                       BaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
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
                                                                       final BaseHttpResponseCallBackBlock<Result> responseCallBackBlock){
        return HttpRequestManager.getInstance().getRequest(requestHeaders, requestParams, responseDelegate, new BaseHttpResponseCallBackBlock<Result>(responseCallBackBlock.getClazz()) {
            @Override
            public void callBack(XIBaseHttpRequestDelegate request, Result result, Object resultObj) {
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
            public Boolean existsData(XIBaseHttpRequestDelegate request, Result result, Object resultObj) {
                Boolean existsData = true;
                if(responseCallBackBlock != null){
                    existsData = responseCallBackBlock.existsData(request,result,resultObj);
                }
                return existsData;
            }
        });
    }
}
