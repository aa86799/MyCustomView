package com.stone.customview.model;

import java.util.Map;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/6/1 15 51
 */
public class PuzzleBean {

    private String coverThumb;
    private Map<Integer, String[]> point;


    public String getCoverThumb() {
        return coverThumb;
    }

    public void setCoverThumb(String coverThumb) {
        this.coverThumb = coverThumb;
    }

    public Map<Integer, String[]> getPoint() {
        return point;
    }

    public void setPoint(Map<Integer, String[]> point) {
        this.point = point;
    }
}
