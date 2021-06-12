package com.course.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/8 11:33
 * @Description:
 */
@Data
public class User {
    @JSONField(ordinal = 0)
    private Integer id;
    @JSONField(ordinal = 1)
    private String userName;
    @JSONField(ordinal = 2)
    private String password;
    @JSONField(ordinal = 3)
    private String age;
    @JSONField(ordinal = 4)
    private String sex;
    @JSONField(ordinal = 5)
    private String permission;
    @JSONField(ordinal = 6)
    private String isDelete;
}
