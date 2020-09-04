package com.xframework_parse.xparse;

import com.alibaba.fastjson.JSONObject;
import com.xframework_base.xmodel.XBaseModel;

import java.lang.reflect.Field;
import java.util.List;

public class XRunTimeCompare<T> extends XBaseModel {

    public static Field[] getFieldsFromClass(Class cls){

        if(cls == null)
            return null;

        Field[] fields = cls.getDeclaredFields();

        //取子孙类数据
        Class superClass = cls.getSuperclass();
        if(superClass == null){
            return null;
        }

        int index = 0;
        Field[] superClassField = getFieldsFromClass(superClass);
        Field[] totalFields = null;
        if(superClassField != null){
            totalFields = new Field[fields.length + superClassField.length];

            for(Field field : fields){
                totalFields[index ++] = field;
            }

            for(Field field : superClassField){
                totalFields[index ++] = field;
            }
            return totalFields;
        }else {
            totalFields = new Field[fields.length];
            for(Field field : fields){
                totalFields[index ++] = field;
            }
        }


        return totalFields;
    }

    public static <T> T getClassCompareWeight(List<String> list, JSONObject jsonObject){

        Object object = null;
        if(list.size() <= 0)
            return null;

        if(jsonObject.size() <= 0)
            return null;


        float maxWeight = 0.0f;
        int maxFieldSize = -1;
        for(int index = 0; index < list.size(); index++){
            String className = list.get(index);
            try {
                Class cls = Class.forName(className);
                Field[] fields = getFieldsFromClass(cls);
                Object newObject = cls.newInstance();
                int fieldSize = fields.length;

                int count = 0;
                for(Field field : fields){
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    for(String key : jsonObject.keySet()){
                        if(fieldName.toLowerCase().equals(key.toLowerCase())){
                            count ++;
                            field.set(newObject,jsonObject.get(key));
                            break;
                        }
                    }
                }
                float weight = (count * 1.0f) / fields.length;
                if(weight > maxWeight){
                    maxWeight = weight;
                    maxFieldSize = fieldSize;
                    object = newObject;
                }else if(weight == maxWeight){
                    if(fieldSize > maxFieldSize){
                        maxWeight = weight;
                        maxFieldSize = fieldSize;
                        object = newObject;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return (T)object;
    }
}
