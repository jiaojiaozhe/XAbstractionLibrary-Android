package com.xframework_base.xmodel;

import com.xframework_base.xutils.XUUID;

import java.io.Serializable;

/**
 * Created by lanbia on 2018/5/30
 * 基础对象,其他数据相关的对象都需要直接或间接继承自它
 */
public abstract class XBaseModel implements Serializable {

    /**
     * 对象ID，用于区分不同对象
     */
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public XBaseModel(){
        super();
        setId(getUUID());
    }

    /**
     * 随机产生的一个随机串，表示唯一对象，提供者确保唯一性
     * @return 唯一字符串
     */
    protected String getUUID(){
        return XUUID.getInstance().getUUID();
    }

    /**
     * 验证对象是否合法
     *
     * @return true表示合法 否则不合法
     */
    public boolean validateID() {
        String id = getId();
        if (id == null)
            return false;

        if (id.length() <= 0)
            return false;
        else
            return true;
    }

    /**
     * 验证对象是否相等
     * @param model 待比较的对象
     * @return true相同 false不同
     */
    public boolean isEqual(XBaseModel model){
        if(model == null || model.getId() == null)
            return false;

        if(this == model)
            return true;

        if(model.getId().equals(getId()))
            return true;

        return false;
    }
}
