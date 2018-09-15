package com.xframework_network.xhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xframework_base.xmodel.XBaseModel;

/**
 * 网络状态工具类
 */
public class XNetUtil extends XBaseModel {

    /**
     * 未连接状态
     */
    public static final int NETWORK_NONE = -1;

    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;

    /**
     * wifi网络
     */
    public static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return NETWORK_WIFI;
            }else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                return NETWORK_MOBILE;
            }
        }else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }
}
