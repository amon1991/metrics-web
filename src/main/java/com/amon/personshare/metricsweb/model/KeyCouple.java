package com.amon.personshare.metricsweb.model;

/**
 * yaming.chen@siemens.com
 * Created by chenyaming on 2016/7/20.
 */
public class KeyCouple {

    private String appname;
    private String metricskey;

    @Override
    public String toString() {
        return "KeyCouple{" +
                "appname='" + appname + '\'' +
                ", metricskey='" + metricskey + '\'' +
                '}';
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getMetricskey() {
        return metricskey;
    }

    public void setMetricskey(String metricskey) {
        this.metricskey = metricskey;
    }
}
