package com.amon.personshare.metricsweb.action;

import com.amon.personshare.metricsweb.model.DynamicChartData;
import com.amon.personshare.metricsweb.model.DynamicChartModel;
import com.amon.personshare.metricsweb.service.GetDynamicChartJsonService;
import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;

/**
 * Created by 710-6 on 2015/11/17.
 */
public class GaugesAction extends ActionSupport {

    private static final long serialVersionUID = 2870787275172658075L;

    private ArrayList<DynamicChartModel> models;//返回给前端的json数据

    private String bgTime;  //查询的开始时间
    private String endTime; //查询的结束时间
    private int resultNum = 10;  //一次返回的最多数据量

    /**
     * 获取最新数据
     * @return
     */
    public String findLeastData(){

        System.out.println("Gauges...");

        //构造第一段数据
        DynamicChartModel dynamicChartModel = new DynamicChartModel();
        ArrayList<DynamicChartData> main = new ArrayList<DynamicChartData>();
        dynamicChartModel.setxScale("ordinal");
        dynamicChartModel.setComp(new ArrayList<String>());
        dynamicChartModel.setType("line-dotted");
        dynamicChartModel.setyScale("linear");
        dynamicChartModel.setMain(main);

        //将构造的数据置入DynamicChartData的list中
        GetDynamicChartJsonService service = new GetDynamicChartJsonService();
        DynamicChartData dynamicChartData_get =
                service.getJsonStrByNum_counter("tsdata", "com.iecas.metrics.mysql.test.GaugesTest.job.size", 10, ".main.l1","gauges");
        DynamicChartData dynamicChartData_put =
                service.getJsonStrByNum_counter("tsdata", "com.iecas.metrics.mysql.test.GaugesTest.job_2.size", 10, ".main.l2","gauges");

        main.add(dynamicChartData_get);
        main.add(dynamicChartData_put);

        //构造第二段数据
        DynamicChartModel dynamicChartModel_2 = new DynamicChartModel();
        dynamicChartModel_2.setxScale("ordinal");
        dynamicChartModel_2.setComp(new ArrayList<String>());
        dynamicChartModel_2.setType("bar");
        dynamicChartModel_2.setyScale("linear");
        dynamicChartModel_2.setMain(main);

        //将两段数据组合成一个json数据
        models = new ArrayList<DynamicChartModel>();
        models.add(dynamicChartModel);
        models.add(dynamicChartModel_2);

        //System.out.println(JSONArray.fromObject(models).toString());

        return SUCCESS;
    }

    public String findDataByTime(){

        //System.out.println("bgTime:"+bgTime+"//endTime:"+endTime+"//size:"+resultNum);

        //构造第一段数据
        DynamicChartModel dynamicChartModel = new DynamicChartModel();
        ArrayList<DynamicChartData> main = new ArrayList<DynamicChartData>();
        dynamicChartModel.setxScale("ordinal");
        dynamicChartModel.setComp(new ArrayList<String>());
        dynamicChartModel.setType("line-dotted");
        dynamicChartModel.setyScale("linear");
        dynamicChartModel.setMain(main);

        //将构造的数据置入DynamicChartData的list中
        GetDynamicChartJsonService service = new GetDynamicChartJsonService();
        DynamicChartData dynamicChartData_get =
                service.getJsonStrByDateRegion_counter("tsdata", "com.iecas.metrics.mysql.test.GaugesTest.job.size",
                        ".main.l1", bgTime ,endTime,resultNum,"gauges");
        DynamicChartData dynamicChartData_put =
                service.getJsonStrByDateRegion_counter("tsdata", "com.iecas.metrics.mysql.test.GaugesTest.job_2.size",
                        ".main.l2", bgTime, endTime,resultNum,"gauges");

        main.add(dynamicChartData_get);
        main.add(dynamicChartData_put);

        //构造第二段数据
        DynamicChartModel dynamicChartModel_2 = new DynamicChartModel();
        dynamicChartModel_2.setxScale("ordinal");
        dynamicChartModel_2.setComp(new ArrayList<String>());
        dynamicChartModel_2.setType("bar");
        dynamicChartModel_2.setyScale("linear");
        dynamicChartModel_2.setMain(main);

        //将两段数据组合成一个json数据
        models = new ArrayList<DynamicChartModel>();
        models.add(dynamicChartModel);
        models.add(dynamicChartModel_2);

        //System.out.println(JSONArray.fromObject(models).toString());

        return SUCCESS;
    }

    public ArrayList<DynamicChartModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<DynamicChartModel> models) {
        this.models = models;
    }

    public String getBgTime() {
        return bgTime;
    }

    public void setBgTime(String bgTime) {
        this.bgTime = bgTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getResultNum() {
        return resultNum;
    }

    public void setResultNum(int resultNum) {
        this.resultNum = resultNum;
    }
}
