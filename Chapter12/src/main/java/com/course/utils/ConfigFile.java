package com.course.utils;

import com.course.model.InterfaceName;
import sun.security.util.Resources;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/8 11:59
 * @Description:
 */
public class ConfigFile {
    private static ResourceBundle bundle = Resources.getBundle("application", Locale.CHINA);

    public static String getUrl(InterfaceName i){
        String address = bundle.getString("test.url");
        String uri = "";
        String testUrl;
//        //判断是否在枚举中
//        if(Objects.isNull(InterfaceName.ofMethod(method))){
//            testUrl = address + uri;
//            return testUrl;
//        }
       if(i.getMethodName().equals(InterfaceName.LOGIN.getMethodName())){
          uri = bundle.getString("login.uri");
       }
       if(i.getMethodName().equals(InterfaceName.ADD_USER.getMethodName())){
           uri = bundle.getString("addUser.uri");
       }
        if(i.getMethodName().equals(InterfaceName.GET_USER_INFO.getMethodName())){
            uri = bundle.getString("getUserInfo.uri");
        }
        if(i.getMethodName().equals(InterfaceName.GET_USER_LIST.getMethodName())){
            uri = bundle.getString("getUserList.uri");
        }
        if(i.getMethodName().equals(InterfaceName.UPDATE_USER_INFO.getMethodName())){
            uri = bundle.getString("updateUserInfo.uri");
        }
        testUrl = address + uri;
        return testUrl;
    }
}
