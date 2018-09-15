package com.xframework_parse.xparse;

import com.xframework_parse.xparse.xdeserializer.XBaseDefaultData;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * created by lanbiao on 2018/06/08
 * 类型处理器,数据兼容
 */
public class XTypeUtis {

    /**
     * 判断是否是整数（int）
     * @param str 待判断的字符串
     * @return true整数 否则非整数
     */
    private static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否是浮点数（double和float）
     * @param str 带判断的字符串
     * @return true浮点数 否则非浮点数
     */
    private static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 转换未知类型到单精度浮点型
     * @param value 未知数据类型
     * @return 返回转换后的单精度浮点型
     */
    public static Float castToFloat(Object value) {

        Float floatObj = null;

        if(value == null){
            floatObj = XBaseDefaultData.defaultFloatData;
        }else if(value instanceof Long){
            if((Long)value > Float.MAX_VALUE || (Long)value < Float.MIN_VALUE){
                floatObj = XBaseDefaultData.defaultFloatData;
            }else {
                floatObj = (Float)value;
            }
        }else if(value instanceof Integer){
            if((Integer)value > Float.MAX_VALUE || (Integer)value < Float.MIN_VALUE){
                floatObj = XBaseDefaultData.defaultFloatData;
            }else {
                floatObj = (Float)value;
            }
        }else if(value instanceof Short){
            if((Short)value > Float.MAX_VALUE || (Short)value < Float.MIN_VALUE){
                floatObj = XBaseDefaultData.defaultFloatData;
            }else {
                floatObj = (Float)value;
            }
        }else if(value instanceof Byte){
            if((Byte)value > Float.MAX_VALUE || (Byte)value < Float.MIN_VALUE){
                floatObj = XBaseDefaultData.defaultFloatData;
            }else {
                floatObj = (Float)value;
            }
        }else if(value instanceof Double){
            if((Double)value > Float.MAX_VALUE || (Double)value < Float.MIN_VALUE){
                floatObj = XBaseDefaultData.defaultFloatData;
            }else {
                floatObj = (Float)value;
            }
        }else if(value instanceof Float){
            if((Float)value > Float.MAX_VALUE || (Float)value < Float.MIN_VALUE){
                floatObj = XBaseDefaultData.defaultFloatData;
            }else {
                floatObj = (Float)value;
            }
        } else if(value instanceof Number){
            floatObj = ((Number) value).floatValue();
        }else if(value instanceof String){
            String strVal = value.toString();
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                floatObj = XBaseDefaultData.defaultFloatData;
            }else if(strVal.indexOf(',') != 0){
                strVal = strVal.replaceAll(",", "");
            }

            if(floatObj == null){
                if(isInteger(strVal)){
                    Long longValue = Long.parseLong(strVal);
                    if(longValue > Float.MAX_VALUE || longValue < Float.MIN_VALUE){
                        floatObj = XBaseDefaultData.defaultFloatData;
                    }else {
                        floatObj = Float.parseFloat(strVal);
                    }
                }else if(isDouble(strVal)){
                    Double doubleValue = Double.parseDouble(strVal);
                    if(doubleValue > Float.MAX_VALUE || doubleValue < Float.MIN_VALUE){
                        floatObj = XBaseDefaultData.defaultFloatData;
                    }else {
                        floatObj = Float.parseFloat(strVal);
                    }
                }else {
                    floatObj = XBaseDefaultData.defaultFloatData;
                }
            }
        }else if(value instanceof Boolean){
            floatObj = ((Boolean) value).booleanValue() ? 1.f : 0f;
        }else if(value instanceof Map){
            floatObj = XBaseDefaultData.defaultFloatData;
        }else {
            floatObj = XBaseDefaultData.defaultFloatData;
        }

        return floatObj;
    }

    /**
     * 转换未知类型到双精度浮点型
     * @param value 未知数据类型数据
     * @return 返回转换后的双精度浮点型数据
     */
    public static Double castToDouble(Object value){

        Double doubleObj = null;

        if(value == null){
            doubleObj = XBaseDefaultData.defaultDoubleData;
        }else if(value instanceof Long){
            if((Long)value > Double.MAX_VALUE || (Long)value < Double.MIN_VALUE){
                doubleObj = XBaseDefaultData.defaultDoubleData;
            }else {
                doubleObj = (Double)value;
            }
        }else if(value instanceof Integer){
            if((Integer)value > Double.MAX_VALUE || (Integer)value < Double.MIN_VALUE){
                doubleObj = XBaseDefaultData.defaultDoubleData;
            }else {
                doubleObj = (Double)value;
            }
        }else if(value instanceof Short){
            if((Short)value > Double.MAX_VALUE || (Short)value < Double.MIN_VALUE){
                doubleObj = XBaseDefaultData.defaultDoubleData;
            }else {
                doubleObj = (Double)value;
            }
        }else if(value instanceof Byte){
            if((Byte)value > Double.MAX_VALUE || (Byte)value < Double.MIN_VALUE){
                doubleObj = XBaseDefaultData.defaultDoubleData;
            }else {
                doubleObj = (Double)value;
            }
        }else if(value instanceof Double){
            doubleObj = (Double)value;
        }else if(value instanceof Float){
            doubleObj = (Double)value;
        } else if(value instanceof Number){
            doubleObj = ((Number) value).doubleValue();
        }else if(value instanceof String){
            String strVal = value.toString();
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                doubleObj = XBaseDefaultData.defaultDoubleData;
            }else if(strVal.indexOf(',') != 0){
                strVal = strVal.replaceAll(",", "");
            }

            if(doubleObj == null){

                if(isInteger(strVal)){
                    Long longValue = Long.parseLong(strVal);
                    if(longValue > Double.MAX_VALUE || longValue < Double.MIN_VALUE){
                        doubleObj = XBaseDefaultData.defaultDoubleData;
                    }else {
                        doubleObj = Double.parseDouble(strVal);
                    }
                }else if(isDouble(strVal)) {
                    doubleObj = Double.parseDouble(strVal);
                }else {
                    doubleObj = XBaseDefaultData.defaultDoubleData;
                }
            }
        }else if(value instanceof Boolean){
            doubleObj = ((Boolean) value).booleanValue() ? 1. : 0.;
        }else if(value instanceof Map){
            doubleObj = XBaseDefaultData.defaultDoubleData;
        }else {
            doubleObj = XBaseDefaultData.defaultDoubleData;
        }

        return doubleObj;
    }


    /**
     * 转换未知类型到字节型
     * @param value 待转换的数据类型
     * @return 返回转换后的数据类型
     */
    public static Byte castToByte(Object value){

        Byte byteObject = null;
        if(value == null){
            byteObject = XBaseDefaultData.defaultByteData;
        }else if(value instanceof Long){
            if((Long)value > Byte.MAX_VALUE || (Long)value < Byte.MIN_VALUE){
                byteObject = XBaseDefaultData.defaultByteData;
            }else {
                byteObject = (Byte) value;
            }
        }else if(value instanceof Integer){
            if((Integer)value > Byte.MAX_VALUE || (Integer)value < Byte.MIN_VALUE){
                byteObject = XBaseDefaultData.defaultByteData;
            }else {
                byteObject = (Byte) value;
            }
        }else if(value instanceof Short) {
            if((Short)value > Byte.MAX_VALUE || (Short)value < Byte.MIN_VALUE){
                byteObject = XBaseDefaultData.defaultByteData;
            }else {
                byteObject = (Byte) value;
            }
        } else if(value instanceof Byte){
            byteObject = (Byte) value;
        }else if(value instanceof Double){
            if((Double)value > Byte.MAX_VALUE || (Double)value < Byte.MIN_VALUE){
                byteObject = XBaseDefaultData.defaultByteData;
            }else {
                //这个地方的强制类型转换，会舍弃掉小数点或四舍五入
                byteObject = (Byte) value;
            }
        }else if(value instanceof Float){
            if((Float)value > Byte.MAX_VALUE || (Float)value < Byte.MIN_VALUE){
                byteObject = XBaseDefaultData.defaultByteData;
            }else {
                //这个地方的强制类型转换，会舍弃掉小数点或四舍五入
                byteObject = (Byte) value;
            }
        }else if(value instanceof Number){
            byteObject = ((Number) value).byteValue();
        }else if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                byteObject = XBaseDefaultData.defaultByteData;
            }else if(strVal.indexOf(',') != 0){
                strVal = strVal.replaceAll(",", "");
            }

            if(byteObject == null){
                if(isInteger(strVal)){
                    Long longValue = Long.parseLong(strVal);
                    if(longValue > Byte.MAX_VALUE || longValue < Byte.MIN_VALUE){
                        byteObject = XBaseDefaultData.defaultByteData;
                    }else {
                        byteObject = longValue.byteValue();
                    }
                }else if(isDouble(strVal)){
                    Double doubleValue = Double.parseDouble(strVal);
                    if(doubleValue > Byte.MAX_VALUE || doubleValue < Byte.MIN_VALUE){
                        byteObject = XBaseDefaultData.defaultByteData;
                    }else {
                        //这个地方的强制类型转换，会舍弃掉小数点或四舍五入
                        byteObject = doubleValue.byteValue();
                    }
                }else {
                    byteObject = XBaseDefaultData.defaultByteData;
                }
            }
        }else if(value instanceof Boolean){
            if(((Boolean) value).booleanValue()){
                byteObject = 1;
            }else {
                byteObject = 0;
            }
        }else if(value instanceof Map){
            byteObject = XBaseDefaultData.defaultByteData;
        }else {
            byteObject = XBaseDefaultData.defaultByteData;
        }

        return byteObject;
    }



    /**
     * 转换未知类型到短整型
     * @param value 待转换的数据类型
     * @return 返回转换后的数据类型
     */
    public static Short castToShort(Object value){

        Short shortObject = null;
        if(value == null){
            shortObject = XBaseDefaultData.defaultShortData;
        }else if(value instanceof Long){
            if((Long)value > Short.MAX_VALUE || (Long)value < Short.MIN_VALUE){
                shortObject = XBaseDefaultData.defaultShortData;
            }else {
                shortObject = (Short) value;
            }
        }else if(value instanceof Integer){
            if((Integer)value > Short.MAX_VALUE || (Integer)value < Short.MIN_VALUE){
                shortObject = XBaseDefaultData.defaultShortData;
            }else {
                shortObject = (Short) value;
            }
        }else if(value instanceof Short) {
            shortObject = (Short) value;
        } else if(value instanceof Byte){
            shortObject = (Short) value;
        }else if(value instanceof Double){
            if((Double)value > Short.MAX_VALUE || (Double)value < Short.MIN_VALUE){
                shortObject = XBaseDefaultData.defaultShortData;
            }else {
                //这个地方的强制类型转换，会舍弃掉小数点或四舍五入
                shortObject = (Short)value;
            }
        }else if(value instanceof Float){
            if((Float)value > Short.MAX_VALUE || (Float)value < Short.MIN_VALUE){
                shortObject = XBaseDefaultData.defaultShortData;
            }else {
                //这个地方的强制类型转换，会舍弃掉小数点或四舍五入
                shortObject = (Short)value;
            }
        }else if(value instanceof Number){
            shortObject = ((Number) value).shortValue();
        }else if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                shortObject = XBaseDefaultData.defaultShortData;
            }else if(strVal.indexOf(',') != 0){
                strVal = strVal.replaceAll(",", "");
            }

            if(shortObject == null){
                if(isInteger(strVal)){
                    Long longValue = Long.parseLong(strVal);
                    if(longValue > Short.MAX_VALUE || longValue < Short.MIN_VALUE){
                        shortObject = XBaseDefaultData.defaultShortData;
                    }else {
                        shortObject = longValue.shortValue();
                    }
                }else if(isDouble(strVal)){
                    Double doubleValue = Double.parseDouble(strVal);
                    if(doubleValue > Short.MAX_VALUE || doubleValue < Short.MIN_VALUE){
                        shortObject = XBaseDefaultData.defaultShortData;
                    }else {
                        //这个地方的强制类型转换，会舍弃掉小数点或四舍五入
                        shortObject = doubleValue.shortValue();
                    }
                }else {
                    shortObject = XBaseDefaultData.defaultShortData;
                }
            }
        }else if(value instanceof Boolean){
            if(((Boolean) value).booleanValue()){
                shortObject = 1;
            }else {
                shortObject = 0;
            }
        }else if(value instanceof Map){
            shortObject = XBaseDefaultData.defaultShortData;
        }else {
            shortObject = XBaseDefaultData.defaultShortData;
        }

        return shortObject;
    }

    /**
     * 转换未知类型到整形
     * @param value 未知数据类型
     * @return 返回整形
     */
    public static Integer castToInteger(Object value){
        Integer intObj = null;
        if(value == null){
            intObj = XBaseDefaultData.defaultIntegerData;
        }else if(value instanceof Long){
            if((Long)value > Integer.MAX_VALUE || (Long)value < Integer.MIN_VALUE){
                intObj = XBaseDefaultData.defaultIntegerData;
            }else {
                intObj = (Integer)value;
            }
        } else if(value instanceof Integer){
            intObj = (Integer) value;
        }
        else if(value instanceof Short){
            intObj = (Integer) value;
        }else if(value instanceof Byte){
            intObj = (Integer) value;
        }else if(value instanceof Double){
            if((Double)value > Integer.MAX_VALUE || (Double)value < Integer.MIN_VALUE){
                intObj = XBaseDefaultData.defaultIntegerData;
            }else {
                //强制类型转换，小数点后可能会有误差
                intObj = (Integer) value;
            }
        }else if(value instanceof Float){
            if((Float)value > Integer.MAX_VALUE || (Float)value < Integer.MIN_VALUE){
                intObj = XBaseDefaultData.defaultIntegerData;
            }else {
                //强制类型转换，小数点后可能会有误差
                intObj = (Integer)value;
            }
        }
        else if(value instanceof Number){
            intObj = ((Number) value).intValue();
        }else if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                intObj = XBaseDefaultData.defaultIntegerData;
            }else if(strVal.indexOf(',') != 0){
                strVal = strVal.replaceAll(",", "");
            }

            if(intObj == null){
                if(isInteger(strVal)){
                    intObj = Integer.parseInt(strVal);
                }else if(isDouble(strVal)){
                    if((Double)value > Integer.MAX_VALUE || (Double)value < Integer.MIN_VALUE){
                        intObj = XBaseDefaultData.defaultIntegerData;
                    }else {
                        ////强制类型转换，小数点后可能会有误差
                        intObj = Integer.parseInt(strVal);
                    }
                } else {
                    intObj = XBaseDefaultData.defaultIntegerData;
                }
            }

        }else if(value instanceof Boolean){
            intObj = ((Boolean) value).booleanValue() ? 1 : 0;
        }else if(value instanceof Map){
            intObj = XBaseDefaultData.defaultIntegerData;
        }else {
            intObj = XBaseDefaultData.defaultIntegerData;
        }

        return intObj;
    }

    /**
     * 将未知类型转换为长整型
     * @param value 未知数据类型
     * @return 返回长整型
     */
    public static Long castToLong(Object value){

        Long longObject = null;
        if(value == null){
            longObject = XBaseDefaultData.defaultLongData;
        }else if(value instanceof Long){
            longObject = (Long)value;
        }else if(value instanceof Integer){
            longObject = (Long) value;
        }else if(value instanceof Short){
            longObject = (Long) value;
        }else if(value instanceof Byte){
            longObject = (Long) value;
        } else if(value instanceof Double){
            if((Double)value > Long.MAX_VALUE || (Double)value < Long.MIN_VALUE){
                longObject = XBaseDefaultData.defaultLongData;
            }else {
                //这个地方的强制类型转换，会舍弃掉小数点或四舍五入
                longObject = (Long) value;
            }
        }else if(value instanceof Float){
            if((Float)value > Long.MAX_VALUE || (Float)value < Long.MIN_VALUE){
                longObject = XBaseDefaultData.defaultLongData;
            }else {
                //这个地方的强制类型转换，会舍弃掉小数点或四舍五入
                longObject = (Long)value;
            }
        } else if(value instanceof Number){
            longObject = ((Number) value).longValue();
        }else if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                longObject = XBaseDefaultData.defaultLongData;
            }else if(strVal.indexOf(',') != 0){
                strVal = strVal.replaceAll(",", "");
            }

            if(longObject == null){
                if(isInteger(strVal)){
                    longObject = Long.parseLong(strVal);
                }else if(isDouble(strVal)){
                    Double doubleValue = Double.parseDouble(strVal);
                    if(doubleValue > Long.MAX_VALUE || doubleValue < Long.MIN_VALUE){
                        longObject = XBaseDefaultData.defaultLongData;
                    }else {
                        //这个地方的强制类型转换，会舍弃掉小数点或四舍五入
                        longObject = Long.parseLong(strVal);
                    }
                }else {
                    longObject = XBaseDefaultData.defaultLongData;
                }
            }
        }else if(value instanceof Boolean){
            longObject = ((Boolean) value).booleanValue() ? 1L : 0L;
        }else if(value instanceof Map){
            longObject = XBaseDefaultData.defaultLongData;
        }else {
            longObject = XBaseDefaultData.defaultLongData;
        }

        return longObject;
    }


    /**
     * 将未知类型转换为bool类型
     * @param value 未知数据类型
     * @return 返回bool类型
     */
    public static Boolean castToBoolean(Object value){
        Boolean booleanObject = null;
        if(value == null){
            booleanObject = XBaseDefaultData.defaultBooleanData;
        }else if(value instanceof Long){
            Long longValue = (Long)value;
            if(longValue == 1){
                booleanObject = Boolean.TRUE;
            }else {
                booleanObject = Boolean.FALSE;
            }
        }else if(value instanceof Integer){
            Integer intValue = (Integer) value;
            if(intValue == 1){
                booleanObject = Boolean.TRUE;
            }else {
                booleanObject = Boolean.FALSE;
            }
        }else if(value instanceof Short){
            Short shortValue = (Short)value;
            if(shortValue == 1){
                booleanObject = Boolean.TRUE;
            }else {
                booleanObject = Boolean.FALSE;
            }
        }else if(value instanceof Byte){
            Byte byteValue = (Byte)value;
            if(byteValue == 1){
                booleanObject = Boolean.TRUE;
            }else {
                booleanObject = Boolean.FALSE;
            }
        } else if(value instanceof Double){
            Double doubleValue = (Double)value;
            if(doubleValue.compareTo(1.0) == 0){
                booleanObject = Boolean.TRUE;
            }else {
                booleanObject = Boolean.FALSE;
            }
        }else if(value instanceof Float){
            Float floatValue = (Float)value;
            if(floatValue.compareTo(1.0f) == 0){
                booleanObject = Boolean.TRUE;
            }else {
                booleanObject = Boolean.FALSE;
            }
        }else if(value instanceof Number){
            Long longValue = ((Number) value).longValue();
            if(longValue == 1){
                booleanObject = Boolean.TRUE;
            }else {
                booleanObject = Boolean.FALSE;
            }
        }else if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                booleanObject = XBaseDefaultData.defaultBooleanData;
            }else if(strVal.indexOf(',') != 0){
                strVal = strVal.replaceAll(",", "");
            }

            if(booleanObject == null){
                if(isInteger(strVal)){
                    Long longValue = Long.parseLong(strVal);
                    if(longValue == 1){
                        booleanObject = Boolean.TRUE;
                    }else {
                        booleanObject = Boolean.FALSE;
                    }
                }else if(isDouble(strVal)){
                    Double doubleValue = Double.parseDouble(strVal);
                    if(doubleValue.compareTo(1.0) == 0){
                        booleanObject = Boolean.TRUE;
                    }else {
                        booleanObject = Boolean.FALSE;
                    }
                }else {
                    if("true".equalsIgnoreCase(strVal) //
                            || "1".equals(strVal)){
                        booleanObject = Boolean.TRUE;
                    }else if("false".equalsIgnoreCase(strVal) //
                            || "0".equals(strVal)){
                        booleanObject = Boolean.FALSE;
                    }else if("yes".equalsIgnoreCase(strVal)){
                        booleanObject = Boolean.TRUE;
                    }else if("no".equalsIgnoreCase(strVal)){
                        booleanObject = Boolean.FALSE;
                    } else if("Y".equalsIgnoreCase(strVal) //
                            || "T".equalsIgnoreCase(strVal)){
                        booleanObject = Boolean.TRUE;
                    }else if("F".equalsIgnoreCase(strVal) //
                            || "N".equalsIgnoreCase(strVal)){
                        booleanObject = Boolean.FALSE;
                    }else {
                        booleanObject = XBaseDefaultData.defaultBooleanData;
                    }
                }
            }
        }else if(value instanceof Boolean){
            booleanObject = ((Boolean) value).booleanValue();
        }else if(value instanceof Map){
            booleanObject = XBaseDefaultData.defaultBooleanData;
        }else {
            booleanObject = XBaseDefaultData.defaultBooleanData;
        }

        return booleanObject;
    }

}
