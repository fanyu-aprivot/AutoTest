package com.course.server;

import com.course.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/7 9:32
 * @Description:
 */
@RestController
@Api(value = "/",description = "这是我全部的post方法")
@RequestMapping("/v1")
public class MyPostMethod {
    private Cookie cookie;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "登录接口（post请求），登录成功后返回cookie信息",httpMethod = "POST")
    public String login(HttpServletResponse response, @RequestParam(value = "userName",required = true) String useName, @RequestParam(value = "password",required = true) String password){
        //模拟数据库验证
        if(useName.equals("lucy") && password.equals("123")){
            Cookie cookie = new Cookie("login","true");
            response.addCookie(cookie);

            this.cookie = cookie;

            return "login success!";
        }else {
            return "login error:userName or password failed!";
        }
    }
    @RequestMapping(value = "/getUserList",method = RequestMethod.POST)
    @ApiOperation(value = "获取用户列表接口（post请求，需要cookie和管理员权限），登录成功后返回用户列表",httpMethod = "POST")
    private List getUserList(HttpServletRequest request, @RequestBody User user){
        //获取用户列表
        List<User> list = new ArrayList<>();
        list.add(new User("lily","111","女",18));
        list.add(new User("tom","222","男",20));
        list.add(new User("ailisi","333","女",24));
        //定义错误的list
        List<String> errorList = new ArrayList<>();
        //验证管理员权限
        if(user.getName().equals("lucy") && user.getPassword().equals("123")){
            //获取cookie
            Cookie[] cookies = request.getCookies();
            if(Objects.isNull(cookies)){
                errorList.add("need cookies");
                return errorList;
            }
            for (Cookie c:
                 cookies) {
                if(c.getName().equals("login") && c.getValue().equals("true")){
                    return list;
                }
            }
        }else {
            errorList.add("need admin auth!");
            return errorList;
        }
        errorList.add("error cookie!");
        return errorList;
    }
}
