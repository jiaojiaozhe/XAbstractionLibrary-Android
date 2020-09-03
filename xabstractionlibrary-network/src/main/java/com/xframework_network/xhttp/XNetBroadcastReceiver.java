package com.xframework_network.xhttp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.xframework_base.xconfig.XConfig;


public class XNetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            int netWorkState = XNetUtil.getNetWorkState(context);

            Intent netIntent = new Intent(XConfig.ACTION_BASE_BROADCAST);
            Bundle bundle = new Bundle();
            bundle.putInt("network_state",netWorkState);
            netIntent.putExtra("action", XConfig.Action.ACTION_NETWORK_CHANGE);
            netIntent.putExtra("bundle", bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(netIntent);
        }
    }
}
