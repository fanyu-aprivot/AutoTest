package com.course.testng.parameter;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/3 20:09
 * @Description:
 */
public class DataProviderTest {
    @Test(dataProvider = "data")
    public void test(String name,Integer age){
        System.out.println("name=" + name + ";age=" + age);
    }
    @DataProvider(name = "data")
    public Object[][] providerData(){
        Object[][] o = new Object[][]{
                {"lucy",17},
                {"tom",21},
                {"nana",23}
        };
        return o;
    }
    //==================================================================

    @Test(dataProvider = "dataProvider")
    public void test1(String name,Integer age){
        System.out.println("test1111：name=" + name + ";age=" + age);
    }
    @Test(dataProvider = "dataProvider")
    public void test2(String name,Integer age){
        System.out.println("test2222：name=" + name + ";age=" + age);
    }
//    dataProvider向不同方法传递不同的参数；
    @DataProvider(name = "dataProvider")
    public Object[][] providerDataByDiffMethod(Method method){
//        必须要写method入参，自动把test1和test2两个方法传过来；
//        注意是java.lang.reflect.Method;
        Object[][] o = null;
        if(method.getName().equals("test1")){
            o = new Object[][]{
                    {"赵露思",21},
                    {"赵婷婷",18}
            };
        }else if(method.getName().equals("test2")){
            o = new Object[][]{
                    {"婉言",17},
                    {"花西子",30}
            };
        }
        return o;
    }

}
