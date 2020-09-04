package com.xframework_parse.xparse;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.xframework_parse.xparse.xdeserializer.XBaseBooleanDeserializer;
import com.xframework_parse.xparse.xdeserializer.XBaseDoubleDeserializer;
import com.xframework_parse.xparse.xdeserializer.XBaseDoubleFieldDeserializer;
import com.xframework_parse.xparse.xdeserializer.XBaseFloatDeserializer;
import com.xframework_parse.xparse.xdeserializer.XBaseFloatFieldDeserializer;
import com.xframework_parse.xparse.xdeserializer.XBaseIntDeserializer;
import com.xframework_parse.xparse.xdeserializer.XBaseLongDeserializer;
import com.xframework_parse.xparse.xdeserializer.XStringDeserializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanbiao on 2018/06/08
 * 解析器配置器
 */
public class XParserConfig extends ParserConfig {
    private static XParserConfig configInstance = null;
    private XParserConfig(){
        super();
        loadJSONDeserializer();
    }

    // 配置自定义解析器支持的解析类型，包括基础数据类型以及模型类型
    private void loadJSONDeserializer(){
        putDeserializer(int.class, XBaseIntDeserializer.getInstance());
        putDeserializer(Integer.class, XBaseIntDeserializer.getInstance());
        putDeserializer(float.class, XBaseFloatDeserializer.getInstance());
        putDeserializer(Float.class, XBaseFloatDeserializer.getInstance());
        putDeserializer(double.class, XBaseDoubleDeserializer.getInstance());
        putDeserializer(Double.class, XBaseDoubleDeserializer.getInstance());
        putDeserializer(long.class, XBaseLongDeserializer.getInstance());
        putDeserializer(Long.class, XBaseLongDeserializer.getInstance());
        putDeserializer(Boolean.class, XBaseBooleanDeserializer.getInstance());
        putDeserializer(boolean.class, XBaseBooleanDeserializer.getInstance());
        putDeserializer(String.class, XStringDeserializer.getInstance());
        putDeserializer(StringBuilder.class, XStringDeserializer.getInstance());
        putDeserializer(StringBuffer.class, XStringDeserializer.getInstance());
        //putDeserializer(Object.class,new JavaBeanDeserializer(this,Object.class));
    }

    public static XParserConfig getInstance(){
        if(configInstance == null){
            synchronized (XParserConfig.class){
                if(configInstance == null){
                    configInstance = new XParserConfig();
                }
            }
        }
        return configInstance;
    }

    @Override
    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, JavaBeanInfo beanInfo, FieldInfo fieldInfo) {
        Class<?> clazz = beanInfo.clazz;
        Class<?> fieldClass = fieldInfo.fieldClass;

        Class<?> deserializeUsing = null;
        JSONField annotation = fieldInfo.getAnnotation();
        if (annotation != null) {
            deserializeUsing = annotation.deserializeUsing();
            if (deserializeUsing == Void.class) {
                deserializeUsing = null;
            }
        }

        if (deserializeUsing == null && (fieldClass == List.class || fieldClass == ArrayList.class)) {
            return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if(fieldClass == Float.class || fieldClass == float.class){
            return new XBaseFloatFieldDeserializer(mapping,clazz,fieldInfo);
        }else if(fieldClass == Double.class || fieldClass == double.class){
            return new XBaseDoubleFieldDeserializer(mapping,clazz,fieldInfo);
        }

        return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
    }
}
