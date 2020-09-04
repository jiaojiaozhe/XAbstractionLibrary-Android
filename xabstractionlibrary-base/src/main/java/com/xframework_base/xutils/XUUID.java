package com.xframework_base.xutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * 获取随机码相关，包括设备唯一标识
 */
public class XUUID {

    private static XUUID instance = null;
    private Context mContext = null;
    private String mDeviceId = null;
    private final static String DEFAULT_NAME = "system_device_id";
    private final String DEFAULT_FILE_NAME = "system_device_id";
    private final String DEFAULT_DEVICE_ID = "device_id";
    private final String ANDROID_FILE = Environment.getExternalStoragePublicDirectory("Android") + File.separator + DEFAULT_FILE_NAME;
    private final String DCIM_FILE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + DEFAULT_FILE_NAME;
    private static SharedPreferences preferences = null;

    public static XUUID getInstance(){
        if(instance == null){
            synchronized (XUUID.class){
                if(instance == null){
                    instance = new XUUID();
                }
            }
        }
        return instance;
    }

    public String getmDeviceId() {
        return mDeviceId;
    }

    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    /**
     * 启动的时候需要在App的初始化中调用
     * @param newContext 当前App的context
     */
    public void register(Context newContext){
        mContext = newContext;
        initDeviceId();
    }

    /**
     * 尝试获取设备唯一标识，不存在就创建
     * @return 返回唯一标示
     */
    private void initDeviceId(){
        if(preferences == null){
            preferences = mContext.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        }

        String deviceId = preferences.getString(DEFAULT_DEVICE_ID,null);
        if(TextUtils.isEmpty(deviceId)){

            Boolean bAndroid = TextUtils.isEmpty(checkAndroidFile());
            Boolean bDCIM = TextUtils.isEmpty(checkDCIMFile());
            if(!bAndroid && !bDCIM){
                deviceId = getPhoneSign();
                saveAndroidFile(deviceId);
                saveDCIMFile(deviceId);
            }else {
                if(bAndroid){
                    deviceId = checkAndroidFile();
                    saveDCIMFile(deviceId);
                }else if(bDCIM){
                    deviceId = checkDCIMFile();
                    saveAndroidFile(deviceId);
                }
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(DEFAULT_DEVICE_ID,deviceId);
            editor.commit();
        }else {
            //SP存在，但本地持久化不存在
            if(TextUtils.isEmpty(checkAndroidFile())){
                saveAndroidFile(deviceId);
            }

            if(TextUtils.isEmpty(checkDCIMFile())){
                saveDCIMFile(deviceId);
            }
        }

        setmDeviceId(deviceId);
    }

    private String checkAndroidFile(){
        BufferedReader reader = null;
        String content = null;
        File file = new File(ANDROID_FILE);

        if(file != null){
            try {
                reader = new BufferedReader(new FileReader(file));
                if(reader != null){
                    content = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    return content;
                }
            }
        }
        return content;
    }

    private void saveAndroidFile(String data){
        if(data == null)
            data = "";

        FileWriter fileWriter = null;
        File file = new File(ANDROID_FILE);
        if(file != null){
            try {
                fileWriter = new FileWriter(file);
                if(fileWriter != null){
                    fileWriter.write(data);
                    fileWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String checkDCIMFile(){
        BufferedReader reader = null;
        String content = null;
        File file = new File(DCIM_FILE);
        if(file != null){
            try {
                reader = new BufferedReader(new FileReader(file));
                try {
                    content = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            return content;
                        }
                    }
                    return content;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                return content;
            }
        }
        return content;
    }

    private void saveDCIMFile(String data){

        if(data == null)
            data = "";

        File file = new File(DCIM_FILE);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(data);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileWriter != null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 获取本地唯一标识码
     * @return 返回本机唯一标识码
     */
    private String getPhoneSign(){
        String deviceId = null;
        StringBuilder device = new StringBuilder();

        try {
            device.append("");
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if(!TextUtils.isEmpty(imei)){
                device.append("imei");
                device.append(imei);
            }

            if(TextUtils.isEmpty(device)){
               String sn = telephonyManager.getSimSerialNumber();
               if(!TextUtils.isEmpty(sn)){
                   device.append("sn");
                   device.append(sn);
               }
            }

            if(TextUtils.isEmpty(device)){
                String uuid = UUID.randomUUID().toString();
                if(!TextUtils.isEmpty(uuid)){
                    device.append("id");
                    device.append(uuid);
                }
            }

        }catch (SecurityException e){
            e.printStackTrace();
        }finally {
            if(TextUtils.isEmpty(device)){
                String uuid = UUID.randomUUID().toString();
                if(!TextUtils.isEmpty(uuid)){
                    device.append("id");
                    device.append(uuid);
                }
            }
        }

        device.insert(0,"android");
        deviceId = device.toString();
        return XMD5.md5s(deviceId);
    }

    /**
     * 得到全局唯一UUID
     * @return 返回全局唯一UUID
     */
    public String getUUID(){
        return XMD5.md5s(UUID.randomUUID().toString());
    }
}
