package com.holly.desginpattern.singleton;

/**
 * @ClassName InnerSingleton
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-11-27 16:44
 **/
public class InnerSingleton {

    private InnerSingleton() {
        System.out.println(Thread.currentThread().getName()+"\tInnerSingleton\t 私有构造器执行!");
    }
    private static class InnerHolder{
        private static InnerSingleton instance = new InnerSingleton();
    }

    public static InnerSingleton getInstance(){
        return InnerHolder.instance;
    }
}
