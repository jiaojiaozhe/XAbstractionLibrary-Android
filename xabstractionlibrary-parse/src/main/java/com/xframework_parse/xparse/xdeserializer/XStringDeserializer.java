package com.xframework_parse.xparse.xdeserializer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.StringCodec;

import java.lang.reflect.Type;

/**
 * Creayte by lanbiao on 2018/06/09
 * 字符型解析器
 */
public class XStringDeserializer extends StringCodec {

    private static XStringDeserializer stringInstance = null;

    private XStringDeserializer(){
        super();
    }

    public static XStringDeserializer getInstance(){
        if(stringInstance == null){
            synchronized (XStringDeserializer.class){
                if(stringInstance == null) {
                    stringInstance = new XStringDeserializer();
                }
            }
        }
        return stringInstance;
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        if (clazz == StringBuffer.class) {
            final JSONLexer lexer = parser.lexer;
            String stringObj = null;
            if(lexer.token() == JSONToken.NULL){
                lexer.nextToken(JSONToken.COMMA);
                stringObj = XBaseDefaultData.defaultStringData;
            }else if(lexer.token() == JSONToken.LITERAL_INT){
                Long longValue = lexer.longValue();
                stringObj = longValue.toString();
                lexer.nextToken(JSONToken.COMMA);
            }else if(lexer.token() == JSONToken.LITERAL_FLOAT){
                Float floatValue = lexer.floatValue();
                stringObj = floatValue.toString();
                lexer.nextToken(JSONToken.COMMA);
            } else if (lexer.token() == JSONToken.LITERAL_STRING) {
                stringObj = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);
            }else {
                if (lexer.token() == JSONToken.LBRACE) {
                    JSONObject jsonObject = new JSONObject(true);
                    parser.parseObject(jsonObject);
                    stringObj = jsonObject.toString();
                } else {
                    Object value = parser.parse();
                    stringObj = value.toString();
                }
            }

            if (stringObj == null) {
                stringObj = XBaseDefaultData.defaultStringData;
            }

            return (T) new StringBuffer(stringObj);
        }

        if (clazz == StringBuilder.class) {
            final JSONLexer lexer = parser.lexer;
            String stringObj = null;
            if(lexer.token() == JSONToken.NULL){
                lexer.nextToken(JSONToken.COMMA);
                stringObj = XBaseDefaultData.defaultStringData;
            }else if(lexer.token() == JSONToken.LITERAL_INT){
                Long longValue = lexer.longValue();
                stringObj = longValue.toString();
                lexer.nextToken(JSONToken.COMMA);
            }else if(lexer.token() == JSONToken.LITERAL_FLOAT){
                Float floatValue = lexer.floatValue();
                stringObj = floatValue.toString();
                lexer.nextToken(JSONToken.COMMA);
            } else if (lexer.token() == JSONToken.LITERAL_STRING) {
                stringObj = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);
            }else {
                if (lexer.token() == JSONToken.LBRACE) {
                    JSONObject jsonObject = new JSONObject(true);
                    parser.parseObject(jsonObject);
                    stringObj = jsonObject.toString();
                } else {
                    Object value = parser.parse();
                    stringObj = value.toString();
                }
            }

            if (stringObj == null) {
                stringObj = XBaseDefaultData.defaultStringData;
            }

            return (T) new StringBuilder(stringObj);
        }

        return (T) deserialze(parser);
    }

    public static <T> T deserialze(DefaultJSONParser parser) {
        final JSONLexer lexer = parser.lexer;
        String stringObj = null;
        if(lexer.token() == JSONToken.NULL){
            lexer.nextToken(JSONToken.COMMA);
            stringObj = XBaseDefaultData.defaultStringData;
        }else if(lexer.token() == JSONToken.LITERAL_INT){
            Long longValue = lexer.longValue();
            stringObj = longValue.toString();
            lexer.nextToken(JSONToken.COMMA);
        }else if(lexer.token() == JSONToken.LITERAL_FLOAT){
            Float floatValue = lexer.floatValue();
            stringObj = floatValue.toString();
            lexer.nextToken(JSONToken.COMMA);
        } else if (lexer.token() == JSONToken.LITERAL_STRING) {
            stringObj = lexer.stringVal();
            lexer.nextToken(JSONToken.COMMA);
        }else {
            if (lexer.token() == JSONToken.LBRACE) {
                JSONObject jsonObject = new JSONObject(true);
                parser.parseObject(jsonObject);
                stringObj = jsonObject.toString();
            } else {
                Object value = parser.parse();
                stringObj = value.toString();
            }
        }

        if (stringObj == null) {
            stringObj = XBaseDefaultData.defaultStringData;
        }

        return (T) stringObj;
    }
}
