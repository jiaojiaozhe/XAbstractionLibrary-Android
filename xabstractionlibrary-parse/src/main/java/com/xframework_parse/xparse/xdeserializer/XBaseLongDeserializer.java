package com.xframework_parse.xparse.xdeserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.LongCodec;
import com.xframework_parse.xparse.XTypeUtis;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lanbiao on 2018/06/08
 * 长整型解析器
 */
public class XBaseLongDeserializer extends LongCodec {

    private static XBaseLongDeserializer longInstance = null;

    private XBaseLongDeserializer(){
        super();
    }

    public static XBaseLongDeserializer getInstance(){
        if(longInstance == null){
            synchronized (XBaseLongDeserializer.class){
                if(longInstance == null){
                    longInstance = new XBaseLongDeserializer();
                }
            }
        }
        return longInstance;
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONLexer lexer = parser.lexer;

        Long longObject;
        try {
            final int token = lexer.token();

            if (token == JSONToken.NULL) {
                lexer.nextToken(JSONToken.COMMA);
                longObject = XBaseDefaultData.defaultLongData;
            }else if (token == JSONToken.LITERAL_INT) {
                Long longValue = lexer.longValue();
                lexer.nextToken(JSONToken.COMMA);
                longObject = longValue;
            } else if (token == JSONToken.LITERAL_FLOAT) {
                BigDecimal decimalValue = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                if(decimalValue.longValue() > Long.MAX_VALUE || decimalValue.longValue() < Long.MIN_VALUE){
                    longObject = XBaseDefaultData.defaultLongData;
                }else {
                    longObject = decimalValue.longValue();
                }
            } else {
                if (token == JSONToken.LBRACE) {
                    JSONObject jsonObject = new JSONObject(true);
                    parser.parseObject(jsonObject);
                    longObject = XTypeUtis.castToLong(jsonObject);
                } else {
                    Object value = parser.parse();
                    longObject = XTypeUtis.castToLong(value);
                }
            }
        } catch (Exception ex) {
            throw new JSONException("parseLong error, field : " + fieldName, ex);
        }

        if (clazz == AtomicInteger.class) {
            return (T) new AtomicLong(longObject.longValue());
        }

        if(longObject == null){
            longObject = XBaseDefaultData.defaultLongData;
        }

        return (T)longObject;
    }
}
