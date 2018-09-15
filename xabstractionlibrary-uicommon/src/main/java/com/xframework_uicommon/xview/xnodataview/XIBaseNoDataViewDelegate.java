package com.xframework_uicommon.xview.xnodataview;

import java.lang.ref.WeakReference;

/**
 * 无数据状态接口
 */
public interface XIBaseNoDataViewDelegate {
    /**
     * 无数据重试代理
     */
    WeakReference<XIBaseNoDataRetryDelegate> delegate = null;

    /**
     * 初始化页面
     */
    void initView();

    /**
     * 显示无数据页
     * @param bNoData true无数据 否则有数据
     */
    void visibleNoData(Boolean bNoData);
}
