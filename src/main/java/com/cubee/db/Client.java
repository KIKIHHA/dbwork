package com.cubee.db;

import com.cubee.dao.AbstractFactory;

/**
 * @program: dbtest
 * @description: test
 * @author: Charles
 * @create: 2019-01-23 15:06
 **/

public class Client {
    public static void main(String[] args) {
        Object o=BeanFactory.getBean("userService");
        System.out.println("o.toString() = " + o.toString());
        Integer NUM=Integer.parseInt((String)BeanFactory.getBean("onceCount"));
        System.out.println("NUM = " + NUM);
        BeanFactory.setBean("onceCount","6000");
        NUM=Integer.parseInt((String)BeanFactory.getBean("onceCount"));
        System.out.println("NUM = " + NUM);
        AbstractFactory abstractFactory= (AbstractFactory) BeanFactory.getBean("abstractFactory");
        abstractFactory.createConnection();
    }
}
