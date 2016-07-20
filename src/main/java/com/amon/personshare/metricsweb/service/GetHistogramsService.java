package com.amon.personshare.metricsweb.service;

import com.amon.personshare.metricsweb.dao.MysqlBaseDao;
import com.amon.personshare.metricsweb.model.DynamicChartData;
import com.amon.personshare.metricsweb.model.DynamicChartXYZ2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 710-6 on 2015/11/19.
 */
public class GetHistogramsService {
    private Connection con =null;
    private Statement sm = null;
    private ResultSet rs = null;


    /**
     * 获取当前时间之前的一分钟、五分钟、十五分钟和总的时间的平均处理速率
     * @param appName       监控应用名
     * @param metricskey    监控指标名
     * @param className     数据线前端标示名
     * @return
     */
    public DynamicChartData getJsonStr_histograms(String appName, String metricskey,String className){
        //构造第一条指标的数据
        DynamicChartData dynamicChartData = new DynamicChartData();
        dynamicChartData.setClassName(className);

        MysqlBaseDao dao = new MysqlBaseDao();

        //获取系统当前时间
        Date date=new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.format(date);

        //查询当前时间之前的最后一条数据
        String sql = "select * from histograms ,(select max(tm) maxt from histograms WHERE histograms.tm <= '"+date+"' AND appname = '"+appName+"' AND metricskey = '"+metricskey+"' ) a " +
                "where histograms.tm=a.maxt AND appname = '"+appName+"' AND metricskey = '"+metricskey+"' ";


        try {

            con = dao.getConnection();
            sm =  con.createStatement();
            rs = sm.executeQuery(sql);

            ArrayList<DynamicChartXYZ2> dynamicChartXYZ2List = new ArrayList();

            while (rs.next()){

                DynamicChartXYZ2 dynamicChartXYZ1 = new DynamicChartXYZ2();
                dynamicChartXYZ1.setX("最小值");
                dynamicChartXYZ1.setY(rs.getDouble("min"));
                dynamicChartXYZ2List.add(dynamicChartXYZ1);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ2 = new DynamicChartXYZ2();
                dynamicChartXYZ2.setX("最大值");
                dynamicChartXYZ2.setY(rs.getDouble("max"));
                dynamicChartXYZ2List.add(dynamicChartXYZ2);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ3 = new DynamicChartXYZ2();
                dynamicChartXYZ3.setX("平均值");
                dynamicChartXYZ3.setY(rs.getDouble("mean"));
                dynamicChartXYZ2List.add(dynamicChartXYZ3);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ4 = new DynamicChartXYZ2();
                dynamicChartXYZ4.setX("标准差");
                dynamicChartXYZ4.setY(rs.getDouble("stddev"));
                dynamicChartXYZ2List.add(dynamicChartXYZ4);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ5 = new DynamicChartXYZ2();
                dynamicChartXYZ5.setX("中位数");
                dynamicChartXYZ5.setY(rs.getDouble("median"));
                dynamicChartXYZ2List.add(dynamicChartXYZ5);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ6 = new DynamicChartXYZ2();
                dynamicChartXYZ6.setX("75%");
                dynamicChartXYZ6.setY(rs.getDouble("sevenfive"));
                dynamicChartXYZ2List.add(dynamicChartXYZ6);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ7 = new DynamicChartXYZ2();
                dynamicChartXYZ7.setX("95%");
                dynamicChartXYZ7.setY(rs.getDouble("ninefive"));
                dynamicChartXYZ2List.add(dynamicChartXYZ7);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ8 = new DynamicChartXYZ2();
                dynamicChartXYZ8.setX("98%");
                dynamicChartXYZ8.setY(rs.getDouble("nineeight"));
                dynamicChartXYZ2List.add(dynamicChartXYZ8);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ9 = new DynamicChartXYZ2();
                dynamicChartXYZ9.setX("99%");
                dynamicChartXYZ9.setY(rs.getDouble("ninenine"));
                dynamicChartXYZ2List.add(dynamicChartXYZ9);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ10 = new DynamicChartXYZ2();
                dynamicChartXYZ10.setX("99.9%");
                dynamicChartXYZ10.setY(rs.getDouble("nineninenine"));
                dynamicChartXYZ2List.add(dynamicChartXYZ10);//x,y to data

                dynamicChartData.setCount(rs.getInt("count"));
                dynamicChartData.setDate(sdf.format(rs.getTimestamp("tm")));

            }

            dynamicChartData.setData(dynamicChartXYZ2List);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dao.close(rs,sm,con);
        }

        return dynamicChartData;
    }

    public DynamicChartData getJsonStrByTime_histograms(String appName, String metricskey,String className,String bgTime){

        //构造第一条指标的数据
        DynamicChartData dynamicChartData = new DynamicChartData();
        dynamicChartData.setClassName(className);

        MysqlBaseDao dao = new MysqlBaseDao();

        bgTime+=":00";
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "select * from histograms where tm='"+bgTime+"' AND appname = '"+appName+"' AND metricskey = '"+metricskey+"'";
        System.out.println(sql);

        try {

            con = dao.getConnection();
            sm =  con.createStatement();
            rs = sm.executeQuery(sql);

            ArrayList<DynamicChartXYZ2> dynamicChartXYZ2List = new ArrayList();

            while (rs.next()){

                DynamicChartXYZ2 dynamicChartXYZ1 = new DynamicChartXYZ2();
                dynamicChartXYZ1.setX("最小值");
                dynamicChartXYZ1.setY(rs.getDouble("min"));
                dynamicChartXYZ2List.add(dynamicChartXYZ1);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ2 = new DynamicChartXYZ2();
                dynamicChartXYZ2.setX("最大值");
                dynamicChartXYZ2.setY(rs.getDouble("max"));
                dynamicChartXYZ2List.add(dynamicChartXYZ2);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ3 = new DynamicChartXYZ2();
                dynamicChartXYZ3.setX("平均值");
                dynamicChartXYZ3.setY(rs.getDouble("mean"));
                dynamicChartXYZ2List.add(dynamicChartXYZ3);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ4 = new DynamicChartXYZ2();
                dynamicChartXYZ4.setX("标准差");
                dynamicChartXYZ4.setY(rs.getDouble("stddev"));
                dynamicChartXYZ2List.add(dynamicChartXYZ4);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ5 = new DynamicChartXYZ2();
                dynamicChartXYZ5.setX("中位数");
                dynamicChartXYZ5.setY(rs.getDouble("median"));
                dynamicChartXYZ2List.add(dynamicChartXYZ5);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ6 = new DynamicChartXYZ2();
                dynamicChartXYZ6.setX("75%");
                dynamicChartXYZ6.setY(rs.getDouble("sevenfive"));
                dynamicChartXYZ2List.add(dynamicChartXYZ6);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ7 = new DynamicChartXYZ2();
                dynamicChartXYZ7.setX("95%");
                dynamicChartXYZ7.setY(rs.getDouble("ninefive"));
                dynamicChartXYZ2List.add(dynamicChartXYZ7);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ8 = new DynamicChartXYZ2();
                dynamicChartXYZ8.setX("98%");
                dynamicChartXYZ8.setY(rs.getDouble("nineeight"));
                dynamicChartXYZ2List.add(dynamicChartXYZ8);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ9 = new DynamicChartXYZ2();
                dynamicChartXYZ9.setX("99%");
                dynamicChartXYZ9.setY(rs.getDouble("ninenine"));
                dynamicChartXYZ2List.add(dynamicChartXYZ9);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ10 = new DynamicChartXYZ2();
                dynamicChartXYZ10.setX("99.9%");
                dynamicChartXYZ10.setY(rs.getDouble("nineninenine"));
                dynamicChartXYZ2List.add(dynamicChartXYZ10);//x,y to data

                dynamicChartData.setCount(rs.getInt("count"));
                dynamicChartData.setDate(sdf.format(rs.getTimestamp("tm")));

            }

            dynamicChartData.setData(dynamicChartXYZ2List);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dao.close(rs,sm,con);
        }

        return dynamicChartData;

    }
}
