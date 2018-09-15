package com.xframework_network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.xframework_base.XBaseApp;
import com.xframework_network.xhttp.XNetBroadcastReceiver;

/**
 * Created by lanbiao on 2018/07/09
 * 网络模块的初始化
 */
public class XNetWorkApp extends XBaseApp {
    @Override
    public void initApp(Context context) {
        super.initApp(context);
        //ToDo:网络模块的初始化
        if(context != null){
            context.registerReceiver(new XNetBroadcastReceiver(),new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
}
