package com.xframework_uicommon.xview;


import cn.appsdream.nestrefresh.base.AbsRefreshLayout;
import cn.appsdream.nestrefresh.base.OnPullListener;

/**
 * 带网络请求的上下拉滑动抽象fragment
 * @param <M> 滑动每个item的类型
 */
public abstract class XBaseNetRefreshFragment<M> extends XBaseRefreshFragment<M> {

    /**
     * 更多业务请求
     */
    abstract protected void loadMore();

    @Override
    public OnPullListener getRefreshEventListener() {
        return new OnPullListener() {
            @Override
            public void onRefresh(AbsRefreshLayout listLoader) {
                loadPage();
            }

            @Override
            public void onLoading(AbsRefreshLayout listLoader) {
                loadMore();
            }
        };
    }
}
