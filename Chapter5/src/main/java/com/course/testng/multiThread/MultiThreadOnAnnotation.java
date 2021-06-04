package com.course.testng.multiThread;

import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/3 20:40
 * @Description:
 */
public class MultiThreadOnAnnotation {
    @Test(invocationCount = 10,threadPoolSize = 3)
    public void test1(){
        System.out.println(Thread.currentThread().getId() + "--->output:" + 1);

    }
}
