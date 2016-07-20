package com.amon.personshare.metricsweb.model;

/**
 * Created by 710-6 on 2015/11/18.
 */
public class HealthChecks {

    private String time;
    private String metricskey;
    private int isHealth;
    private String message;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMetricskey() {
        return metricskey;
    }

    public void setMetricskey(String metricskey) {
        this.metricskey = metricskey;
    }

    public int getIsHealth() {
        return isHealth;
    }

    public void setIsHealth(int isHealth) {
        this.isHealth = isHealth;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
