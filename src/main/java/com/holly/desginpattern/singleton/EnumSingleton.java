package com.holly.desginpattern.singleton;

/**
 * @ClassName EnumSingleton
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-11-27 17:34
 **/
public enum EnumSingleton {
    INSTANCE;
    private SingletonEnum instance;

    private EnumSingleton(){
        instance = new SingletonEnum();
    }

    public SingletonEnum getInstance() {
        return instance;
    }

    public class SingletonEnum{
        private SingletonEnum(){
            System.out.println(Thread.currentThread().getName()+"\tSingletonEnum被初始化");
        }
    }

}
