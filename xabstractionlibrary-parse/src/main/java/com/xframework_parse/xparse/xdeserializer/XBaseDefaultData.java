package com.xframework_parse.xparse.xdeserializer;

/**
 * created by lanbiao on 2018/06/07
 * 抽象解析器默认容错的默认值
 */
public interface XBaseDefaultData {

    /**
     * 默认字节型的解析器值
     */
    final Byte defaultByteData = Byte.MIN_VALUE;

    /**
     * 默认短整型解析器值
     */
    final Short defaultShortData = Short.MIN_VALUE;

    /**
     * 整形默认解析器值
     */
    final Integer defaultIntegerData = Integer.MIN_VALUE;

    /**
     * 默认长整型解析器值
     */
    final Long defaultLongData = Long.MIN_VALUE;

    /**
     * 浮点型默认解析器值
     */
    final Float defaultFloatData = Float.MIN_NORMAL;

    /**
     * 双精度默认解析器值
     */
    final Double defaultDoubleData = Double.MIN_NORMAL;

    /**
     * 字符串默认解析器值
     */
    final String defaultStringData = "NAN~~nan";

    /**
     * 布尔默认值
     */
    final Boolean defaultBooleanData = Boolean.FALSE;

}
