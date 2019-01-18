package com.cubee.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 本例讨论增量备份。
 * 暂时有三种方法。
 * 1.选取变更数据，根据不同的sql选取出不同的情况（增加，修改）来分类讨论
 * 2.选取变更数据，从t1表中查出更新时间在半个小时内记录,再从t2表中删除存在这些id的记录,最后把最开始从t1查出的记录插入到t2中
 * 3.选取变更数据, 从t1表中查出更新时间在半个小时内记录,插入到t2中,若产生sql主键重复异常,则catch并删除相关包含该主键的记录,再插入
 */

/**
 * @program: dbtest
 * @description: 当t1表发生增加.删除情况下的备份。此时选用第一种方法，选取半小时内发生变更的数据，进行筛选讨论。只要半个小时数据有过更新，就会备份
 * @author: Charles
 * @create: 2019-01-17 09:56
 **/

public class CuBackup {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //得到连接
        Connection conn=ConnectionUtil.getConnection();
        //选取更新时间在距离现在半个小时之内的所有记录,这句sql适用的情况时每半个小时就会备份一次,所以不会选取到超过半个小时的数据
        String sql2="select * from t1 where id not in (select id from t2)";
        //选取半个小时内t1表新增的数据,并插入到t2中
        String sql3="insert into t2 select * from t1 where id not in (select id from t2)";
        //选取半个小时内发生了变更的，同时于t2表中存在的记录，此时属于修改后的记录,得到id后可以作删除存在于id集合的记录，再从t1表选取出相应记录添加到t2中
        String sql4="select id from t2 where id in (select id from t1 where TIME_TO_SEC(TIMEDIFF(CURRENT_TIMESTAMP(),updated_at))<=30*60)";
        //利用StringBuffer拼接sql中in的集合,集合为sql4得到的查询记录
        //修改记录的id的集合
        List<Long> idList=new ArrayList<>();
        //增加记录的id的集合
        List<Long> idList2=new ArrayList<>();
        StringBuffer ids=new StringBuffer("(");
        Long count=0L;
        PreparedStatement pst;
        ResultSet rs;


        System.out.println("1-----------------------------更新修改的记录");
        //备份更新记录到t2
        pst=conn.prepareStatement(sql4);
        rs=pst.executeQuery();
        //结果为t2表需要更新的数据
        ids=ConnectionUtil.operation(rs,ids,idList,0);
        //从t2表中删除已变更的数据
        String sql5="delete from t2 where id in "+ids;
        //从t1表查询出相应数据，插入到t2表中
        String sql6="insert into t2 select * from t1 where id in "+ids;
        /*System.out.println("sql5 = " + sql5);*/
        if(ids.length()!=1) {
            count = pst.executeLargeUpdate(sql5);
        }
        System.out.println("t2表删除了上述已发生变更的"+count+"条记录");
        if(ids.length()!=1){
            count=pst.executeLargeUpdate(sql6);
        }
        System.out.println("t2表更新了"+count+"条记录"+"\n");


        System.out.println("2-----------------------------更新新增的记录");
        //备份新增记录到t2
        rs=pst.executeQuery(sql2);
        //结果为t2表需要新增的数据
        ids=ConnectionUtil.operation(rs,ids,idList2,1);
        count=pst.executeLargeUpdate(sql3);
        System.out.println("t2表备份了上述增加的"+count+"条记录");
        System.out.println("备份完成");
    }
}









