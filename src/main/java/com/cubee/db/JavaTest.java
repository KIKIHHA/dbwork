
package com.cubee.db;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 假设t1,t2两个表结构完全相同，考虑所有情况，每次向备份表插入一万条数据
 */
public class JavaTest {

    public static final Integer num=10000;
    private static Integer sum=null,backupData=0,quantity=null;

    /**
     *
     * @return 根据url,user,password,驱动类型产生的对应的连接
     * @throws SQLException 驱动管理器得到连接中可能出现的异常
     * @throws ClassNotFoundException class的forName方法没有成功加载驱动抛出的异常
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        String URL="jdbc:mysql://localhost:3306/backup?characterEncoding=utf8&useSSL=true&serverTimezone=GMT%2B8";
        String USER="root";
        String PASSWORD="root";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn= DriverManager.getConnection(URL, USER, PASSWORD);
        return conn;
    }

    /**
     *
     * @param backupData 已经过备份的数据条数
     */
    public static void isInserted(Integer backupData){
        if(backupData>0){
            System.out.println("此次备份了"+backupData+" 条数据");
            System.out.println("备份成功");
        }
        else{
            System.out.println("此次备份了"+backupData+" 条数据");
            System.out.println("备份出现异常,复制数据失败");
        }
    }

    /**
     *
     * @param pst 根据已得到的连接来返回相应的预处理类
     * @param rs  根据相关方法得到查询后的结果集
     * @throws SQLException
     */
    public static void  backup(PreparedStatement pst,ResultSet rs) throws SQLException {
        //数据量
        String sql4="select count(*) as sum from t1";
        rs=pst.executeQuery(sql4);
        if(rs.next()) {
            sum = Integer.parseInt(rs.getString("sum"));
            /*System.out.println("待更新的数据量为 " + sum);*/
        }
        //按照每次插入一定量的数据所需的总次数
        quantity=sum%num==0?sum/num:sum/num+1;

        //备份数据
        for(int i=0;i<quantity;i++) {
            String sql = "insert into t2(id,updated_at,other) select * from t1"+" limit "+num*i+","+num;
            backupData+=pst.executeUpdate(sql);
            /*System.out.println("backupData = "+"第"+i+"次" + backupData);*/
        }
        isInserted(backupData);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
        Connection conn=getConnection();
        String sql="select * from t2";
        PreparedStatement pst=conn.prepareStatement(sql);
        ResultSet rs=pst.executeQuery("select * from t2");
        StringBuffer ids=new StringBuffer("(");
        List<Long> list=new ArrayList<Long>();
        Long count=0L;


        //备份表t2不为空
        if(rs.next()){
            //将t1,t2进行对比,思路是重复联合再分组,若是不同的,按字段分组后只有一条记录.相同的有两条记录.该sql是选取已经发生变更的id集合
            String sql3="SELECT  DISTINCT id\n" +
                    "FROM (\n" +
                    "SELECT id, other,updated_at FROM t1\n" +
                    "UNION ALL\n" +
                    "SELECT id,other,updated_at FROM t2\n" +
                    ") tbl\n" +
                    "GROUP BY id, other,updated_at\n" +
                    "HAVING count(*) = 1\n" +
                    "ORDER BY id;";
            rs=pst.executeQuery(sql3);
            while(rs.next()) {
                list.add(Long.parseLong(rs.getString(1)));
            }
            System.out.println("备份表需要删除的数据为 " + list.size()+" 条");
            for(Long id:list){
                if(id!=list.get(list.size()-1))
                ids.append(id+",");
                else
                    ids.append(id+")");
            }

            //若t1表数据发生改变,则在t2表删除相应的已改变的数据
            if(list.size()!=0) {
                System.out.println("需要删除的id集合为 " + ids);
                sum=list.size();
                quantity=sum%num==0?sum/num:sum/num+1;
                String sql4 = "delete from t2 where id in " + ids + ""+" limit "+num+"";
                for(int i=0;i<quantity;i++) {
                    count += pst.executeLargeUpdate(sql4);
                }
                System.out.println("在t2中删除了 " + count+" 条数据");


                //向备份表t2插入t1表中已更新的数据
                count=0L;                  //重置计数器
                for(int i=0;i<quantity;i++) {
                    String sql5="insert into t2  select * from t1 where id in "+ids+" limit "+i*num+","+num;
                    count += pst.executeLargeUpdate(sql5);
                }
                System.out.println("插入了 " + count+" 条数据");
                System.out.println("t2表变更了 " + list.size()+" 条数据");
            }
            else{
                System.out.println("备份表数据完全相同,不需要进行备份");
            }
        }
        //备份表为空，直接备份即可
        else{
            backup(pst,rs);
        }
        rs.close();
        pst.close();
        conn.close();
    }
    }
