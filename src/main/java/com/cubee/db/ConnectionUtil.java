package com.cubee.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @program: dbtest
 * @description: 数据库工具类
 * @author: Charles
 * @create: 2019-01-17 09:57
 **/

public class ConnectionUtil {
    /**
     * @return 根据url, user, password, 驱动类型产生的对应的连接
     * @throws SQLException           驱动管理器得到连接中可能出现的异常
     * @throws ClassNotFoundException class的forName方法没有成功加载驱动抛出的异常
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        String URL = "jdbc:mysql://106.75.226.193:7788/db_test?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
        String USER = "lottery";
        String PASSWORD = "2s43r@rOG^3Gewe";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

       /* String URL = "jdbc:mysql://localhost:3306/backup?characterEncoding=utf8&useSSL=true&serverTimezone=GMT%2B8";
        String USER = "root";
        String PASSWORD = "root";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("conn = " + conn);*/
        return conn;
    }

    /**
     * @param list 需要拼接的集合
     * @param ids  拼接后的字符串
     * @return
     */
    public static StringBuffer getString(List<Long> list, StringBuffer ids) {
        ids=new StringBuffer();
        for (Long id : list) {
            if (!id.equals(list.get(list.size() - 1))) {
                ids.append(id + ",");
            } else {
                ids.append(id + ")");
            }
        }
        return ids;

    }

    /**
     *
     * @param ids 用到的id集合
     * @param count 操作步骤
     */
    public static void show(StringBuffer ids,int count){
        if(ids.length()!=1){
            System.out.println("变更记录的id有 " + ids);
        }
        else if(ids.length()==1&&count==0){
            System.out.println("t1表没有纪录发生修改");
        }
        else if(ids.length()==1&&count==1){
            System.out.println("t1表没有纪录发生新增");
        }
    }

    /**
     *
     * @param rs 执行sql得到的结果集
     * @param ids id集合拼接的字符串
     * @param idList id集合
     * @return 最终拼接后的字符串，格式类似（1，2）
     * @throws SQLException rs.next()时可能会出现异常
     */
    public static StringBuffer operation(ResultSet rs,StringBuffer ids,List<Long> idList,int count) throws SQLException {
        ids=new StringBuffer("(");
        while(rs.next()){
            idList.add(Long.parseLong(rs.getString(1)));
        }
        ids.append(ConnectionUtil.getString(idList,ids));
        ConnectionUtil.show(ids,count);
        return ids;
    }
}
