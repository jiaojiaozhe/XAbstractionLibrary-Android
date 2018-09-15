package com.xframework_uicommon.xview.xerrorview;

/**
 * Created by lanbiao on 2018/06/10
 * 失败重试事件
 */
public interface XIBaseRetryDelegate {

    /**
     * 请求错误重试
     */
    void retryControl();
}
