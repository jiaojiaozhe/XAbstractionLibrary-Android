package com.xframework_parse.xparse.xdeserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.BooleanCodec;
import com.xframework_parse.xparse.XTypeUtis;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lanbiao on 2018/06/09
 * 布尔类型的解析器
 */
public class XBaseBooleanDeserializer extends BooleanCodec {

    private static XBaseBooleanDeserializer booleanInstance = null;

    private XBaseBooleanDeserializer(){
        super();
    }

    public static XBaseBooleanDeserializer getInstance(){
        if(booleanInstance == null){
            synchronized (XBaseBooleanDeserializer.class){
                if(booleanInstance == null){
                    booleanInstance = new XBaseBooleanDeserializer();
                }
            }
        }
        return booleanInstance;
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONLexer lexer = parser.lexer;

        Boolean boolObj;

        try {
            if(lexer.token() == JSONToken.NULL){
                lexer.nextToken(JSONToken.COMMA);
                boolObj = XBaseDefaultData.defaultBooleanData;
            }else if (lexer.token() == JSONToken.TRUE) {
                lexer.nextToken(JSONToken.COMMA);
                boolObj = Boolean.TRUE;
            } else if (lexer.token() == JSONToken.FALSE) {
                lexer.nextToken(JSONToken.COMMA);
                boolObj = Boolean.FALSE;
            } else if (lexer.token() == JSONToken.LITERAL_INT) {
                Long longValue = lexer.longValue();
                lexer.nextToken(JSONToken.COMMA);

                if (longValue == 1) {
                    boolObj = Boolean.TRUE;
                } else {
                    boolObj = Boolean.FALSE;
                }
            } else {

                if (lexer.token() == JSONToken.LBRACE) {
                    JSONObject jsonObject = new JSONObject(true);
                    parser.parseObject(jsonObject);
                    boolObj = XTypeUtis.castToBoolean(jsonObject);
                } else {
                    Object value = parser.parse();
                    boolObj = XTypeUtis.castToBoolean(value);
                }
            }
        } catch (Exception ex) {
            throw new JSONException("parseBoolean error, field : " + fieldName, ex);
        }

        if (clazz == AtomicBoolean.class) {
            return (T) new AtomicBoolean(boolObj.booleanValue());
        }

        return (T) boolObj;
    }
}
