package com.cubee.dao;

import com.cubee.db.ConnectionDao;

/**
 * @program: dbtest
 * @description: 抽象工厂
 * @author: Charles
 * @create: 2019-01-23 18:01
 **/

public interface AbstractFactory {
    ConnectionDao createConnection();
}
