package com.xframework_uicommon.xview.xnonetview;

import java.lang.ref.WeakReference;

/**
 * 无网状态接口
 */
public interface XIBaseNoNetViewDelegate {

    /**
     * 设置网络的回调代理
     */
    WeakReference<XIBaseNoNetRetryDelegate> delegate = null;

    /**
     * 无网，显示或隐藏部分相关元素
     * @param bNoNet true无网 否则有网
     */
    public void visibleNoNet(boolean bNoNet);

    /**
     * 初始化页面
     */
    void initView();


}
