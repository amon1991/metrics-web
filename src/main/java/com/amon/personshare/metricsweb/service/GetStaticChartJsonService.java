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
 * Created by 710-6 on 2015/11/18.
 */
public class GetStaticChartJsonService {

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
    public DynamicChartData getJsonStr_meters(String appName, String metricskey,String className){
        //构造第一条指标的数据
        DynamicChartData dynamicChartData = new DynamicChartData();
        dynamicChartData.setClassName(className);

        MysqlBaseDao dao = new MysqlBaseDao();

        //获取系统当前时间
        Date date=new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.format(date);

        //查询当前时间之前的最后一条数据
        String sql = "select * from meters ,(select max(tm) maxt from meters WHERE meters.tm <= '"+date+"' AND appname = '"+appName+"' AND metricskey = '"+metricskey+"' ) a " +
                "where meters.tm=a.maxt AND appname = '"+appName+"' AND metricskey = '"+metricskey+"' ";

        try {

            con = dao.getConnection();
            sm =  con.createStatement();
            rs = sm.executeQuery(sql);

            ArrayList<DynamicChartXYZ2> dynamicChartXYZ2List = new ArrayList();

            while (rs.next()){

                DynamicChartXYZ2 dynamicChartXYZ1 = new DynamicChartXYZ2();
                dynamicChartXYZ1.setX("TPS");
                dynamicChartXYZ1.setY(rs.getDouble("meanrate"));
                dynamicChartXYZ2List.add(dynamicChartXYZ1);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ2 = new DynamicChartXYZ2();
                dynamicChartXYZ2.setX("TPS(一分钟)");
                dynamicChartXYZ2.setY(rs.getDouble("onemrate"));
                dynamicChartXYZ2List.add(dynamicChartXYZ2);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ3 = new DynamicChartXYZ2();
                dynamicChartXYZ3.setX("TPS(五分钟)");
                dynamicChartXYZ3.setY(rs.getDouble("fivemrate"));
                dynamicChartXYZ2List.add(dynamicChartXYZ3);//x,y to data

                DynamicChartXYZ2 dynamicChartXYZ4 = new DynamicChartXYZ2();
                dynamicChartXYZ4.setX("TPS(十五分钟)");
                dynamicChartXYZ4.setY(rs.getDouble("fifmrate"));
                dynamicChartXYZ2List.add(dynamicChartXYZ4);//x,y to data

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
