package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/9 16:03
 * @Description:
 */
@Log4j2
@RestController
@Api(value = "/v1",description = "用户相关5个接口")
@RequestMapping("/v1")
public class UserManger {
    @Autowired
    private SqlSessionTemplate template;


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "用户登录接口",httpMethod = "POST")
    public Boolean login(HttpServletRequest request,HttpServletResponse response, @RequestParam String userName, @RequestParam String password){
        log.info("request url is:" + request.getRequestURL().toString());
        log.info(request.getParameter("userName"));
        log.info(request.getParameter("password"));
        User userOld = template.selectOne("selectByUserName",userName);
        log.info("userOld is :" + userOld);
        if(Objects.isNull(userOld)){
            log.info("此用户" + userName + "不存在");
            return false;
        }
        if(!userOld.getPassword().equals(password)){
            log.info("此用户" + userName + "密码不正确");
            return false;
        }
        log.info("登录成功------>用户是" + userOld.getUserName());
        //设置cookie信息
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);

        //设置session信息,记录当前登录用户的身份
        HttpSession session = request.getSession();
        session.setAttribute("auth",userOld);

        //处理敏感信息
//        userOld.setPassword(null);
        return true;
    }
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ApiOperation(value = "添加用户接口(只有管理员可以）",httpMethod = "POST")
    public Boolean addUser(HttpServletRequest request, @RequestBody User user){
        //验证cookie
        Boolean isLogin = verifyCookie(request);
        if(isLogin == false){
            log.info("需要登录才可进行addUser操作");
            return false;
        }
        //验证管理员身份
        User currentUser = (User) request.getSession().getAttribute("auth");

        if(currentUser == null){
            log.info("session已失效，请重新登录");
            return false;
        }
        System.out.println("addUser方法获取到的当前登录用户信息" + currentUser);
        System.out.println("addUser方法获取到的当前登录用户权限" + currentUser.getPermission());
        if(currentUser.getPermission().equals("1")){
            log.info("只有管理员才可进行addUser操作");
            return false;
        }

        //调用SQL添加数据
        int res = template.insert("addUser",user);

        if(res == 1){
            log.info("addUser方法执行成功，插入数据条数" + res);
            return true;
        }
        return false;
    }

    private Boolean verifyCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        log.info("verifyCookie方法获取到的cookie是" + cookies);
        if(cookies == null || cookies.length == 0){
            return false;
        }
        for (Cookie c:
             cookies) {
            if(c.getName().equals("login") && c.getValue().equals("true")){
                return true;
            }
        }
        return false;
    }
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    @ApiOperation(value = "获取用户（列表）信息接口(只有管理员可以）",httpMethod = "POST")
    public List<User> getUserInfo(HttpServletRequest request, @RequestBody User user){
        //验证cookie
        Boolean isLogin = verifyCookie(request);
        if(isLogin == false){
            log.info("需要登录才可进行getUserInfo操作");
            return null;
        }
        //验证管理员身份
        User currentUser = (User) request.getSession().getAttribute("auth");

        if(currentUser == null){
            log.info("session已失效，请重新登录");
            return null;
        }

        if(currentUser.getPermission().equals("1")){
            log.info("只有管理员才可进行getUserInfo操作");
            return null;
        }

        //调用SQL添加数据
        List<User> list = template.selectList("getUserInfo",user);
        if(Objects.isNull(list) || list.size() == 0){
            return null;
        }
        log.info("getUserInfo查询到的用户数量是" + list.size());
        return list;
    }
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    @ApiOperation(value = "更新用户信息/删除用户接口(只有管理员可以）",httpMethod = "POST")
    public int updateUserInfo(HttpServletRequest request, @RequestBody User user){
        //验证cookie
        Boolean isLogin = verifyCookie(request);
        if(isLogin == false){
            log.info("需要登录才可进行updateUserInfo操作");
            return 0;
        }

        //验证管理员身份
        //验证管理员身份
        User currentUser = (User) request.getSession().getAttribute("auth");

        if(currentUser == null){
            //比如服务器重启
            log.info("session已失效，请重新登录");
            return 0;
        }
        System.out.println("updateUserInfo方法获取到的当前登录用户信息" + currentUser);
        System.out.println("updateUserInfo方法获取到的当前登录用户权限" + currentUser.getPermission());
        if(currentUser.getPermission().equals("1")){
            log.info("只有管理员才可进行updateUserInfo操作");
            return 0;
        }

        //调用SQL添加数据
        int res = template.update("updateUserInfo",user);
        if(res == 1){
            log.info("调用SQL更新用户信息成功");
            User param = new User();
            param.setId(user.getId());
            log.info("调用SQL更新的用户是：" + param.getId());
            User userLatest = template.selectOne("getUserInfo",param);
            log.info("更新后查到的user是:" + userLatest);
            return res;
        }
        log.info("调用SQL更新用户信息失败");
        return 0;
    }
}
