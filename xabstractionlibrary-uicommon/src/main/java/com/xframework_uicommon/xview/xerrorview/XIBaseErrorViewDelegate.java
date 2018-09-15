package com.xframework_uicommon.xview.xerrorview;

import java.lang.ref.WeakReference;

/**
 * 请求状态回调接口
 */
public interface XIBaseErrorViewDelegate {

    /**
     * 重试响应者
     */
    WeakReference<XIBaseRetryDelegate> delegate = null;

    /**
     * 初始化页面
     */
    void initView();

    /**
     * 显示错误页
     * @param bError true错误 否则不存在错误
     */
    void visibleErrorView(Boolean bError);
}
