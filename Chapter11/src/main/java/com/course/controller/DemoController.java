package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/7 16:36
 * @Description:
 */
@Slf4j
@RestController
@Api(value = "v1",description = "这是我的第一个版本的demo")
@RequestMapping("v1")
public class DemoController {
    @Autowired
    private  SqlSessionTemplate template;

    @RequestMapping(value = "/getUserCount",method = RequestMethod.GET)
    @ApiOperation(value = "返回用户数量的接口",httpMethod = "GET")
    public int getUserCount(){
//        SqlSession session = MybatisUtils.openSession();
        int count =  template.selectOne("getUserCount");
        System.out.println(count);
        return count;
    }
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ApiOperation(value = "添加用户的接口",httpMethod = "POST")
    public int addUser(@RequestBody User user){
        int res = template.insert("addUser",user);
        return res;
    }
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    @ApiOperation(value = "更新用户的接口",httpMethod = "POST")
    public int updateUser(@RequestBody User user){
        int res = template.update("updateUser",user);
        return res;
    }
    @RequestMapping(value = "/deleteUser",method = RequestMethod.POST)
    @ApiOperation(value = "删除用户的接口",httpMethod = "POST")
    public int deleteUser(@RequestParam Integer id){
        int res = template.delete("deleteUser",id);
        return res;
    }
}
