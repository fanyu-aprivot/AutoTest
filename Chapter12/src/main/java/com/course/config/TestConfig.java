package com.course.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/8 11:57
 * @Description:
 */
public class TestConfig {
    public static String loginUrl;
    public static String addUserUrl;
    public static String getUserInfoUrl;
    public static String getUserListUrl;
    public static String updateUserInfoUrl;

//    public static SqlSession adminSession;

    public static CloseableHttpClient client;
    public static CookieStore cookieStore;
}
