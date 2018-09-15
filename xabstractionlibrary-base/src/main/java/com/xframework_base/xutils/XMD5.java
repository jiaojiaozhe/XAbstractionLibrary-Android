package com.xframework_base.xutils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class XMD5 {
    /**
     * 对字符串类型返回md5计算后字符串
     * @param plainText 需要进行md5处理的字符串
     * @return 返回处理后的md5串
     */
    public static String md5s(String plainText) {
        if(TextUtils.isEmpty(plainText)){
            return "";
        }

        String str;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        return str;
    }
}
