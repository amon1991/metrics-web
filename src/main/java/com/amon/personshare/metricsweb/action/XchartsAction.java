package com.amon.personshare.metricsweb.action;

import com.alibaba.fastjson.JSON;
import com.amon.personshare.metricsweb.model.DynamicChartData;
import com.amon.personshare.metricsweb.model.DynamicChartModel;
import com.amon.personshare.metricsweb.model.DynamicChartXYZ;
import com.amon.personshare.metricsweb.model.KeyCouple;
import com.amon.personshare.metricsweb.service.GetDynamicChartJsonService;
import com.opensymphony.xwork2.ActionSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 */
public class XchartsAction extends ActionSupport {

    private static final long serialVersionUID = -6185956912317260643L;

    private ArrayList<DynamicChartModel> models;//返回给前端的json数据

    private String bgTime;  //查询的开始时间
    private String endTime; //查询的结束时间
    private int resultNum = 10;  //一次返回的最多数据量

    private String dateRegionType;  //时间段类型   1代表10分钟；2代表30分钟；3代表3小时；4代表1天；5代表1周

    private final int MaxNum = 100;//在前端最多返回的点数
    private final String  TableName = "counter";

    private int returnNum;     //向前端返回的最新数据
    private String resultTime; //向前端返回的最新数据时间

    private String appName;    //应用名称
    private String metricsKey; //指标key

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
     * 返回最新的单条记录
     * @return
     */
    public String findSingleLeastData(){

        GetDynamicChartJsonService service = new GetDynamicChartJsonService();
        DynamicChartXYZ dynamicChartXYZ = service.getSingleLeastData(appName,metricsKey,TableName);

        returnNum = dynamicChartXYZ.getY();
        resultTime = dynamicChartXYZ.getX();

        return SUCCESS;
    }

    /**
     * 根据不同时间段，计算TPS
     * @return
     */
    public String findDataByDateRegion(){

        if (null==dateRegionType)
            dateRegionType = "1";

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nowtime = System.currentTimeMillis();
        endTime = sdf.format(new Date(nowtime));//设置查询的结束时间

        if (dateRegionType.equals("1")){//查询最近10分钟的数据
            bgTime = sdf.format(new Date(nowtime-1000*60*10));
        }else if(dateRegionType.equals("2")){//查询最近30分钟的数据
            bgTime = sdf.format(new Date(nowtime-1000*60*30));
        }else if(dateRegionType.equals("3")){//查询最近3小时的数据
            bgTime = sdf.format(new Date(nowtime-1000*60*60*3));
        }else if(dateRegionType.equals("4")){//查询最近1天的数据
            bgTime = sdf.format(new Date(nowtime-1000*60*60*24));
        }else if(dateRegionType.equals("5")){//查询最近一周的数据
            bgTime = sdf.format(new Date(nowtime-1000*60*60*24*7));
        }


        if (null!=keyCouples&&!keyCouples.isEmpty()){

            ArrayList<DynamicChartData> main = new ArrayList<>();

            this.resetDynamicChartModel();
            this.dynamicChartModel.setMain(main);

            this.resetdynamicChartModel_2();
            this.dynamicChartModel_2.setMain(main);


            GetDynamicChartJsonService service = new GetDynamicChartJsonService();

            DynamicChartData dynamicChartData;
            ArrayList<DynamicChartXYZ> dynamicChartXYZList;


            List<KeyCouple> myKeyCouples = JSON.parseArray(keyCouples,KeyCouple.class);

            for (int i = 1; i <= myKeyCouples.size(); i++) {
                dynamicChartData = new DynamicChartData();
                dynamicChartData.setClassName(".main.l"+i);
                //对获取的数据进行迭代计算，计算出其每秒钟（实际上是最小间隔时间）处理的数据量
                dynamicChartXYZList = service.getxyListByDateRegion(myKeyCouples.get(i-1).getAppname(), myKeyCouples.get(i-1).getMetricskey(), bgTime, endTime, TableName);
                //对数据进行过滤（只最多保留100个数据）
                dynamicChartData.setData(service.getXYListOfNum(dynamicChartXYZList,MaxNum));
                main.add(dynamicChartData);
            }

            //将两段数据组合成一个json数据
            models = new ArrayList<>();
            models.add(dynamicChartModel);
            models.add(dynamicChartModel_2);

        }

        dateRegionType = null;
        return SUCCESS;

    }

    /**
     * 根据时间段选择，查询历史数据
     * @return
     */
    public String findDataByTime(){

        System.out.println("bgTime:"+bgTime+"//endTime:"+endTime+"//size:"+resultNum);

        if (null!=keyCouples&&!keyCouples.isEmpty()){

            ArrayList<DynamicChartData> main = new ArrayList<>();

            this.resetDynamicChartModel();
            this.dynamicChartModel.setMain(main);

            this.resetdynamicChartModel_2();
            this.dynamicChartModel_2.setMain(main);


            // 将构造的数据置入DynamicChartData的list中
            GetDynamicChartJsonService service = new GetDynamicChartJsonService();

            DynamicChartData dynamicChartData;

            List<KeyCouple> myKeyCouples = JSON.parseArray(keyCouples,KeyCouple.class);

            for (int i = 1; i <= myKeyCouples.size(); i++) {

                dynamicChartData = service.getJsonStrByDateRegion_counter(myKeyCouples.get(i-1).getAppname(),myKeyCouples.get(i-1).getMetricskey(),
                        ".main.l"+i,bgTime ,endTime,resultNum,TableName);
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

    /**
     * 查询最新数据
     * @return
     */
    public String findLeastData(){

        if (null!=keyCouples&&!keyCouples.isEmpty()){

            ArrayList<DynamicChartData> main = new ArrayList<>();

            this.resetDynamicChartModel();
            this.dynamicChartModel.setMain(main);

            this.resetdynamicChartModel_2();
            this.dynamicChartModel_2.setMain(main);


            // 将构造的数据置入DynamicChartData的list中
            GetDynamicChartJsonService service = new GetDynamicChartJsonService();

            DynamicChartData dynamicChartData;
            List<KeyCouple> myKeyCouples = JSON.parseArray(keyCouples,KeyCouple.class);

            for (int i = 1; i <= myKeyCouples.size(); i++) {
                dynamicChartData = service.getJsonStrByNum_counter(myKeyCouples.get(i-1).getAppname(),myKeyCouples.get(i-1).getMetricskey()
                        , 10, ".main.l"+i,TableName);
                main.add(dynamicChartData);
            }

            // 将两段数据组合成一个json数据
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

    public String getDateRegionType() {
        return dateRegionType;
    }

    public void setDateRegionType(String dateRegionType) {
        this.dateRegionType = dateRegionType;
    }

    public int getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(int returnNum) {
        this.returnNum = returnNum;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
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

    public int getMaxNum() {
        return MaxNum;
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
