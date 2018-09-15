package com.xframework_parse.xparse.xdeserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.FloatCodec;
import com.xframework_parse.xparse.XTypeUtis;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lanbiao on 2018/06/07
 * 抽象浮点型解析器
 */
public class XBaseFloatDeserializer extends FloatCodec {

    private static XBaseFloatDeserializer floatInstance = null;

    private XBaseFloatDeserializer(){
        super();
    }

    public static XBaseFloatDeserializer getInstance(){
        if(floatInstance == null){
            synchronized (XBaseFloatDeserializer.class){
                if(floatInstance == null){
                    floatInstance = new XBaseFloatDeserializer();
                }
            }
        }

        return floatInstance;
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {

        final JSONLexer lexer = parser.lexer;

        final int token = lexer.token();
        Float floatObj = null;
        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return (T)XBaseDefaultData.defaultFloatData;
        }

        try {
            if (token == JSONToken.LITERAL_INT) {
                Long longVal = lexer.longValue();
                lexer.nextToken(JSONToken.COMMA);
                if(longVal > Float.MAX_VALUE || longVal < Float.MIN_VALUE){
                    floatObj = XBaseDefaultData.defaultFloatData;
                }else {
                    floatObj = longVal.floatValue();
                }
            } else if (token == JSONToken.LITERAL_FLOAT) {
                BigDecimal decimalValue = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                if(decimalValue.intValue() > Float.MAX_VALUE || decimalValue.intValue() < Float.MIN_VALUE){
                    floatObj = XBaseDefaultData.defaultFloatData;
                }else {
                    floatObj = decimalValue.floatValue();
                }
            } else {
                if (token == JSONToken.LBRACE) {
                    JSONObject jsonObject = new JSONObject(true);
                    parser.parseObject(jsonObject);
                    floatObj = XTypeUtis.castToFloat(jsonObject);
                } else {
                    Object value = parser.parse();
                    floatObj = XTypeUtis.castToFloat(value);
                }
            }
        } catch (Exception ex) {
            throw new JSONException("parseInt error, field : " + fieldName, ex);
        }

        if (clazz == AtomicInteger.class) {
            return (T) new AtomicInteger(floatObj.intValue());
        }

        if(floatObj == null){
            floatObj = XBaseDefaultData.defaultFloatData;
        }

        return (T)floatObj;
    }
}
