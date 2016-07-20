package com.amon.personshare.metricsweb.action;

import com.amon.personshare.metricsweb.model.HealthChecks;
import com.amon.personshare.metricsweb.service.HealthCheckService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by 710-6 on 2015/11/18.
 */
public class HealthCheckAction extends ActionSupport {

    private static final long serialVersionUID = -4331264545027617760L;

    private String appName;    //应用名称
    private String metricsKey; //指标key

    private String healthCheckTime;
    private int isHealth;
    private String healthCheckMessage;

    /**
     * 根据查询条件，获取指标最新的健康信息
     * @return
     */
    public String findIsHealth(){

        System.out.println("findIsHealth...");

        HealthCheckService service = new HealthCheckService();
        HealthChecks healthChecks = service.getSingleLeastData(appName,metricsKey);

        if (null!=healthChecks){
            healthCheckTime = healthChecks.getTime();
            isHealth = healthChecks.getIsHealth();
            healthCheckMessage = healthChecks.getMessage();
        }

        return SUCCESS;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMetricsKey() {
        return metricsKey;
    }

    public void setMetricsKey(String metricsKey) {
        this.metricsKey = metricsKey;
    }

    public String getHealthCheckTime() {
        return healthCheckTime;
    }

    public void setHealthCheckTime(String healthCheckTime) {
        this.healthCheckTime = healthCheckTime;
    }

    public int getIsHealth() {
        return isHealth;
    }

    public void setIsHealth(int isHealth) {
        this.isHealth = isHealth;
    }

    public String getHealthCheckMessage() {
        return healthCheckMessage;
    }

    public void setHealthCheckMessage(String healthCheckMessage) {
        this.healthCheckMessage = healthCheckMessage;
    }
}
