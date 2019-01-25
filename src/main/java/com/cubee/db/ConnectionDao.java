package com.cubee.db;

import com.cubee.db.BeanFactory;

import java.sql.*;

/**
 * @program: dbtest
 * @description: 数据库连接接口
 * @author: Charles
 * @create: 2019-01-23 17:15
 **/

public interface ConnectionDao {
    /**
     *
     * @param path 读取的配置文件的路径
     * @return 得到对应的数据库连接
     */
    static Connection getConnection(String path){
        String url=(String) BeanFactory.getBean("url",path);
        String pwd=(String) BeanFactory.getBean("pwd",path);
        String driver=(String) BeanFactory.getBean("driver",path);
        String user=(String) BeanFactory.getBean("user",path);
        Connection conn=null;
        try {
            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     *
     * @param o 需要调用close方法的对象
     */
    static void operate(Object o) {
        String pstClass = "ClientPreparedStatement";
        String connClass = "ConnectionImpl";
        String resultClass = "ResultSetImpl";
        try{
            if(o!=null){
                if(o.getClass().getSimpleName().equals(pstClass)){
                    PreparedStatement pst= (PreparedStatement) o;
                    pst.close();
                }
                if(o.getClass().getSimpleName().equals(connClass)){
                    Connection conn= (Connection) o;
                    conn.commit();
                    conn.close();
                }
                if(o.getClass().getSimpleName().equals(resultClass)){
                    ResultSet rs= (ResultSet) o;
                    rs.close();
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
