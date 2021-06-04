package com.course.testng.groups;

import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/3 10:57
 * @Description:
 */
@Test(groups = "student")
public class GroupsOnClass1 {
    public void test1(){
        System.out.println("GroupsOnClass1-------->test1执行了");
    }
    public void test2(){
        System.out.println("GroupsOnClass1-------->test2执行了");
    }

}
