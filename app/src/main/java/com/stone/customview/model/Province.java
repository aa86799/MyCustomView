package com.stone.customview.model;

import java.util.List;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/5/5 14 54
 */
public class Province {

    private String sortKey;
    private String name;

    private List<City> cityList;

    public Province() {
    }

    public Province(String sortKey, String name) {
        this.sortKey = sortKey;
        this.name = name;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
