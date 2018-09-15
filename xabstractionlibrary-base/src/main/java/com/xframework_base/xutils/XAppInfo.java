package com.xframework_base.xutils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.xframework_base.XBaseApp;

/*App信息*/
public class XAppInfo {
    /**
     * 获取应用版本name
     *
     * @return
     */
    public static String getAppVersion() {

        Application context = XBaseApp.getApplication();
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            String version = packInfo.versionName;
            // int code = packInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取应用版本code
     *
     * @return
     */
    public static int getAppVersionCode() {

        Application context = XBaseApp.getApplication();
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            // String version = packInfo.versionName;
            int code = packInfo.versionCode;
            return code;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }

    }
}
