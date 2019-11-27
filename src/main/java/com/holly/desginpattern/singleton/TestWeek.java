package com.holly.desginpattern.singleton;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestWeek {

    //饿汉式 HungerSingleton 私有构造器执行1,没有线程安全问题
    @Test
    public void test01() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                HungerSingleton singleton = HungerSingleton.getInstance();
                System.out.println("线程\t" + Thread.currentThread().getName() + " \t->" + singleton.hashCode());
            }, (i + 1) + "").start();
        }

        Thread.sleep(TimeUnit.SECONDS.toSeconds(15));
    }

    //懒汉式 LazySingleton  LazySingleton 私有的构造方法被实例化 1 次。
    // 1  LazySingleton 私有的构造方法被实例化 1 次。
    //2	 LazySingleton 私有的构造方法被实例化 2 次。
    // 存在线程安全问题
    @Test
    public void test02() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                LazySingleton singleton = LazySingleton.getInstance();
                System.out.println("线程A\t" + Thread.currentThread().getName() + " \t->" + singleton.hashCode());
            }, (i + 1) + "").start();

        }
        Thread.sleep(TimeUnit.SECONDS.toSeconds(15));
    }

    //双重锁 SyncSingleton  SyncSingleton 私有的构造方法被实例化 1 次。没有线程安全问题
    @Test
    public void test03() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
          new Thread(() -> {
                SyncSingleton singleton = SyncSingleton.getInstance();
                System.out.println("线程A\t" + Thread.currentThread().getName() + " \t->" + singleton.hashCode());
            }, (i + 1) + "").start();
        }
        for (int i = 0; i < 50; i++) {
           new Thread(() -> {
                SyncSingleton singleton = SyncSingleton.getInstance();
                System.out.println("线程B\t" + Thread.currentThread().getName() + "\t->" + singleton.hashCode());
            }, (i + 1) + "").start();
        }
        Thread.sleep(TimeUnit.SECONDS.toSeconds(15));
    } //双重锁 SyncSingleton  SyncSingleton 私有的构造方法被实例化 1 次。

    //内部类 InnerSingleton  9	InnerSingleton	 私有构造器执行!
    @Test
    public void test04() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
          new Thread(() -> {
              InnerSingleton singleton = InnerSingleton.getInstance();
                System.out.println("线程A\t" + Thread.currentThread().getName() + " \t->" + singleton.hashCode());
            }, (i + 1) + "").start();
        }
        for (int i = 0; i < 50; i++) {
           new Thread(() -> {
               InnerSingleton singleton = InnerSingleton.getInstance();
                System.out.println("线程B\t" + Thread.currentThread().getName() + "\t->" + singleton.hashCode());
            }, (i + 1) + "").start();
        }
        Thread.sleep(TimeUnit.SECONDS.toSeconds(15));
    }
    //枚举类 SingletonEnum  6	SingletonEnum被初始化
 @Test
    public void test05() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
          new Thread(() -> {
              EnumSingleton.SingletonEnum singleton = EnumSingleton.INSTANCE.getInstance();
                System.out.println("线程A\t" + Thread.currentThread().getName() + " \t->" + singleton.hashCode());
            }, (i + 1) + "").start();
        }

        Thread.sleep(TimeUnit.SECONDS.toSeconds(15));
    }


}
