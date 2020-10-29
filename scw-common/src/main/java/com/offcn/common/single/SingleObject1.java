package com.offcn.common.single;

// 单例模式  例 ==> 实例 ，对象  懒汉模式
public class SingleObject1 {

    // 定义一个变量用来存放唯一的对象
    public static SingleObject1 obj1 ;

    // 封装构造方法
    private SingleObject1(){ }

    // 工厂方法
    public static SingleObject1 getInstance(){
        if(obj1 == null) {  // 提高执行效率
            synchronized (SingleObject1.class) {  // 保证线程安全
                if (obj1 == null) {  // 确保对象唯一
                    obj1 = new SingleObject1();
                }
            }
        }
        return obj1;
    }
}
