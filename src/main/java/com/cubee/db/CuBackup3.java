package com.cubee.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @program: dbtest
 * @description: 当t1表发生增加.删除情况下的备份。此时选用第三种方法，选取半小时内发生变更的数据，插入到t2中,若产生sql主键重复异常,则catch并删除相关包含该主键的记录,再插入
 * @author: Charles
 * @create: 2019-01-17 09:56
 **/

public class CuBackup3 {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //得到连接
        Connection conn=ConnectionUtil.getConnection();

        //选取半个小时内发生了变更的，同时于t2表中存在的记录，此时属于修改后的记录,得到id后可以作删除存在于id集合的记录，再从t1表选取出相应记录添加到t2中
        String sql4="select id from t1 where TIME_TO_SEC(TIMEDIFF(CURRENT_TIMESTAMP(),updated_at))<=30*60";
        //修改记录的id的集合
        List<Long> idList=new ArrayList<>();
        //利用StringBuffer拼接sql中in的集合,集合为sql4得到的查询记录
        StringBuffer ids=new StringBuffer("(");
        Long count=0L;
        PreparedStatement pst;
        ResultSet rs;
        String sql6;


        System.out.println("1---------插入");
        pst=conn.prepareStatement(sql4);
        rs=pst.executeQuery();
        ids=ConnectionUtil.operation(rs,ids,idList,0);
        for(int i=0;i<idList.size();i++) {
            try {
                sql6 = "insert into t2 select * from t1 where id =" + idList.get(i);
                count+=pst.executeUpdate(sql6);
            } catch (Exception e) {
                String sql7 = "delete from t2 where id =" + idList.get(i);
                pst.executeLargeUpdate(sql7);
                String sql8 = "insert into t2 select * from t1 where id =" + idList.get(i);
                count+=pst.executeLargeUpdate(sql8);
            }
        }
        System.out.println("往t2表中插入了"+count+"条记录");
        System.out.println("备份完成");
    }
}









