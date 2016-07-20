package com.amon.personshare.metricsweb.action;

import com.amon.personshare.metricsweb.model.DynamicChartData;
import com.amon.personshare.metricsweb.model.DynamicChartModel;
import com.amon.personshare.metricsweb.model.DynamicChartXYZ;
import com.amon.personshare.metricsweb.service.GetDynamicChartJsonService;
import com.opensymphony.xwork2.ActionSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        if (dateRegionType==null)
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

        //构造第一段数据
        DynamicChartData dynamicChartData_get = new DynamicChartData();
        dynamicChartData_get.setClassName(".main.l1");
        GetDynamicChartJsonService service = new GetDynamicChartJsonService();
        //最关键步骤，获取数据
        ArrayList<DynamicChartXYZ> dynamicChartXYZList = service.
                getxyListByDateRegion("tsdata", "com.amon.personshare.metrics_mysql.CounterTest.pedding-jobs_1", bgTime, endTime, TableName);
        service.setXYListOfTPS(dynamicChartXYZList);//对获取的数据进行迭代计算，计算出其每秒钟（实际上是最小间隔时间）处理的数据量
        dynamicChartData_get.setData(service.getXYListOfNum(dynamicChartXYZList,MaxNum));//对数据进行过滤（只最多保留100个数据）

        DynamicChartData dynamicChartData_put = new DynamicChartData();
        dynamicChartData_put.setClassName(".main.l2");
        ArrayList<DynamicChartXYZ> dynamicChartXYZList_2 = service.
                getxyListByDateRegion("tsdata", "com.amon.personshare.metrics_mysql.CounterTest.pedding-jobs_2", bgTime, endTime,TableName);
        service.setXYListOfTPS(dynamicChartXYZList_2);
        dynamicChartData_put.setData(service.getXYListOfNum(dynamicChartXYZList_2,MaxNum));

        DynamicChartModel dynamicChartModel = new DynamicChartModel();
        ArrayList<DynamicChartData> main = new ArrayList<DynamicChartData>();
        dynamicChartModel.setxScale("ordinal");
        dynamicChartModel.setComp(new ArrayList<String>());
        dynamicChartModel.setType("line");
        dynamicChartModel.setyScale("linear");
        dynamicChartModel.setMain(main);

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
        models = new ArrayList<>();
        models.add(dynamicChartModel);
        models.add(dynamicChartModel_2);

        //System.out.println(JSONArray.fromObject(models).toString());

        dateRegionType = null;
        return SUCCESS;

    }

    /**
     * 根据时间段选择，查询历史数据
     * @return
     */
    public String findDataByTime(){

        System.out.println("bgTime:"+bgTime+"//endTime:"+endTime+"//size:"+resultNum);

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
                service.getJsonStrByDateRegion_counter("tsdata", "com.amon.personshare.metrics_mysql.CounterTest.pedding-jobs_1",
                        ".main.l1", bgTime ,endTime,resultNum,TableName);
        DynamicChartData dynamicChartData_put =
                service.getJsonStrByDateRegion_counter("tsdata", "com.amon.personshare.metrics_mysql.CounterTest.pedding-jobs_2",
                        ".main.l2", bgTime, endTime,resultNum,TableName);

        DynamicChartData dynamicChartData_three =
                service.getJsonStrByDateRegion_counter("tsdata", "com.amon.personshare.metrics_mysql.CounterTest.pedding-jobs_3",
                        ".main.l3", bgTime, endTime,resultNum,TableName);

        main.add(dynamicChartData_get);
        main.add(dynamicChartData_put);
        main.add(dynamicChartData_three);


        //构造第二段数据
        DynamicChartModel dynamicChartModel_2 = new DynamicChartModel();
        dynamicChartModel_2.setxScale("ordinal");
        dynamicChartModel_2.setComp(new ArrayList<String>());
        dynamicChartModel_2.setType("bar");
        dynamicChartModel_2.setyScale("linear");
        dynamicChartModel_2.setMain(main);

        //将两段数据组合成一个json数据
        models = new ArrayList<>();
        models.add(dynamicChartModel);
        models.add(dynamicChartModel_2);

        //System.out.println(JSONArray.fromObject(models).toString());

        return SUCCESS;
    }

    /**
     * 查询最新数据
     * @return
     */
    public String findLeastData(){

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
                service.getJsonStrByNum_counter("tsdata", "com.amon.personshare.metrics_mysql.CounterTest.pedding-jobs_1", 10, ".main.l1",TableName);
        DynamicChartData dynamicChartData_put =
                service.getJsonStrByNum_counter("tsdata", "com.amon.personshare.metrics_mysql.CounterTest.pedding-jobs_2", 10, ".main.l2",TableName);

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
        models = new ArrayList<>();
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
}
