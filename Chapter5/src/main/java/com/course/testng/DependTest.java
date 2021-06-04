package com.course.testng;

import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/3 11:42
 * @Description:
 */
public class DependTest {
    @Test(dependsOnMethods = {"test2"})
    public void test1(){
        System.out.println("这是test1方法");
    }
    @Test
    public void test2(){
        System.out.println("这是依赖的方法test2");
        throw new RuntimeException();
    }
}
