package com.amon.personshare.metricsweb.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/10.
 */
public class DynamicChartData {

    private String className;
    private ArrayList<?> data;

    private int count;
    private String date;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ArrayList<?> getData() {
        return data;
    }

    public void setData(ArrayList<?> data) {
        this.data = data;
    }



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
