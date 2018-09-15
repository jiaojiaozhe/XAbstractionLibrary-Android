package com.xframework_base.xconfig;

import com.xframework_base.xmodel.XBaseModel;

/**
 * 基础配置对象
 */
public class XConfig extends XBaseModel {

    public static class Action{

        /**
         * 网络变化action
         */
        public static String ACTION_NETWORK_CHANGE = "NETWORK_CHANGE";
    }

    /**
     * 基础广播
     */
    public final static String ACTION_BASE_BROADCAST = "ACTION_BASE_BROADCAST";
}
