package com.amon.personshare.metricsweb.service;

import com.amon.personshare.metricsweb.dao.MysqlBaseDao;
import com.amon.personshare.metricsweb.model.HealthChecks;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by 710-6 on 2015/11/18.
 */
public class HealthCheckService {

    private Connection con =null;
    private Statement sm = null;
    private ResultSet rs = null;

    /**
     * 查询单条最新的数据
     * @param appName
     * @param metricskey
     * @return
     */
    public HealthChecks getSingleLeastData(String appName,String metricskey){

        HealthChecks result = null;

        MysqlBaseDao dao = new MysqlBaseDao();

        String sql = "select * from healthchecks where appname = '"+appName+"' " +
                "and metricskey = '"+metricskey+"' order by id DESC limit 1";

        try {

            con = dao.getConnection();
            sm =  con.createStatement();
            rs = sm.executeQuery(sql);

            Timestamp ts;
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()){
                result = new HealthChecks();

                ts = rs.getTimestamp("tm");
                result.setTime(sdf.format(ts));
                result.setIsHealth(rs.getInt("ishealth"));
                result.setMessage(rs.getString("message"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dao.close(rs,sm,con);
        }

        return result;
    }
}
