package com.course.testng.multiThread;

import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/3 21:07
 * @Description:
 */
public class MultiThreadOnXml {
    @Test
    public void test1(){
        System.out.println(Thread.currentThread().getId() + "在执行test1方法");
    }
    @Test
    public void test2(){
        System.out.println(Thread.currentThread().getId() + "在执行test2方法");
    }
    @Test
    public void test3(){
        System.out.println(Thread.currentThread().getId() + "在执行test3方法");
    }
}
