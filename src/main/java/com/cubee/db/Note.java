package com.cubee.db;

/**
 * @program: dbtest
 * @description: 多余的注释
 * @author: Charles
 * @create: 2019-01-18 12:19
 **/

public class Note {
    public static void main(String[] args) {

        /*for(int i=0;i<idList.size();i++) {
        try {
        sql6 = "insert into t2 select * from t1 where id =" + idList.get(i);
        count+=pst.executeUpdate(sql6);
        } catch (Exception e) {
        String msg = e.getMessage();
        String[] strs = msg.split(" ");
        String[] strz = strs[2].split("'");
        Long id = Long.parseLong(strz[1]);
        *//*System.out.println(strz[1]);*//*
        String sql7 = "delete from t2 where id =" + id;
        pst.executeLargeUpdate(sql7);
        String sql8 = "insert into t2 select * from t1 where id =" + id;
        count+=pst.executeLargeUpdate(sql8);
        }
        }*/

        /* public static final Integer num=2;
    private static Integer sum=null,backupData=0,quantity=null;

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

    public static void  backup(PreparedStatement pst,ResultSet rs) throws SQLException {
        //数据量
        String sql4="select count(*) as sum from t1";
        rs=pst.executeQuery(sql4);
        if(rs.next()) {
            sum = Integer.parseInt(rs.getString("sum"));
            *//*System.out.println("待更新的数据量为 " + sum);*//*
        }
        //按照每次插入一定量的数据所需的总次数
        quantity=sum%num==0?sum/num:sum/num+1;

        //备份数据
        for(int i=0;i<quantity;i++) {
            String sql = "insert into t2(id,updated_at,other) select * from t1"+" limit "+num*i+","+num;
            backupData+=pst.executeUpdate(sql);
            *//*System.out.println("backupData = "+"第"+i+"次" + backupData);*//*
        }
        isInserted(backupData);
    }*/


        /*String sql3="select updated_at time from t1 where updated_at>(select max(updated_at) from t2)";
        ResultSet rs=pst.executeQuery(sql3);
        if(rs.next()) {
            String time = rs.getString(1);
            System.out.println("time = " + time);
        }*/
        /*while(rs.next()){
            System.out.println(rs.getString("id")+" "
                    +rs.getString("other"));
        }*/
//关闭资源

         /*String sql3="select count(*) as sum from t1 where updated_at>(select max(updated_at) from t2)";
                ResultSet rs=pst.executeQuery(sql3);
                if(Integer.parseInt(rs.getString("sum"))<=2){
                    String sql4="insert into t2(id,updated_at,other) select * from t1 where updated_at>(select max(updated_at) from t2)";
                    System.out.println("备份成功");
                }
                else{

                }*/






            /*System.out.println("若备份表t2已有备份数据");
            String sql2="delete from t2";
            System.out.println("删除t2所有数据");
            count=pst.executeLargeUpdate(sql2);
            if(count>0){
                System.out.println("删除成功");
                backup(pst,rs);
            }
            else{
                System.out.println("备份出现异常，删除备份表已更新的数据失败,可能是两张表数据一样");
            }*/



        /* 4..从反面，当双表备份时，只有当id相等，其他字段相同的记录不需要备份，其他度需要备份，将完全相同的记录筛选出来作为集合A，则A的绝对补集就是需要备份的记录*/
    }
}
