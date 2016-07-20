package com.amon.personshare.metricsweb.dao;

import java.sql.*;

/**
 * Created by Administrator on 2015/11/10.
 */
public class MysqlBaseDao {

    private static final long serialVersionUID = 1L;
    //连接驱动
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    //连接路劲
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/metrics";
    //用户名
    private static final String USERNAME = "root";
    //密码
    private static final String PASSWORD = "123456";
    //静态代码块
    static {
        try {
            // 加载驱动
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * 获取数据库连接
     */
    public Connection getConnection() {
        Connection conn = null;
        try{
            conn= DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return conn;
    }

    /*
     * 关闭数据库连接，注意关闭的顺序
     */
    public void close(ResultSet rs, Statement ps, Connection conn) {
        if(rs!=null){
            try{
                rs.close();
                rs=null;
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(ps!=null){
            try{
                ps.close();
                ps=null;
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try{
                conn.close();
                conn=null;
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

}
