package com.course.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/8 11:45
 * @Description:
 */
@Data
public class LoginCase {
    private Integer id;
    private String userName;
    private String password;
    private String expected;
}
