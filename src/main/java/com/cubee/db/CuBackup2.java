package com.cubee.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @program: dbtest
 * @description: 当t1表发生增加.删除情况下的备份。此时选用第二种方法，选取半小时内发生变更的数据，直接删除变更的数据，再从t1表查出相应数据插入到t2
 * @author: Charles
 * @create: 2019-01-17 09:56
 **/

public class CuBackup2 {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //得到连接
        Connection conn=ConnectionUtil.getConnection();
        //选取半个小时内发生了变更的，同时于t2表中存在的记录，此时属于修改后的记录,得到id后可以作删除存在于id集合的记录，再从t1表选取出相应记录添加到t2中
        String sql4="select id from t1 where TIME_TO_SEC(TIMEDIFF(CURRENT_TIMESTAMP(),updated_at))<=30*60";
        //利用StringBuffer拼接sql中in的集合,集合为sql4得到的查询记录
        //修改记录的id的集合
        List<Long> idList=new ArrayList<>();
        //增加记录的id的集合
        StringBuffer ids=new StringBuffer("(");
        Long count=0L;
        PreparedStatement pst;
        ResultSet rs;

        System.out.println("1---------删除");
        pst=conn.prepareStatement(sql4);
        rs=pst.executeQuery();
        ids=ConnectionUtil.operation(rs,ids,idList,0);
        /*不管是新增还是修改,只要更新时间在半小时之内的,从t1中选出来,再从t2中删除相应的,
         如果有记录存在于t1,而t2中没有,则从t2删除此记录不会影响行数*/
        String sql5="delete from t2 where id in "+ids;
        if(ids.length()!=1) {
            count = pst.executeLargeUpdate(sql5);
        }
        System.out.println("t2表删除了上述已发生过变化的,存在于t2表的"+count+"条记录");

        System.out.println("2---------插入");
        //从t1中选取相关记录插入到t2中,添加的记录行数<=删除的记录行数
        String sql6="insert into t2 select * from t1 where id in "+ids;
        if(ids.length()!=1) {
            count=pst.executeLargeUpdate(sql6);
        }
        System.out.println("往t2表中插入了"+count+"条记录");
        System.out.println("备份完成");
    }
}
