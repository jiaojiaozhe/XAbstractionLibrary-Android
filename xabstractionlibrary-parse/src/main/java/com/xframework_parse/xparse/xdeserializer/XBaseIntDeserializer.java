package com.xframework_parse.xparse.xdeserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.xframework_parse.xparse.XTypeUtis;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lanbiao on 2018/06/07
 * 抽象整形解析器，具备数据容错纠错能力
 */
public class XBaseIntDeserializer extends IntegerCodec {

    private static XBaseIntDeserializer baseInstance = null;
    private XBaseIntDeserializer(){
        super();
    }

    public static XBaseIntDeserializer getInstance(){
        if(baseInstance == null){
            synchronized (XBaseIntDeserializer.class){
                if(baseInstance == null){
                    baseInstance = new XBaseIntDeserializer();
                }
            }
        }
        return baseInstance;
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {

        final JSONLexer lexer = parser.lexer;

        final int token = lexer.token();

        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return (T)XBaseDefaultData.defaultIntegerData;
        }

        Integer intObj = null;
        try {
            if (token == JSONToken.LITERAL_INT) {
                Long longValue = lexer.longValue();
                lexer.nextToken(JSONToken.COMMA);
                if(longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE){
                    intObj = XBaseDefaultData.defaultIntegerData;
                }else {
                    intObj = longValue.intValue();
                }
            } else if (token == JSONToken.LITERAL_FLOAT) {
                BigDecimal decimalValue = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                if(decimalValue.intValue() > Integer.MAX_VALUE || decimalValue.intValue() < Integer.MIN_VALUE){
                    intObj = XBaseDefaultData.defaultIntegerData;
                }else {
                    intObj = Integer.valueOf(decimalValue.intValue());
                }
            } else {
                if (token == JSONToken.LBRACE) {
                    JSONObject jsonObject = new JSONObject(true);
                    parser.parseObject(jsonObject);
                    intObj = XTypeUtis.castToInteger(jsonObject);
                } else {
                    Object value = parser.parse();
                    intObj = XTypeUtis.castToInteger(value);
                }
            }
        } catch (Exception ex) {
            throw new JSONException("parseInt error, field : " + fieldName, ex);
        }

        if (clazz == AtomicInteger.class) {
            return (T) new AtomicInteger(intObj.intValue());
        }

        if(intObj == null){
            intObj = XBaseDefaultData.defaultIntegerData;
        }

        return (T)intObj;
    }
}
