package com.xframework_parse.xparse.xdeserializer;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.util.FieldInfo;

/**
 * Created by lanbiao on 2018/06/08
 * 双精度浮点型属性解析器
 */
public class XBaseDoubleFieldDeserializer extends DefaultFieldDeserializer {
    public XBaseDoubleFieldDeserializer(ParserConfig config, Class<?> clazz, FieldInfo fieldInfo) {
        super(config, clazz, fieldInfo);
    }

    @Override
    public void setValue(Object object, Object value) {
        super.setValue(object, value);
    }
}
