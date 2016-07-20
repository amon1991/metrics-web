package com.amon.personshare.metricsweb.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/10.
 */
public class DynamicChartModel {

    private String xScale;
    private int yMin;
    private ArrayList<String> comp;
    private ArrayList<DynamicChartData> main;
    private String type;
    private String yScale;

    public String getxScale() {
        return xScale;
    }

    public void setxScale(String xScale) {
        this.xScale = xScale;
    }

    public ArrayList<String> getComp() {
        return comp;
    }

    public void setComp(ArrayList<String> comp) {
        this.comp = comp;
    }

    public ArrayList<DynamicChartData> getMain() {
        return main;
    }

    public void setMain(ArrayList<DynamicChartData> main) {
        this.main = main;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getyScale() {
        return yScale;
    }

    public void setyScale(String yScale) {
        this.yScale = yScale;
    }

    public int getyMin() {
        return yMin;
    }

    public void setyMin(int yMin) {
        this.yMin = yMin;
    }

}
