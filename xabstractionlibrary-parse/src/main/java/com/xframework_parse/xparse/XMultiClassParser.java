package com.xframework_parse.xparse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xframework_base.xmodel.XBaseModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class XMultiClassParser extends XBaseModel {
    public static <T> ArrayList<T> parse(List jsonArray, Class<? extends T>[] classes) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return new ArrayList<>();
        }
        if (!(jsonArray.get(0) instanceof JSONObject)) {
            return (ArrayList<T>) jsonArray;
        }

        ArrayList<T> result = new ArrayList<>();
        try {
            Class<? extends T>[] bestClasses = new Class[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                float max = 0;// 匹配度 （参数名匹配数量）
                for (Class<? extends T> clazz : classes) {// 遍历所有类型
                    Field[] classFields = clazz.getDeclaredFields();
                    int sum = 0;
                    for (Field classField : classFields) {// 遍历类的所有成员
                        String classFiledName = classField.getName();
                        aa:
                        for (String itemFileName : item.keySet()) {// 查找该成员是否与item的成员相同
                            if (itemFileName.equals(classFiledName)) {
                                sum++;
                                break aa;
                            }
                        }
                    }
                    float percent = sum * 1f / Math.max(item.keySet().size(), classFields.length);
                    if (max < percent) {
                        bestClasses[i] = clazz;
                        max = percent;
                    }
                }
                result.add(JSON.parseObject(item.toJSONString(), bestClasses[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object parse(Object jsonArray, Class<? extends Object>[] classes) {
        if (jsonArray == null) {
            return new ArrayList<>();
        }
        if (!(jsonArray instanceof JSONObject)) {
            return jsonArray;
        }

        try {
            JSONObject item = (JSONObject) jsonArray;
            float max = 0;// 匹配度 （参数名匹配数量）
            Class c = classes[0];
            for (Class<? extends Object> clazz : classes) {// 遍历所有类型
                Field[] classFields = clazz.getDeclaredFields();
                int sum = 0;
                for (Field classField : classFields) {// 遍历类的所有成员
                    String classFiledName = classField.getName();
                    aa:
                    for (String itemFileName : item.keySet()) {// 查找该成员是否与item的成员相同
                        if (itemFileName.equals(classFiledName)) {
                            sum++;
                            break aa;
                        }
                    }
                }
                float percent = sum * 1f / Math.max(item.keySet().size(), classFields.length);
                if (max < percent) {
                    c = clazz;
                    max = percent;
                }
            }
            return JSON.parseObject(item.toJSONString(), c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
