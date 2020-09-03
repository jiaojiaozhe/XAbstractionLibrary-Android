package com.xframework_uicommon.xview;


import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xframework_base.xconfig.XConfig;
import com.xframework_base.xmodel.XBaseModel;
import com.xframework_network.xhttp.XHttpRequest;
import com.xframework_network.xhttp.XIBaseHttpRequestDelegate;
import com.xframework_network.xhttp.XIBaseHttpResponseDelegate;
import com.xframework_network.xhttp.XNetUtil;
import com.xframework_uicommon.R;
import com.xframework_uicommon.xview.xerrorview.XIBaseErrorViewDelegate;
import com.xframework_uicommon.xview.xerrorview.XIBaseRetryDelegate;
import com.xframework_uicommon.xview.xloadingview.XIBaseLoadingViewDelegate;
import com.xframework_uicommon.xview.xnodataview.XIBaseNoDataRetryDelegate;
import com.xframework_uicommon.xview.xnodataview.XIBaseNoDataViewDelegate;
import com.xframework_uicommon.xview.xnonetview.XIBaseNoNetRetryDelegate;
import com.xframework_uicommon.xview.xnonetview.XIBaseNoNetViewDelegate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lanbiao on 2018/06/10
 * 抽象基础fragment，无法滑动页面直接继承
 */
public abstract class XBaseFragment extends Fragment implements XIBaseHttpResponseDelegate,XIBaseRetryDelegate,XIBaseNoDataRetryDelegate,XIBaseNoNetRetryDelegate {

    /**
     * 根容器
     */
    private RelativeLayout mBaseRootView;

    /**
     * 内容容器
     */
    private RelativeLayout mContentView;

    /**
     * 错误内容区，与业务无关，基本属于抽象
     */
    private RelativeLayout mErrorContextView;

    /**
     * 是否显示error_content_view中的内容，一般会在加载出数据之后，由业务调用
     */
    private Boolean bIgnoreShowError = false;

    /**
     * bIgnoreShowError的数据同步锁
     */
    private Serializable ignoreShowErrorleLock = new XBaseModel() {
    };

    /**
     * 是否存在错误状态
     */
    private Boolean bError = false;

    /**
     * bError数据同步锁
     */
    private Serializable errorLock = new XBaseModel() {
    };

    /**
     * 是否无网络
     */
    private Boolean bNotNet = false;

    /**
     * bNotNet数据同步锁
     */
    private Serializable notNetLock = new XBaseModel() {
    };

    /**
     * 是否无数据
     */
    private Boolean bNotData = false;

    /**
     * bNotData数据同步锁
     */
    private Serializable notDataLock = new XBaseModel() {
    };

    /**
     * 是否正在请求过程中
     */
    private Boolean bLoading = false;

    /**
     * 加载过程中的同步锁
     */
    private Serializable loadingLock = new XBaseModel() {
    };

    /**
     * UI刷新同步锁
     */
    private Serializable refreshUILock = new XBaseModel() {
    };

    /**
     * 加载过程中内容区
     */
    private XIBaseLoadingViewDelegate loadingViewDelegate = null;

    /**
     * 错误内容区
     */
    private XIBaseErrorViewDelegate errorViewDelegate = null;

    /**
     * 无网内容区
     */
    private XIBaseNoNetViewDelegate noNetViewDelegate = null;

    /**
     * 无数据内容区
     */
    private XIBaseNoDataViewDelegate noDataViewDelegate = null;

    /**
     * 请求集合，不建议用，推荐用任务推荐
     */
    private Map<String,XIBaseHttpRequestDelegate> requests = new HashMap<>();

    public Boolean isbIgnoreShowError() {
        synchronized (ignoreShowErrorleLock) {
            return bIgnoreShowError;
        }
    }

    public void setbIgnoreShowError(Boolean bIgnoreShowError) {
        synchronized (ignoreShowErrorleLock) {
            this.bIgnoreShowError = bIgnoreShowError;
        }
    }

    public Boolean isbError() {
        synchronized (errorLock) {
            return bError;
        }
    }

    public void setbError(Boolean bError) {
        synchronized (errorLock) {
            this.bError = bError;
        }
    }

    public Boolean isbNotNet() {
        synchronized (notNetLock) {
            return bNotNet;
        }
    }

    public void setbNotNet(Boolean bNotNet) {
        synchronized (notNetLock) {
            this.bNotNet = bNotNet;
        }
    }

    public Boolean isbNotData() {
        synchronized (notDataLock) {
            return bNotData;
        }
    }

    public void setbNotData(Boolean bNotData) {
        synchronized (notDataLock) {
            this.bNotData = bNotData;
        }
    }

    public Boolean isbLoading() {
        synchronized (loadingLock) {
            return bLoading;
        }
    }

    public void setbLoading(Boolean bLoading) {
        synchronized (loadingLock) {
            this.bLoading = bLoading;
        }
    }

    public void setErrorViewDelegate(XIBaseErrorViewDelegate errorViewDelegate) {
        synchronized (errorLock) {
            this.errorViewDelegate = errorViewDelegate;
        }
    }

    public XIBaseErrorViewDelegate getErrorViewDelegate() {
        synchronized (errorLock) {
            return errorViewDelegate;
        }
    }

    public void setNoNetViewDelegate(XIBaseNoNetViewDelegate noNetViewDelegate) {
        synchronized (notNetLock) {
            this.noNetViewDelegate = noNetViewDelegate;
        }
    }

    public XIBaseNoNetViewDelegate getNoNetViewDelegate() {
        synchronized (notNetLock) {
            return noNetViewDelegate;
        }
    }

    public void setNoDataViewDelegate(XIBaseNoDataViewDelegate noDataViewDelegate) {
        synchronized (notDataLock) {
            this.noDataViewDelegate = noDataViewDelegate;
        }
    }

    public XIBaseNoDataViewDelegate getNoDataViewDelegate() {
        synchronized (notDataLock) {
            return noDataViewDelegate;
        }
    }

    public XIBaseLoadingViewDelegate getLoadingViewDelegate() {
        synchronized (loadingLock) {
            return loadingViewDelegate;
        }
    }

    public void setLoadingViewDelegate(XIBaseLoadingViewDelegate loadingViewDelegate) {
        synchronized (loadingLock) {
            this.loadingViewDelegate = loadingViewDelegate;
        }
    }

    public XBaseActivity getContext() {
        FragmentActivity fragmentActivity = getActivity();
        if(fragmentActivity instanceof XBaseActivity){
            return (XBaseActivity) getActivity();
        }else {
            return null;
        }
    }

    /**
     * 发送广播消息
     * @param action 广播消息类型
     * @param bundle 广播消息数据
     */
    public final void sendMyBroadcast(String action, Bundle bundle) {
        if (getActivity() instanceof XBaseActivity) {
            ((XBaseActivity) getActivity()).sendMyBroadcast(action, bundle);
        }
    }

    /**
     * 广播消息的处理
     * @param action 广播消息类型
     * @param bundle 广播消息数据
     */
    @CallSuper
    public void onMyBroadcastReceiver(String action, Bundle bundle) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment childFragment : fragments) {
                if (childFragment instanceof XBaseFragment) {
                    ((XBaseFragment) childFragment).onMyBroadcastReceiver(action, bundle);
                }
            }
        }

        if(action.equals(XConfig.Action.ACTION_NETWORK_CHANGE)){
            int netWorkState = bundle.getInt("network_state");
            if(netWorkState == XNetUtil.NETWORK_NONE){
                //无网
                setbNotNet(true);
                refreshView();
            }else if(netWorkState == XNetUtil.NETWORK_MOBILE){
                if(!isbIgnoreShowError() &&
                        isbNotNet()){
                    retryNoNet(false);
                }else {
                    setbNotNet(false);
                    refreshView();
                }
            }else if(netWorkState == XNetUtil.NETWORK_WIFI){
                if(!isbIgnoreShowError() &&
                        isbNotNet()){
                    retryNoNet(false);
                }else {
                    setbNotNet(false);
                    refreshView();
                }
            }
        }
    }

    /**
     * 内容区布局关系layout
     * @return 返回布局文件id，与业务相关
     */
    protected @LayoutRes abstract int getContentLayout();

    /**
     * 业务逻辑处理区域
     * @param saveInstanceState 页面保存状态
     * @param rootView 父视图
     */
    protected  abstract void onViewCreated(Bundle saveInstanceState, View rootView);

    /**
     * 加载页面
     */
    protected abstract void loadPage();

    /**
     * 加载加载过程中页面，需要定制
     * @return 访问加载过程中页
     */
    protected XIBaseLoadingViewDelegate loadLoadingView(){
        return getLoadingViewDelegate();
    }

    /**
     * 加载错误页，需要定制
     * @return 返回加载错误页
     */
    protected XIBaseErrorViewDelegate loadErrorView(){
        return getErrorViewDelegate();
    }

    /**
     * 无网错误页，需要定制
     * @return 返回无网错误页
     */
    protected XIBaseNoNetViewDelegate loadNotNetView(){
        return getNoNetViewDelegate();
    }

    /**
     * 无数据错误页，需要定制
     * @return 返回无数据错误页
     */
    protected XIBaseNoDataViewDelegate loadNotDataView(){
        return getNoDataViewDelegate();
    }

    /**
     * 状态初始化
     */
    private void initParam(){
        setbIgnoreShowError(false);
        setbNotNet(false);
        setbNotData(false);
        setbError(false);
        setbLoading(false);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xbase_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBaseRootView = (RelativeLayout) view.findViewById(R.id.xbase_root_frag_id);
        mContentView = (RelativeLayout) view.findViewById(R.id.xbase_content_id);
        mErrorContextView = (RelativeLayout) view.findViewById(R.id.xbase_error_content_id);
        mErrorContextView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);

        int contentLayoutId = getContentLayout();
        if(contentLayoutId > 0){
            mContentView.addView(View.inflate(getContext(),contentLayoutId,null),
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        setLoadingViewDelegate(loadLoadingView());
        if(getLoadingViewDelegate() != null){
            if(mErrorContextView != null){
                View loadingView = (View)getLoadingViewDelegate();
                if(loadingView != null){
                    mErrorContextView.addView(loadingView,
                            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    loadingView.setVisibility(View.GONE);
                }
            }
        }

        setErrorViewDelegate(loadErrorView());
        if(getErrorViewDelegate() != null){
            if(mErrorContextView != null){
                View errorView = (View)getErrorViewDelegate();
                if(errorView != null){
                    mErrorContextView.addView(errorView,
                            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    errorView.setVisibility(View.GONE);
                }
            }
        }

        setNoNetViewDelegate(loadNotNetView());
        if(getNoNetViewDelegate() != null){
            if(mErrorContextView != null){
                View noNetView = (View)getNoNetViewDelegate();
                if(noNetView != null){
                    mErrorContextView.addView(noNetView,
                            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    noNetView.setVisibility(View.GONE);
                }
            }
        }

        setNoDataViewDelegate(loadNotDataView());
        if(getNoDataViewDelegate() != null){
            if(mErrorContextView != null){
                View noDataView = (View)getNoDataViewDelegate();
                if(noDataView != null){
                    mErrorContextView.addView(noDataView,
                            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    noDataView.setVisibility(View.GONE);
                }
            }
        }

        if(getErrorViewDelegate() == null &&
                getNoNetViewDelegate() == null &&
                getNoDataViewDelegate() == null &&
                getLoadingViewDelegate() == null){
            if(mBaseRootView != null && mErrorContextView != null){
                mBaseRootView.removeView(mErrorContextView);
                mErrorContextView = null;
            }
        }

        onViewCreated(savedInstanceState,view);

        if(XNetUtil.getNetWorkState(getContext()) == XNetUtil.NETWORK_NONE){
            setbNotNet(true);
        }else {
            setbNotNet(false);
            loadPage();
        }
        refreshView();
    }


    /**
     * 显示内容区
     */
    private void showContentView(){
        //显示contentView
        if(mContentView != null){
            mContentView.setVisibility(View.VISIBLE);
        }

        if(mErrorContextView != null){
            mErrorContextView.setVisibility(View.GONE);
        }

        if(getLoadingViewDelegate() != null){
            getLoadingViewDelegate().visibleLoading(false);
        }

        //隐藏errorView
        if(getErrorViewDelegate() != null){
            getErrorViewDelegate().visibleErrorView(false);
        }

        //隐藏noNetView
        if(getNoNetViewDelegate() != null){
            getNoNetViewDelegate().visibleNoNet(false);
        }

        //隐藏noDataView
        if(getNoDataViewDelegate() != null){
            getNoDataViewDelegate().visibleNoData(false);
        }
    }

    /**
     * 显示加载过程中内容区
     */
    private void showLoadingView(){
        //隐藏contentView
        if(mContentView != null){
            mContentView.setVisibility(View.GONE);
        }

        if(mErrorContextView != null){
            mErrorContextView.setVisibility(View.VISIBLE);
        }

        if(getLoadingViewDelegate() != null){
            getLoadingViewDelegate().visibleLoading(true);
        }

        //隐藏errorView
        if(getErrorViewDelegate() != null){
            getErrorViewDelegate().visibleErrorView(false);
        }

        //隐藏noNetView
        if(getNoNetViewDelegate() != null){
            getNoNetViewDelegate().visibleNoNet(false);
        }

        //隐藏noDataView
        if(getNoDataViewDelegate() != null){
            getNoDataViewDelegate().visibleNoData(false);
        }
    }

    /**
     * 显示加载错误页
     */
    private void showErrorView(){
        if(mContentView != null){
            mContentView.setVisibility(View.GONE);
        }

        if(mErrorContextView != null){
            mErrorContextView.setVisibility(View.VISIBLE);
        }

        if(getLoadingViewDelegate() != null){
            getLoadingViewDelegate().visibleLoading(false);
        }

        if(getErrorViewDelegate() != null){
            getErrorViewDelegate().visibleErrorView(true);
        }

        if(getNoNetViewDelegate() != null){
            getNoNetViewDelegate().visibleNoNet(false);
        }

        if(getNoDataViewDelegate() != null){
            getNoDataViewDelegate().visibleNoData(false);
        }
    }

    /**
     * 显示无网错误页
     */
    private void showNoNetView(){
        if(mContentView != null){
            mContentView.setVisibility(View.GONE);
        }

        if(mErrorContextView != null){
            mErrorContextView.setVisibility(View.VISIBLE);
        }

        if(getLoadingViewDelegate() != null){
            getLoadingViewDelegate().visibleLoading(false);
        }

        if(getErrorViewDelegate() != null){
            getErrorViewDelegate().visibleErrorView(false);
        }

        if(getNoNetViewDelegate() != null){
            getNoNetViewDelegate().visibleNoNet(true);
        }

        if(getNoDataViewDelegate() != null){
            getNoDataViewDelegate().visibleNoData(false);
        }
    }

    /**
     * 显示无数据页
     */
    private void showNoDataView(){
        if(mContentView != null){
            mContentView.setVisibility(View.GONE);
        }

        if(mErrorContextView != null){
            mErrorContextView.setVisibility(View.VISIBLE);
        }

        if(getLoadingViewDelegate() != null){
            getLoadingViewDelegate().visibleLoading(false);
        }

        if(getErrorViewDelegate() != null){
            getErrorViewDelegate().visibleErrorView(false);
        }

        if(getNoNetViewDelegate() != null){
            getNoNetViewDelegate().visibleNoNet(false);
        }

        if(getNoDataViewDelegate() != null){
            getNoDataViewDelegate().visibleNoData(true);
        }
    }

    protected void refreshView(){
        synchronized (refreshUILock){
            if(isbIgnoreShowError()){
                //隐藏error_content_view
                showContentView();
                return;
            }

            if(isbNotNet()){
                //无网
                if(getNoNetViewDelegate() != null) {
                    showNoNetView();
                    return;
                }
            }

            if(isbError()){
                //存在错误
                if(getErrorViewDelegate() != null){
                    showErrorView();
                    return;
                }
            }

            if(isbNotData()){
                //无数据
                if(getNoDataViewDelegate() != null) {
                    showNoDataView();
                    return;
                }
            }

            if(isbLoading()){
                //加载过程中
                if(getLoadingViewDelegate() != null){
                    showLoadingView();
                    return;
                }
            }

            showContentView();
        }
    }


    /**
     * 即将开始准备请求
     * @param request   请求对象
     */
    @Override
    public void willStartRequest(XIBaseHttpRequestDelegate request){
        if(request != null){
            if(request instanceof XHttpRequest){
                XHttpRequest httpRequest = (XHttpRequest)request;
                requests.put(httpRequest.getId(),httpRequest);

                setbLoading(true);
                refreshView();
                if(getLoadingViewDelegate() != null){
                    getLoadingViewDelegate().startLoad();
                }
            }
        }
    }

    /**
     * 即将重试请求
     * @param oldRequest   旧的请求对象
     * @param newRequest   新的请求对象
     */
    @Override
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void willRetryRequest(XIBaseHttpRequestDelegate oldRequest, XIBaseHttpRequestDelegate newRequest){
        if(oldRequest != null && newRequest != null){
            if(oldRequest instanceof XHttpRequest &&
                    newRequest instanceof XHttpRequest){
                XHttpRequest oldHttpRequest = (XHttpRequest)oldRequest;
                if(requests.containsKey(oldHttpRequest.getId())){
                    XHttpRequest newHttpRequest = (XHttpRequest)newRequest;
                    requests.remove(oldHttpRequest.getId());
                    requests.put(newHttpRequest.getId(),newHttpRequest);
                }

                setbLoading(true);
                refreshView();
                if(getLoadingViewDelegate() != null){
                    getLoadingViewDelegate().startLoad();
                }
            }
        }
    }

    /**
     * 请求进度
     * @param request 请求对象
     * @param progress 请求进度量
     * @param totalProgress 请求总的进度量
     */
    @Override
    public void execRequest(XIBaseHttpRequestDelegate request, long progress, long totalProgress){
        if(request != null){
            if(request instanceof XHttpRequest){
                setbLoading(true);
                refreshView();
                if(getLoadingViewDelegate() != null){
                    getLoadingViewDelegate().loadProgress(progress,totalProgress);
                }
            }
        }
    }

    /**
     * 请求结果
     * @param request   请求对象
     * @param responseString 请求结果字符串
     * @param bError   是否请求错误
     */
    @Override
    public void completeDidRequest(XIBaseHttpRequestDelegate request, String responseString, Boolean bError){
        if(request != null){
            setbLoading(false);
            setbError(bError);
            refreshView();
            if(getLoadingViewDelegate() != null){
                getLoadingViewDelegate().completeLoad(!bError);
            }

            XHttpRequest httpRequest = (XHttpRequest)request;
            if(httpRequest != null){
                httpRequest.cancel();
//                if(requests.containsKey(httpRequest.getId())){
//                    requests.remove(httpRequest.getId());
//                }
            }
        }
    }

    /**
     * 取消请求，可能是系统行为，也可能是用户主动行为
     * @param request 待取消的请求对象
     */
    @Override
    public void cancelRequest(XIBaseHttpRequestDelegate request){
        if(request != null){
            XHttpRequest httpRequest = (XHttpRequest)request;
            if(httpRequest != null){
                requests.remove(httpRequest.getId());
            }
        }
    }

    @Override
    public void retryControl() {
        initParam();
        loadPage();
    }

    @Override
    public void retryNoData() {
        initParam();
        loadPage();
    }

    @Override
    public void retryNoNet(Boolean bSetNet) {
        if(bSetNet) {
            //应该是跳转到设置
            Intent intent = null;
            if (Build.VERSION.SDK_INT > 10) {
                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            } else {
                intent = new Intent();
                ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            getActivity().startActivityForResult(intent, 0);
        }else {
            initParam();
            loadPage();
        }
    }
}
