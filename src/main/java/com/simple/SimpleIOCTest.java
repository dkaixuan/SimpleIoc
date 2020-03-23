package com.simple;

import org.junit.Test;

/**
 * @author kaixuan
 * @version 1.0
 * @date 23/3/2020 上午11:47
 */
public class SimpleIOCTest {

    @Test
    public void iocTest() throws Exception {
        String location = SimpleIOC.class.getClassLoader().getResource("applicationContext.xml").getFile();
        SimpleIOC ioc = new SimpleIOC(location);
        Wheel wheel = (Wheel) ioc.getBean("wheel");
        Car car = (Car) ioc.getBean("car");
        System.out.println(wheel);
        System.out.println(car);



    }

}
