package com.xframework_uicommon.xview.xloadingview;

import com.xframework_uicommon.xview.xerrorview.XIBaseRetryDelegate;

import java.lang.ref.WeakReference;

/**
 * 加载过程中代理
 */
public interface XIBaseLoadingViewDelegate {
    /**
     * 重试响应者
     */
    WeakReference<XIBaseRetryDelegate> delegate = null;

    /**
     * 初始化页面
     */
    void initView();

    /**
     * 显示或隐藏加载进度框
     * @param bVisible true显示 否则隐藏
     */
    void visibleLoading(Boolean bVisible);

    /**
     * 开始准备加载
     */
    void startLoad();

    /**
     * 加载进度
     * @param progress 已加载的进度
     * @param totalProgress 总进度
     */
    void loadProgress(long progress, long totalProgress);

    /**
     * 加载结果
     * @param bSuccess true成功 否则失败
     */
    void completeLoad(boolean bSuccess);
}
