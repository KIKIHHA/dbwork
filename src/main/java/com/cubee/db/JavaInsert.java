package com.cubee.db;

import java.sql.*;
import java.text.ParseException;

public class JavaInsert {


    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
        Connection conn= ConnectionUtil.getConnection();
        conn.setAutoCommit(false);
        String sql="insert into t1 values(?,?,?)";
        PreparedStatement pst=conn.prepareStatement(sql);
        ResultSet rs=null;
        Long sum=0L;
        for(int i=1;i<=100000;i++){
            pst.setLong(1,i);
            Long time=System.currentTimeMillis();
            Timestamp timestamp=new Timestamp(time);
            pst.setTimestamp(2,timestamp);
            pst.setLong(3,100000-i);
            pst.addBatch();
            if(i%10000==0){
                pst.executeBatch();
                pst.clearBatch();
                conn.commit();
            }
        }
        pst.executeBatch();
        pst.clearBatch();


        pst.close();
        conn.close();
    }
    }





