package com.course.extent;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/4 10:27
 * @Description:
 */
public class TestMethodsDemo {
    @Test
    public void test1(){
        //断言判断
        Assert.assertEquals(1,2);
    }
    @Test
    public void test2(){
        //断言判断
        Assert.assertEquals(1,1);
    }
    @Test
    public void test3(){
        //断言判断
        Assert.assertEquals("aaa","aaa");
    }
    @Test
    public void logDemo(){
//        TestNG中的Report类来实现简单的log输出
        Reporter.log("这是我们自己写的日志");
        throw new RuntimeException("这是我们自己制造的运行时异常");
    }
}
