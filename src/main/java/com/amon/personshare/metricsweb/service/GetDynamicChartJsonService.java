package com.amon.personshare.metricsweb.service;

import com.amon.personshare.metricsweb.dao.MysqlBaseDao;
import com.amon.personshare.metricsweb.model.DynamicChartData;
import com.amon.personshare.metricsweb.model.DynamicChartModel;
import com.amon.personshare.metricsweb.model.DynamicChartXYZ;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * 获取向前端传回的json串的方法
 * Created by Administrator on 2015/11/10.
 */
public class GetDynamicChartJsonService {

    private Connection con =null;
    private Statement sm = null;
    private ResultSet rs = null;


    /**
     * 将结果列表中每个实际值y从累加值变为瞬时值（TPS）
     * @param list
     */
    public void setXYListOfTPS(ArrayList<DynamicChartXYZ> list){

        if (null!=list){

            int listSize = list.size();
            int bigdata;
            int smalldata;
            for (int i = (listSize-1); i > 0 ; i--) {//循环n-1次进行相应设置

                bigdata = list.get(i).getY();
                smalldata = list.get(i-1).getY();
                list.get(i).setY(bigdata-smalldata);
            }

            if (listSize>0){
                list.remove(0);//将列表中第一个元素去除，因为这个数据没有经过计算
            }

        }
    }

    /**
     * 最多只取出maxNum的结果进行统计
     * @param list
     */
    public ArrayList<DynamicChartXYZ> getXYListOfNum(ArrayList<DynamicChartXYZ> list,int maxNum){

        ArrayList<DynamicChartXYZ> resultList = new ArrayList<DynamicChartXYZ>();

        if (null!=list){
            int listSize = list.size();
            if (listSize<=maxNum)
            {
                resultList = list;
            }
            else {

                int jiange = listSize/maxNum +1;//间隔量
                int index = 0;

                for (int i = 0; i < listSize; i++) {

                    if (index%jiange==0){//根据间隔置入数据
                        resultList.add(list.get(i));
                    }
                    index++;
                }
            }
        }

        return resultList;
    }

    /**
     * 查询单条最新的数据
     * @param appName
     * @param metricskey
     * @param tableName
     * @return
     */
    public DynamicChartXYZ getSingleLeastData(String appName,String metricskey,String tableName){

        DynamicChartXYZ dynamicChartXYZ = null;

        MysqlBaseDao dao = new MysqlBaseDao();

        String sql = "select * from "+tableName+" where appname = '"+appName+"' " +
                "and metricskey = '"+metricskey+"' order by id DESC limit 1";

        try {

            con = dao.getConnection();
            sm =  con.createStatement();
            rs = sm.executeQuery(sql);

            Timestamp ts;
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()){
                dynamicChartXYZ = new DynamicChartXYZ();
                ts = rs.getTimestamp("tm");
                dynamicChartXYZ.setX(sdf.format(ts));//简单的日期格式化操作
                dynamicChartXYZ.setY(rs.getInt("value"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dao.close(rs,sm,con);
        }

        return dynamicChartXYZ;
    }

    /**
     * 根据日期范围获取数据
     * @param appName     监控应用名
     * @param metricskey  监控指标名
     * @param bgTime      查询的开始时间
     * @param endTime     查询的结束时间
     * @return
     */
    public ArrayList<DynamicChartXYZ> getxyListByDateRegion(String appName, String metricskey,String bgTime,String endTime,String tableName){

        //构造第一条指标的数据
        ArrayList<DynamicChartXYZ> dynamicChartXYZList = new ArrayList<DynamicChartXYZ>();

        MysqlBaseDao dao = new MysqlBaseDao();

        String sql = "select * from "+tableName+" where appname = '"+appName+"' " +
                "and metricskey = '"+metricskey+"' and tm >= '"+bgTime+"' and tm <= '"+endTime+"'";

        try {

            con = dao.getConnection();
            sm =  con.createStatement();
            rs = sm.executeQuery(sql);

            DynamicChartXYZ dynamicChartXYZ;
            Timestamp ts;
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()){
                    dynamicChartXYZ = new DynamicChartXYZ();
                    ts = rs.getTimestamp("tm");
                    dynamicChartXYZ.setX(sdf.format(ts).replace(" ", "T"));//简单的日期格式化操作
                    dynamicChartXYZ.setY(rs.getInt("value"));
                    dynamicChartXYZList.add(dynamicChartXYZ);//x,y to data
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dao.close(rs,sm,con);
        }

        return dynamicChartXYZList;

    }

    /**
     * 根据日期范围获取数据
     * @param appName     监控应用名
     * @param metricskey  监控指标名
     * @param className   数据线前端标示名
     * @param bgTime      查询的开始时间
     * @param endTime     查询的结束时间
     * @param resultNum   可返回的最大数据量
     * @return
     */
    public DynamicChartData getJsonStrByDateRegion_counter(String appName, String metricskey,String className,String bgTime,String endTime,int resultNum,String tableName){

        //构造第一条指标的数据
        DynamicChartData dynamicChartData = new DynamicChartData();
        dynamicChartData.setClassName(className);

        MysqlBaseDao dao = new MysqlBaseDao();

        String sql = "select * from "+tableName+" where appname = '"+appName+"' " +
                "and metricskey = '"+metricskey+"' and tm >= '"+bgTime+"' and tm <= '"+endTime+"'";

        try {

            con = dao.getConnection();
            sm =  con.createStatement();
           rs = sm.executeQuery(sql);

            ArrayList<DynamicChartXYZ> dynamicChartXYZList = new ArrayList();

            DynamicChartXYZ dynamicChartXYZ;
            Timestamp ts;
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            rs.last();  //调用方法将指针放到最后
            int length = rs.getRow();//从数据库中取出的结果数
            int getResultIndex = 1;  //隔多少个数据取出结果

            if (length > resultNum){

                getResultIndex = length/resultNum +1; //设置间隔数
            }

            rs.beforeFirst();  //将指针拨回到最初始的位置

            int index = 1;//计数器

            while (rs.next()){

                if (index%getResultIndex==0) {//每间隔getResultIndex次可加入结果集
                    dynamicChartXYZ = new DynamicChartXYZ();

                    ts = rs.getTimestamp("tm");
                    dynamicChartXYZ.setX(sdf.format(ts).replace(" ", "T"));//简单的日期格式化操作
                    dynamicChartXYZ.setY(rs.getInt("value"));

                    dynamicChartXYZList.add(dynamicChartXYZ);//x,y to data
                }

                index++;//计数器+1

            }

            dynamicChartData.setData(dynamicChartXYZList);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dao.close(rs,sm,con);
        }

        return dynamicChartData;

    }

    /**
     * 指定获取对应条件下最新的n条数据
     * @param appName     监控应用名
     * @param metricskey  监控指标名
     * @param num         获取的最新数据数量
     * @param className   数据线前端标示名
     * @return
     */
    public DynamicChartData getJsonStrByNum_counter(String appName, String metricskey,int num,String className,String tableName){

        //构造第一条指标的数据
        DynamicChartData dynamicChartData = new DynamicChartData();
        dynamicChartData.setClassName(className);

        MysqlBaseDao dao = new MysqlBaseDao();

        String sql = "select * from "+tableName+" where appname = '"+appName+"' " +
                "and metricskey = '"+metricskey+"' order by id desc limit "+num;

        try {

            con = dao.getConnection();
            sm =  con.createStatement();
            rs = sm.executeQuery(sql);

            ArrayList<DynamicChartXYZ> dynamicChartXYZList = new ArrayList<DynamicChartXYZ>();

            DynamicChartXYZ dynamicChartXYZ;
            Timestamp ts;
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()){

                dynamicChartXYZ = new DynamicChartXYZ();

                ts = rs.getTimestamp("tm");
                dynamicChartXYZ.setX(sdf.format(ts).replace(" ","T"));//简单的日期格式化操作
                dynamicChartXYZ.setY(rs.getInt("value"));

                dynamicChartXYZList.add(dynamicChartXYZ);//x,y to data

            }

            dynamicChartData.setData(dynamicChartXYZList);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dao.close(rs,sm,con);
        }

        return dynamicChartData;

    }


    public static void main(String[] args) {

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
                service.getJsonStrByDateRegion_counter("tsdata", "com.iecas.metrics.mysql.test.CounterTest.pedding-jobs", ".main.l1", "2015-11-12 14:44:37", "2015-11-17 14:44:52", 10, "counter");
        DynamicChartData dynamicChartData_put =
                service.getJsonStrByDateRegion_counter("tsdata", "com.iecas.metrics.mysql.test.CounterTest.pedding-jobs_2", ".main.l2", "2015-11-12 14:25:18","2015-11-17 14:26:20",10,"counter");

        main.add(dynamicChartData_get);
        main.add(dynamicChartData_put);

        //构造第二段数据
        DynamicChartModel dynamicChartModel_2 = new DynamicChartModel();
        ArrayList<DynamicChartData> main_2 = new ArrayList<DynamicChartData>();
        dynamicChartModel_2.setxScale("ordinal");
        dynamicChartModel_2.setComp(new ArrayList<String>());
        dynamicChartModel_2.setType("bar");
        dynamicChartModel_2.setyScale("linear");
        dynamicChartModel_2.setMain(main);

        //将构造的数据置入DynamicChartData的list中
        DynamicChartData dynamicChartData_get_2 =
                service.getJsonStrByDateRegion_counter("tsdata", "com.iecas.metrics.mysql.test.CounterTest.pedding-jobs", ".main.l1", "2015-11-12 14:44:37", "2015-11-17 14:44:52",10,"counter");
        DynamicChartData dynamicChartData_put_2 =
                service.getJsonStrByDateRegion_counter("tsdata", "com.iecas.metrics.mysql.test.CounterTest.pedding-jobs_2", ".main.l2", "2015-11-12 14:25:18","2015-11-17 14:26:20",10,"counter");

        main_2.add(dynamicChartData_get_2);
        main_2.add(dynamicChartData_put_2);

        //将两段数据组合成一个json数据
        ArrayList<DynamicChartModel> models = new ArrayList<DynamicChartModel>();
        models.add(dynamicChartModel);
        models.add(dynamicChartModel_2);

        //System.out.println(JSONArray.fromObject(models).toString());
    }

}
