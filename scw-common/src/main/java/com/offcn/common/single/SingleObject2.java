package com.offcn.common.single;

// 单例模式  例 ==> 实例 ，对象   饿汉模式
public class SingleObject2 {

    // 定义一个变量用来存放唯一的对象

    public static SingleObject2 obj1 = new SingleObject2();

    // 封装构造方法
    private SingleObject2(){ }

    // 工厂方法
    private static SingleObject2 getInstance(){

        return obj1;
    }
}
