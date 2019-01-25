
package com.cubee.db;

import java.sql.*;

/**
 * @program: dbtest
 * @description: 全量备份,此时可以备份有序不连续的数据
 * @author: Charles
 * @create: 2019-01-17 09:56
 **/

/**
 * 1.数据表不同库,多数据库之间的交换   sql,conn  抽象工厂
 * 2.备份不是同步    只要复制规定的记录集合   配置文件读取参数
 * 3.资源释放    资源最后的关闭和rs重复资源的释放  finally.引用类型
 * 4.断点同步,就算上一步没有完成,再次执行,依然可以成功 把断点记录在配置文件中
 * 5.多样化的应用场景      通过事务控制,异常来设计
 */

public class All {

    public static final Integer ONCE_COUNT = Integer.parseInt((String) BeanFactory.getBean("onceCount"));

    public static void main(String[] args) throws SQLException {
        //上次备份中，备份的节点，若备份未中断，默认为null
        Integer lastCount = null, count = 0;
        //赋值为最近一次更新的id
        Long t2maxId;
        //str为用字符串保存上次备份节点
        String sql, sql2, sql3, str;
        PreparedStatement pst1 = null, pst2 = null;
        ResultSet rs1 = null, rs2 = null;
        Connection conn1 = null, conn2 = null;
        try {
            //关于表t2的数据库连接
            conn2 = ConnectionDao.getConnection("connection1.properties");
            //关于表t1的数据库连接
            conn1 = ConnectionDao.getConnection("connection2.properties");
            //选出最近的一次更新的id
            sql = "select max(id) from t2";
            conn2.setAutoCommit(false);
            //conn连接下的预处理语句和结果集
            pst2 = conn2.prepareStatement(sql);
            rs2 = pst2.executeQuery();

            str = (String) BeanFactory.getBean("lastCount");
            //节点不为null,表示上次未备份完全，则把节点转化为Integer类型
            if (!str.equals("null")) {
                lastCount = Integer.parseInt(str);
            }
            //若t2此时没有记录，令t2maxId为0
            if (rs2.next()) {
                t2maxId = rs2.getLong(1);
            } else {
                t2maxId = 0L;
            }
            System.out.println("t2表最后一条数据id = " + t2maxId);
            //往t2表中插入在t2最后一条记录之后t1表还有的数据
            sql2 = "select * from t1 where id>" + t2maxId + " limit " + ONCE_COUNT;

            //conn2连接下的预处理语句和结果集
            pst1 = conn1.prepareStatement(sql2);
            rs1 = pst1.executeQuery();

            //将备份的记录用表格记录，此时为表的标题
            System.out.println("备份的记录为");
            System.out.printf("%-12s\t", "ID");
            System.out.printf("%-28s\t", "UPDATED_AT");
            System.out.printf("%-12s\t", "OTHER");
            System.out.println();

            //遍历此时需要备份的记录集
            while (rs1.next()) {
                //将备份的记录用表格记录，此时为表数据
                System.out.printf("%-12s\t", rs1.getLong(1));
                System.out.printf("%-30s\t", rs1.getTimestamp(2));
                System.out.printf("%-12s\t", rs1.getLong(3));
                System.out.println();

                sql3 = "insert into t2 values (?,?,?)";
                pst2 = conn2.prepareStatement(sql3);
                pst2.setLong(1, rs1.getLong(1));
                pst2.setTimestamp(2, rs1.getTimestamp(2));
                pst2.setLong(3, rs1.getLong(3));
                count+=pst2.executeUpdate();
                /*if(count==500){
                    doSomething();
                }*/
                //若备份点不为空，备份上次未完成记录
                if (lastCount != null && count == ONCE_COUNT - lastCount) {
                    BeanFactory.setBean("lastCount", "null");
                    System.out.println("完成上次备份");
                    break;
                }
            }
            System.out.println("本次共备份了" + count + "条记录");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发生意外情况,本次备份中断,共备份了" + count + "条记录");

            //通过修改配置文件的参数的方式来保存当时备份的节点
            BeanFactory.setBean("lastCount", String.valueOf(count));
        } finally {
            if (rs1 != null) {
                rs1.close();
            }
            if (rs2 != null) {
                rs2.close();
            }
            if (conn1 != null) {
                conn1.close();
            }
            if (conn2 != null) {
                conn2.commit();
                conn2.close();
            }
            if (pst1 != null) {
                pst1.close();
            }
            if (pst2 != null) {
                pst2.close();
            }
        }
    }

    public static void doSomething() throws Exception {
        throw new Exception("发生了一些情况");
    }

    public static void operate(Object o) {
        /*System.out.println(o.getClass());
        System.out.println(o.getClass().toString());
        System.out.println(o.getClass().getSimpleName());*/
        String pstClass = "ClientPreparedStatement";
        String connClass = "ConnectionImpl";
        String resultClass = "ResultSetImpl";
       /* try{
            if(o.getClass().getTypeName().compareToIgnoreCase(pstClass)==0){
                PreparedStatement pst= (PreparedStatement) o;
                if(pst!=null){
                    pst.close();
                    System.out.println(pst);
                }
            }
            if(o.getClass().getTypeName().compareToIgnoreCase(connClass)==0){
                Connection conn= (Connection) o;
                if(conn!=null){
                    conn.close();
                }
            }
            if(o.getClass().getTypeName().compareToIgnoreCase(resultClass)==0){
                ResultSet rs= (ResultSet) o;
                if(rs!=null){
                    rs.close();
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }*/
    }
}
