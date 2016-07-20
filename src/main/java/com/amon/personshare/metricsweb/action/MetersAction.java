package com.amon.personshare.metricsweb.action;

import com.amon.personshare.metricsweb.model.DynamicChartData;
import com.amon.personshare.metricsweb.model.DynamicChartModel;
import com.amon.personshare.metricsweb.service.GetStaticChartJsonService;
import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;

/**
 * Created by 710-6 on 2015/11/17.
 */
public class MetersAction extends ActionSupport {


    private static final long serialVersionUID = 6419719214229203153L;
    private ArrayList<DynamicChartModel> models;//返回给前端的json数据

    private String appname;
    private String keymetrics;

    private int returnNum;     //向前端返回的最新数据
    private String resultTime; //向前端返回的最新数据时间

    /**
     * 获取最新数据的总数和最后处理的时间
     * @return
     */
    public String findReturnDate(){

        GetStaticChartJsonService service=new GetStaticChartJsonService();
        DynamicChartData dynamicChartData=service.getJsonStr_meters(appname, keymetrics, ".main.l1");
        returnNum=dynamicChartData.getCount();
        resultTime=dynamicChartData.getDate();
        return SUCCESS;

    }

    /**
     * 获取最新数据
     * @return
     */
    public String findLeastData(){

        //构造数据
        DynamicChartModel dynamicChartModel = new DynamicChartModel();
        ArrayList<DynamicChartData> main = new ArrayList();
        dynamicChartModel.setxScale("ordinal");
        dynamicChartModel.setComp(new ArrayList());
        dynamicChartModel.setType("bar");
        dynamicChartModel.setyMin(0);
        dynamicChartModel.setyScale("linear");
        dynamicChartModel.setMain(main);

        //将构造的数据置入DynamicChartData的list中
        GetStaticChartJsonService service = new GetStaticChartJsonService();
        DynamicChartData dynamicChartData =
                service.getJsonStr_meters(appname, keymetrics, ".main.l1");

        main.add(dynamicChartData);

        models = new ArrayList();
        models.add(dynamicChartModel);

        //System.out.println(JSONArray.fromObject(models).toString());
        return SUCCESS;

    }

    public ArrayList<DynamicChartModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<DynamicChartModel> models) {
        this.models = models;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public int getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(int returnNum) {
        this.returnNum = returnNum;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getKeymetrics() {
        return keymetrics;
    }

    public void setKeymetrics(String keymetrics) {
        this.keymetrics = keymetrics;
    }

}
