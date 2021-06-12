package com.course.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/8 11:44
 * @Description:
 */
@Data
public class GetUserListCase {
    private Integer id;
    private String userName;
    private String age;
    private String sex;
    private String expected;
}
