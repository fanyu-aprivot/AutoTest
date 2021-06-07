package com.course.server;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/6 20:00
 * @Description:
 */
@RestController
@Api(value = "/",description = "这是我全部的get方法")
public class MyGetMethod {
    //1.开发一个返回cookie的get接口
    @ApiOperation(value = "通过这个get请求接口可以获取到cookies",httpMethod = "GET")
    @RequestMapping(value = "/getCookies",method = RequestMethod.GET)
    public String getCookies(HttpServletResponse response){
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        return "成功获取到cookies信息啦!";
    }
    //2.开发一个需要携带cookie才能访问的get接口
    @ApiOperation(value = "需要携带cookie才能访问的get请求接口",httpMethod = "GET")
    @RequestMapping(value = "/get/with/cookies",method = RequestMethod.GET)
    public String getWithCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)){
            return "cookies is a must!";
        }
        for (Cookie c:
             cookies) {
            if(c.getName().equals("login") && c.getValue().equals("true")){
                return "contragulations to you on successful login";
            }
        }
        return "wrong cookies";
    }
    //3.开发一个需要携带参数才能访问的get接口
    //实现方式1
    @ApiOperation(value = "需要携带参数才能访问的get请求接口1",httpMethod = "GET")
    @RequestMapping(value = "/get/with/param",method = RequestMethod.GET)
    public Map<String,Integer> getWithParams(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        Map<String,Integer> allList = new HashMap<>();
        allList.put("娃哈哈",50);
        allList.put("农夫山泉",4);
        allList.put("特仑苏",8);
        allList.put("相宜本草",300);
        return allList;
    }
    //开发一个需要携带参数才能访问的get接口
    //实现方式2
    @ApiOperation(value = "需要携带参数才能访问的get请求接口2",httpMethod = "GET")
    @RequestMapping(value = "/get/with/param/{pageNum}/{pageSize}",method = RequestMethod.GET)
    public Map<String,Integer> getWithParams1(@PathVariable Integer pageNum, @PathVariable Integer pageSize){
        Map<String,Integer> allList = new HashMap<>();
        allList.put("娃哈哈",50);
        allList.put("农夫山泉",4);
        allList.put("特仑苏",8);
        allList.put("相宜本草",300);
        return allList;
    }
    //4.开发一个需要携带cookie和参数才能访问的get接口
    @ApiOperation(value = "需要携带cookie和参数才能访问的get请求接口",httpMethod = "GET")
    @RequestMapping(value = "/get/with/cookieAndParam")
    public Map<String,Integer> getWithCookieAndParams(HttpServletRequest request,@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        Cookie[] cookies = request.getCookies();
        Map<String,Integer> map = new HashMap<>();
        map.put("农夫山泉",4);
        map.put("特仑苏",8);
        map.put("相宜本草",300);
        Map<String,Integer> errorMap = new HashMap<>();
        if(Objects.isNull(cookies)){
            errorMap.put("cookies is a must!",1);
            return errorMap;
        }

        for (Cookie c:
             cookies) {
            if(c.getName().equals("login") && c.getValue().equals("true")){
                return map;
            }
        }
        errorMap.put("wrong cookies",1);
        return errorMap;
    }
}
