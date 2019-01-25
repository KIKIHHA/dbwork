package com.cubee.dao.impl;

import com.cubee.dao.AbstractFactory;
import com.cubee.db.ConnectionDao;

/**
 * @program: dbtest
 * @description: 实例化任一连接的具体工厂
 * @author: Charles
 * @create: 2019-01-24 10:49
 **/

public class AnyFactory implements AbstractFactory {
    @Override
    public ConnectionDao createConnection() {
        return new AnyConnection();
    }
}
