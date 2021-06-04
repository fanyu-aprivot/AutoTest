package com.course.testng.parameter;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/3 12:01
 * @Description:
 */

public class ParameterTest {
    @Test
    @Parameters({"age","name"})
    public void paraTest(@Optional("17")Integer age,@Optional("lily")String name){
        System.out.println("name=" + name + ",age=" + age);
    }
}
