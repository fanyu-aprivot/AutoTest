package com.course.testng;

import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/3 10:24
 * @Description:
 */
public class IgnoreTest {
    @Test(enabled = true)
    public void test1(){
        System.out.println("test1执行！");
    }
    @Test
    public void test2(){
        System.out.println("test2执行！");
    }
    @Test(enabled = false)
    public void test3(){
        System.out.println("test3执行！");
    }
}
