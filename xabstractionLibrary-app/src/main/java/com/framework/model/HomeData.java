package com.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class HomeData extends BaseModel {

    @JSONField(name="OpertationsActivityVO")
    private ArrayList<OpertationsModel> opertationVO;

    public ArrayList<OpertationsModel> getOpertationVO() {
        return opertationVO;
    }

    public void setOpertationVO(ArrayList<OpertationsModel> opertationVO) {
        this.opertationVO = opertationVO;
    }
}
