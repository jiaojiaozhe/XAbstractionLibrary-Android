package com.personal.xabstractionlibrary;

import com.framework.model.BaseModel;

public class DistanceCountGplusDataModel extends BaseModel {
    private String lng;
    private String lat;
    private String radius;

    public DistanceCountGplusDataModel(String lng,String lat,String radius){
        super();
        this.lng = lng;
        this.lat = lat;
        this.radius = radius;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }
}
