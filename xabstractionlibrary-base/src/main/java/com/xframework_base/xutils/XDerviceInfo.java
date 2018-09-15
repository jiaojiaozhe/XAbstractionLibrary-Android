package com.xframework_base.xutils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.xframework_base.XBaseApp;

/*设备信息*/
public class XDerviceInfo {
    /**
     * 获取IMEI
     *
     * @return
     */
    public String getIMEI() {
        Application context = XBaseApp.getApplication();
        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "无读取手机权限";
        }
        return tManager.getDeviceId();
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取工业设计名称
     *
     * @return
     */
    public static String getIndustryDesign() {
        return android.os.Build.DEVICE;
    }

    /**
     * 获取手机产品名称
     *
     * @return
     */
    public static String getBuildProduct() {
        return android.os.Build.PRODUCT;
    }

    /**
     * 获取手机制造商
     *
     * @return
     */
    public static String getbuildManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取android系统版本号
     */
    public static String getAndroidVerCode(Context context) {
        String androidVerCode = "";
        try {
            androidVerCode = android.os.Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return androidVerCode;
    }
}
