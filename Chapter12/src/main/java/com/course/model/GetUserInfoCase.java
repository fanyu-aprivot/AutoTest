package com.course.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/8 11:42
 * @Description:
 */
@Data
public class GetUserInfoCase {
    private Integer id;
    private String userId;
    private String expected;
}
