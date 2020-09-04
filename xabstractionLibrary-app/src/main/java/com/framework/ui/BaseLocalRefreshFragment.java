package com.framework.ui;

import android.view.View;

import com.framework.ui.errorview.BaseErrorView;
import com.framework.ui.loadingview.BaseLoadingView;
import com.framework.ui.nodataview.BaseNoDataView;
import com.framework.ui.nonetview.BaseNoNetView;
import com.xframework_uicommon.xview.XBaseRefreshFragment;
import com.xframework_uicommon.xview.xerrorview.XIBaseErrorViewDelegate;
import com.xframework_uicommon.xview.xloadingview.XIBaseLoadingViewDelegate;
import com.xframework_uicommon.xview.xnodataview.XIBaseNoDataViewDelegate;
import com.xframework_uicommon.xview.xnonetview.XIBaseNoNetViewDelegate;
import com.personal.xabstractionlibrary.R;

import java.util.ArrayList;

import cn.appsdream.nestrefresh.base.AbsRefreshLayout;

public abstract class BaseLocalRefreshFragment<M> extends XBaseRefreshFragment {

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

    protected void loadPage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<M> dataSource = getFirstPageData();
                BaseLocalRefreshFragment.this.getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshControl(dataSource,false);
                    }
                });
            }
        }).start();
    }

    protected void loadMore(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<M> dataSource = getNextPageData();

                BaseLocalRefreshFragment.this.getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreControl(dataSource,false);
                    }
                });
            }
        }).start();
    }

    @Override
    public AbsRefreshLayout.LoaderDecor refreshHeaderView() {
        return super.refreshHeaderView();
    }

    @Override
    public AbsRefreshLayout.LoaderDecor loadMoreView() {
        return super.loadMoreView();
    }

    protected abstract ArrayList<M> getFirstPageData();

    protected abstract ArrayList<M> getNextPageData();
}
