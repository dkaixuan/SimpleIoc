package com.simple;

/**
 * @author kaixuan
 * @version 1.0
 * @date 23/3/2020 上午11:43
 */
public class Car {
    private String name;
    private String length;
    private String width;
    private String height;
    private Wheel wheel;

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", length='" + length + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", wheel=" + wheel +
                '}';
    }
// 省略其他不重要代码
}

