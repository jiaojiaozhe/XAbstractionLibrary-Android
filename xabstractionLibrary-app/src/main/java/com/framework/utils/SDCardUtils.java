package com.framework.utils;

import android.os.Environment;

import com.App;

import java.io.File;


/**
 * Created by lanbiao on 2018/5/28
 * 用于设置App的数据存储目录
 */
public class SDCardUtils {
    public final static String SDCARD_PATH = getDataPath();

    /**
     * 现在的Android应用将文件放到SD卡上时总是随便创建一个目录，那这样有个问题就是卸载应用时，
     * 这些垃圾还留在用户的SD卡上导致占用存储空间（猎豹清理大师这样的工具由此应用而生）。
     * 其实Android系统已经帮我们提供了相关的API可以将文件缓存到data/data目录下，
     * 当APP卸载时，这些垃圾文件也跟着自动卸载清除了。
     */
    private static String getDataPath() {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {

            cachePath = App.getApplication().getExternalCacheDir().getAbsolutePath();
        } else {
            cachePath = App.getApplication().getFilesDir().getAbsolutePath();
        }

        return cachePath + File.separator;
    }
}
