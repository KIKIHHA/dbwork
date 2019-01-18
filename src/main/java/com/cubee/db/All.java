
package com.cubee.db;

import java.sql.*;

/**
 * @program: dbtest
 * @description: 全量备份,此时可以备份有序不连续的数据
 * @author: Charles
 * @create: 2019-01-17 09:56
 **/


public class All {

    public static final Integer NUM=1000;

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        Connection conn=ConnectionUtil.getConnection();
        //取出t2此时最后一条记录
        String sql="select max(id) from t2";
        PreparedStatement pst=conn.prepareStatement(sql);
        ResultSet rs=pst.executeQuery();
        Long count=0L;
        Integer t1maxId;
        //若t2此时已有记录
        if(rs.next()){
            Integer t2maxId=rs.getInt(1);
            System.out.println("t2表最后一条数据id = " + t2maxId);
            //取出此时t1中最后一条记录
            String sql1="select max(id) from t1";
            rs=pst.executeQuery(sql1);
            //若t1此时已有记录
            if(rs.next()){
                t1maxId=rs.getInt(1);
                System.out.println("t1表最后一条数据id = " + t1maxId);
                //往t2表中插入在t2最后一条记录之后t1表还有的数据
                if(t2maxId<t1maxId) {
                    String sql2 = "insert into t2 select * from t1 where id >" + t2maxId + " limit " + NUM;
                    count = pst.executeLargeUpdate(sql2);
                }
                else {
                    System.out.println("备份表t2与t1表数据相同,不需要备份");
                }
            }
            else{
                String sql3="delete from t2";
                count=pst.executeLargeUpdate(sql3);
                System.out.println("t1表数据为空,则删除备份表所有数据,共删除"+count+"数据,完成备份");
            }
            System.out.println("本次备份了"+count+"条记录");
        }
        else{
            String sql4 = "insert into t2 select * from t1  limit+ " + NUM;
            count = pst.executeLargeUpdate(sql4);
            System.out.println("本次备份了"+count+"条记录");
        }
        rs.close();
        pst.close();
        conn.close();
    }
    }
