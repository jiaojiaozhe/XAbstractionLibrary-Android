package com.xframework_parse.xparse.xdeserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import com.xframework_parse.xparse.XTypeUtis;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Created by lanbiao on 2018/06/07
 * 抽象双精度浮点型解析器
 */
public class XBaseDoubleDeserializer extends NumberDeserializer {

    private static XBaseDoubleDeserializer doubleInstance = null;

    private XBaseDoubleDeserializer(){
        super();
    }

    public static XBaseDoubleDeserializer getInstance(){
        if(doubleInstance == null){
            synchronized (XBaseDoubleDeserializer.class){
                if(doubleInstance == null){
                    doubleInstance = new XBaseDoubleDeserializer();
                }
            }
        }

        return doubleInstance;
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONLexer lexer = parser.lexer;

        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return (T)XBaseDefaultData.defaultDoubleData;
        }

        if (lexer.token() == JSONToken.LITERAL_INT) {
            if (clazz == double.class || clazz  == Double.class) {
                String val = lexer.numberString();
                lexer.nextToken(JSONToken.COMMA);
                return (T) Double.valueOf(Double.parseDouble(val));
            }

            long val = lexer.longValue();
            lexer.nextToken(JSONToken.COMMA);

            if (clazz == short.class || clazz == Short.class) {
                if (val > Short.MAX_VALUE || val < Short.MIN_VALUE) {
                    throw new JSONException("short overflow : " + val);
                }
                return (T) Short.valueOf((short) val);
            }

            if (clazz == byte.class || clazz == Byte.class) {
                if (val > Byte.MAX_VALUE || val < Byte.MIN_VALUE) {
                    throw new JSONException("short overflow : " + val);
                }

                return (T) Byte.valueOf((byte) val);
            }

            if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
                return (T) Integer.valueOf((int) val);
            }
            return (T) Long.valueOf(val);
        }

        if (lexer.token() == JSONToken.LITERAL_FLOAT) {
            if (clazz == double.class || clazz == Double.class) {
                String val = lexer.numberString();
                lexer.nextToken(JSONToken.COMMA);
                return (T) Double.valueOf(Double.parseDouble(val));
            }

            BigDecimal val = lexer.decimalValue();
            lexer.nextToken(JSONToken.COMMA);

            if (clazz == short.class || clazz == Short.class) {
                if (val.compareTo(BigDecimal.valueOf(Short.MAX_VALUE)) > 0 || val.compareTo(BigDecimal.valueOf(Short.MIN_VALUE)) < 0) {
                    throw new JSONException("short overflow : " + val);
                }
                return (T) Short.valueOf(val.shortValue());
            }

            if (clazz == byte.class || clazz == Byte.class) {
                return (T) Byte.valueOf(val.byteValue());
            }

            return (T) val;
        }

        if (lexer.token() == JSONToken.IDENTIFIER && "NaN".equals(lexer.stringVal())) {
            lexer.nextToken();
            Object nan = null;
            if (clazz == Double.class) {
                nan = XBaseDefaultData.defaultDoubleData;
            } else if (clazz == Float.class) {
                nan = XBaseDefaultData.defaultFloatData;
            }
            return (T) nan;
        }

        Object value = parser.parse();

        if (value == null) {
            return (T)XBaseDefaultData.defaultDoubleData;
        }

        if (clazz == double.class || clazz == Double.class) {
            try {
                return (T) XTypeUtis.castToDouble(value);
            } catch (Exception ex) {
                throw new JSONException("parseDouble error, field : " + fieldName, ex);
            }
        }

        if (clazz == short.class || clazz == Short.class) {
            try {
                return (T) XTypeUtis.castToShort(value);
            } catch (Exception ex) {
                throw new JSONException("parseShort error, field : " + fieldName, ex);
            }
        }

        if (clazz == byte.class || clazz == Byte.class) {
            try {
                return (T) XTypeUtis.castToByte(value);
            } catch (Exception ex) {
                throw new JSONException("parseByte error, field : " + fieldName, ex);
            }
        }

        return (T) TypeUtils.castToBigDecimal(value);
    }
}
