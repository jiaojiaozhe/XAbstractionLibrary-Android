package com.xframework_parse.xparse.xdeserializer;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.util.FieldInfo;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by lanbiao on 2018/06/08
 * 单精度浮点型的属性解析器
 */
public class XBaseFloatFieldDeserializer extends DefaultFieldDeserializer {
    public XBaseFloatFieldDeserializer(ParserConfig config, Class<?> clazz, FieldInfo fieldInfo) {
        super(config, clazz, fieldInfo);
    }

    @Override
    public void setValue(Object object, Object value) {
        super.setValue(object, value);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        super.parseField(parser, object, objectType, fieldValues);
    }
}
