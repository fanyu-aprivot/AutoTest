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
public class UpdateUserInfoCase {
    private Integer id;
    private Integer userId;
    private String userName;
    private String sex;
    private String age;
    private String permission;
    private String isDelete;
    private String expected;

}
