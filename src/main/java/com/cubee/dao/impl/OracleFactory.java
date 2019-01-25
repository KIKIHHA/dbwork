package com.cubee.dao.impl;

import com.cubee.dao.AbstractFactory;
import com.cubee.db.ConnectionDao;

/**
 * @program: dbtest
 * @description:
 * @author: Charles
 * @create: 2019-01-23 18:03
 **/

public class OracleFactory implements AbstractFactory {
    @Override
    public ConnectionDao createConnection() {
        return new OracleConnection();
    }
}
