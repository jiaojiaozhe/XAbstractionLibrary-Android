package com.xframework_uicommon.xview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.xframework_base.xconfig.XConfig;
import com.xframework_base.xmodel.XBaseModel;
import com.xframework_network.xhttp.XHttpRequest;
import com.xframework_network.xhttp.XIBaseHttpRequestDelegate;
import com.xframework_network.xhttp.XIBaseHttpResponseDelegate;
import com.xframework_uicommon.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  created by lanbiao on 2018/06/19
 *  抽象的基础Activity
 */
public abstract class XBaseActivity extends AppCompatActivity implements XIBaseHttpResponseDelegate {

    /**
     * 基础背景布局
     */
    private RelativeLayout mBaseRootView = null;

    /**
     * 内容填充区基础部分
     */
    private RelativeLayout mContentView = null;

    /**
     * 内容区，包含所有的错误啥的，需要实现层实现
     */
    private XBaseFragment mContentFragemnt = null;

    /**
     * 主题相关的fragment，需要实现层实现
     */
    private Fragment mThemeFragment = null;

    /**
     * 请求request集合
     */
    private Map<String,XIBaseHttpRequestDelegate> requests = new HashMap<>();

    /**
     * 请求集合访问锁
     */
    private Serializable requestLock = new XBaseModel() {
    };

    /**
     * 内容区的fragment
     * @return 返回显示的内容区，由实现层实现
     */
    public abstract XBaseFragment loadContentFragment();

    /**
     * 主题区的fragment，由实现层负责加载
     * @return 返回主题布局
     */
    public abstract Fragment loadThemeFragment();

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(XConfig.ACTION_BASE_BROADCAST)) {
                String myAction = intent.getStringExtra("action");
                onMyBroadcastReceiver(myAction, intent.getBundleExtra("bundle"));
            }
        }
    };

    @CallSuper
    public void onMyBroadcastReceiver(String action, Bundle bundle) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof XBaseFragment) {
                    ((XBaseFragment) fragment).onMyBroadcastReceiver(action, bundle);
                }
            }
        }
    }

    /**
     * 发送广播消息
     * @param action 消息名
     * @param bundle 消息数据
     */
    public final void sendMyBroadcast(String action, Bundle bundle) {
        Intent intent = new Intent(XConfig.ACTION_BASE_BROADCAST);
        intent.putExtra("action", action);
        intent.putExtra("bundle", bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * 注册广播
     */
    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(XConfig.ACTION_BASE_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    /**
     * 反注册广播
     */
    private void unregisterBroadcastReceiver(){
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.xbase_activity_layout);
        registerBroadcastReceiver();
        mBaseRootView = (RelativeLayout) findViewById(R.id.xbase_root);
        mContentView = (RelativeLayout) findViewById(R.id.xcontent_view);
        mThemeFragment = loadThemeFragment();
        mContentFragemnt = loadContentFragment();

        if(mContentFragemnt != null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.xnavigation_title_id,mThemeFragment,"");
            fragmentTransaction.add(R.id.xcontent_view,mContentFragemnt,"");
            fragmentTransaction.show(mThemeFragment);
            fragmentTransaction.show(mContentFragemnt);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }

    @Override
    public void willStartRequest(XIBaseHttpRequestDelegate request) {
        synchronized (requestLock) {
            if (mContentFragemnt != null) {
                mContentFragemnt.willStartRequest(request);
            }

            if(request != null){
                if(request instanceof XHttpRequest){
                    XHttpRequest httpRequest = (XHttpRequest)request;
                    requests.put(httpRequest.getId(),request);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void willRetryRequest(XIBaseHttpRequestDelegate oldRequest, XIBaseHttpRequestDelegate newRequest) {
        synchronized (requestLock) {
            if (mContentFragemnt != null) {
                mContentFragemnt.willRetryRequest(oldRequest, newRequest);
            }

            if(oldRequest != null && newRequest != null){
                if(oldRequest instanceof XHttpRequest &&
                        newRequest instanceof XHttpRequest){
                    XHttpRequest oldHttpRequest = (XHttpRequest)oldRequest;
                    XHttpRequest newHttpRequest = (XHttpRequest)newRequest;
                    if(requests.containsKey(oldHttpRequest.getId())){
                        //requests.replace("oldRequestId",oldHttpRequest,newRequest);
                        requests.remove(oldHttpRequest.getId());
                        requests.put(newHttpRequest.getId(),newHttpRequest);
                    }
                }
            }
        }
    }

    @Override
    public void execRequest(XIBaseHttpRequestDelegate request, long progress, long totalProgress) {
        synchronized (requestLock) {
            if (mContentFragemnt != null) {
                mContentFragemnt.execRequest(request, progress, totalProgress);
            }

            if(request != null){
                if(request instanceof XHttpRequest){
                    //没事做,也可以有
                }
            }
        }
    }

    @Override
    public void completeDidRequest(XIBaseHttpRequestDelegate request, String responseString, Boolean bError) {
        synchronized (requestLock) {
            if (mContentFragemnt != null) {
                mContentFragemnt.completeDidRequest(request, responseString, bError);
            }

            if(request != null){
                if(request instanceof XHttpRequest){
                    XHttpRequest httpRequest = (XHttpRequest) request;
                    if(requests.containsKey(httpRequest.getId())){
                        requests.remove(httpRequest.getId());
                    }
                }
            }
        }
    }

    @Override
    public void cancelRequest(XIBaseHttpRequestDelegate request) {
        synchronized (requestLock) {
            if (mContentFragemnt != null) {
                mContentFragemnt.cancelRequest(request);
            }

            if(request != null){
                if(request instanceof XHttpRequest){
                    XHttpRequest httpRequest = (XHttpRequest) request;
                    requests.remove(httpRequest.getId());
                }
            }
        }
    }
}
