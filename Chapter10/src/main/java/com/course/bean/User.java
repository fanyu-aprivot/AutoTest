package com.course.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/7 10:15
 * @Description:
 */
@Data
@AllArgsConstructor
public class User {
    private String name;
    private String password;
    private String sex;
    private Integer age;

}
