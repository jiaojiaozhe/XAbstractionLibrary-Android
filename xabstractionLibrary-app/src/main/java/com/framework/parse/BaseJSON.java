package com.framework.parse;

import com.xframework_parse.xparse.XBaseJSON;

/**
 * Created by lanbiao on 2018/06/05
 * 基础解析器对象
 */
public class BaseJSON extends XBaseJSON {

    private BaseJSON() {
        super();
    }

    public static XBaseJSON getBaseInstence(){
        if(baseJson == null){
            synchronized (BaseJSON.class){
                if(baseJson == null){
                    baseJson = new BaseJSON();
                }
            }
        }
        return baseJson;
    }
}
