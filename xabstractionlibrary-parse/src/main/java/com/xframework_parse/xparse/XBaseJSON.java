package com.xframework_parse.xparse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.xframework_base.xmodel.XBaseModel;

/**
 * created by lanbiao on 2018/06/05
 * 抽象解析器
 */
public class XBaseJSON extends XBaseModel {

    protected static XBaseJSON baseJson = null;

    protected XBaseJSON(){
        super();
    }


    public  <T extends XBaseModel> T parseObject(String content, Class<T> clazz){

        T object = JSON.parseObject(content,clazz,XParserConfig.getInstance(),new Feature[0]);
        return object;
    }

    public  <T extends XBaseModel> T parseObject(String content, TypeReference<T> type){
        T object = JSON.parseObject(content,type.getType(),XParserConfig.getInstance(),new Feature[0]);
        return object;
    }
}
