package com.amon.personshare.metricsweb.action;

import com.alibaba.fastjson.JSON;
import com.amon.personshare.metricsweb.model.DynamicChartData;
import com.amon.personshare.metricsweb.model.DynamicChartModel;
import com.amon.personshare.metricsweb.model.KeyCouple;
import com.amon.personshare.metricsweb.service.GetDynamicChartJsonService;
import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 710-6 on 2015/11/17.
 */
public class GaugesAction extends ActionSupport {

    private static final long serialVersionUID = 2870787275172658075L;

    private ArrayList<DynamicChartModel> models;//返回给前端的json数据

    private String bgTime;  //查询的开始时间
    private String endTime; //查询的结束时间
    private int resultNum = 10;  //一次返回的最多数据量

    private final String  TableName = "gauges";
    private String keyCouples; // keyCouple数组（json数组形式，在后台decode）


    private DynamicChartModel dynamicChartModel; // 构造线性图
    private DynamicChartModel dynamicChartModel_2; // 构造柱状图

    /**
     * 重置线型模型
     */
    private void resetDynamicChartModel(){

        this.dynamicChartModel = new DynamicChartModel();
        this.dynamicChartModel.setxScale("ordinal");
        this.dynamicChartModel.setComp(new ArrayList<String>());
        this.dynamicChartModel.setType("line-dotted");
        this.dynamicChartModel.setyScale("linear");

    }

    /**
     * 重置柱型模型
     */
    private void resetdynamicChartModel_2(){

        dynamicChartModel_2 = new DynamicChartModel();
        dynamicChartModel_2.setxScale("ordinal");
        dynamicChartModel_2.setComp(new ArrayList<String>());
        dynamicChartModel_2.setType("bar");
        dynamicChartModel_2.setyScale("linear");

    }

    /**
     * 获取最新数据
     * @return
     */
    public String findLeastData(){

        if (null!=keyCouples&&!keyCouples.isEmpty()){

            ArrayList<DynamicChartData> main = new ArrayList<>();

            this.resetDynamicChartModel();
            this.dynamicChartModel.setMain(main);

            this.resetdynamicChartModel_2();
            this.dynamicChartModel_2.setMain(main);

            //将构造的数据置入DynamicChartData的list中
            GetDynamicChartJsonService service = new GetDynamicChartJsonService();

            DynamicChartData dynamicChartData;

            List<KeyCouple> myKeyCouples = JSON.parseArray(keyCouples, KeyCouple.class);

            for (int i = 1; i <= myKeyCouples.size(); i++) {
                dynamicChartData = service.getJsonStrByNum_counter(myKeyCouples.get(i-1).getAppname(), myKeyCouples.get(i-1).getMetricskey(), 10, ".main.l"+i,TableName);
                main.add(dynamicChartData);
            }

            //将两段数据组合成一个json数据
            models = new ArrayList<>();
            models.add(this.dynamicChartModel);
            models.add(this.dynamicChartModel_2);

        }

        return SUCCESS;
    }

    public String findDataByTime(){

        //System.out.println("bgTime:"+bgTime+"//endTime:"+endTime+"//size:"+resultNum);

        if (null!=keyCouples&&!keyCouples.isEmpty()){

            ArrayList<DynamicChartData> main = new ArrayList<>();

            this.resetDynamicChartModel();
            this.dynamicChartModel.setMain(main);

            this.resetdynamicChartModel_2();
            this.dynamicChartModel_2.setMain(main);

            //将构造的数据置入DynamicChartData的list中
            GetDynamicChartJsonService service = new GetDynamicChartJsonService();

            List<KeyCouple> myKeyCouples = JSON.parseArray(keyCouples, KeyCouple.class);
            DynamicChartData dynamicChartData;
            for (int i = 1; i <= myKeyCouples.size(); i++) {
                dynamicChartData  =service.getJsonStrByDateRegion_counter(myKeyCouples.get(i-1).getAppname(), myKeyCouples.get(i-1).getMetricskey(),
                                ".main.l"+i, bgTime ,endTime,resultNum,TableName);
                main.add(dynamicChartData);
            }

            //将两段数据组合成一个json数据
            models = new ArrayList<>();
            models.add(dynamicChartModel);
            models.add(dynamicChartModel_2);

        }

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

    public String getTableName() {
        return TableName;
    }

    public String getKeyCouples() {
        return keyCouples;
    }

    public void setKeyCouples(String keyCouples) {
        this.keyCouples = keyCouples;
    }
}
