package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.InterfaceName;
import com.course.model.LoginCase;
import com.course.utils.ConfigFile;
import com.course.utils.MybatisUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/8 15:54
 * @Description:
 */
@Log4j2
public class LoginTest {
    private CookieStore cookieStore = new BasicCookieStore();

    @BeforeTest(groups = "loginTrue",description = "测试准备工作")
    public void beforeTest(){
        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADD_USER);
        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GET_USER_INFO);
        TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GET_USER_LIST);
        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATE_USER_INFO);

//        TestConfig.adminSession = MybatisUtils.openSession();

        TestConfig.client = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();

    }
    @Test(groups = "loginTrue",description = "用户登录成功接口测试")
    public void loginTrue() throws URISyntaxException {
//        SqlSession sqlSession = MybatisUtils.openSession();
        LoginCase loginCase = MybatisUtils.openSession().selectOne("loginCase",1);
        System.out.println(loginCase);
        System.out.println(TestConfig.loginUrl);

        log.info("********开始验证loginTrue api*********");
        //发请求，获取结果
        String result = getResult(loginCase,TestConfig.loginUrl);
        log.info("请求http:localhost/v1/login的结果是------>" + result);
        //验证返回结果
        Assert.assertEquals(loginCase.getExpected(),result);
        log.info("********loginTrue api 验证通过*********");


    }
    //getResult这个是具体发送请求的方法
    private String getResult(LoginCase loginCase, String loginUrl) throws URISyntaxException {
        //1.创建URLBuilder对象方法
        URIBuilder uriBuilder = new URIBuilder(loginUrl);
        uriBuilder.setParameter("userName",loginCase.getUserName()).setParameter("password",loginCase.getPassword());

        //2.创建post方法
        HttpPost post = new HttpPost(uriBuilder.build());

        //3.获得client对象,并设置cookiestore(取自TestConfig)
        TestConfig.client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        //4.设置请求头
        post.setHeader("content-type","application/json");
        //5.设置cookie,已在3设置
        //6.请求api，获得响应结果
        CloseableHttpResponse response = null;
        String res = null;

        try {
            response = TestConfig.client.execute(post);
            res = EntityUtils.toString(response.getEntity(),"utf-8");

            //7.存储cookies到TestConfig
            log.info(cookieStore);
            TestConfig.cookieStore = cookieStore;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                TestConfig.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //7.返回
        return res;
    }

//让这个失败的用户最先执行，然后关掉session
    @Test(groups = "loginFalse",description = "用户登录失败接口测试")
    public void loginFalse() throws URISyntaxException {
        SqlSession userSession = MybatisUtils.openSession();
        LoginCase loginCase = userSession.selectOne("loginCase",2);
        userSession.close();

        System.out.println(loginCase);
        System.out.println(TestConfig.loginUrl);

        log.info("********开始验证loginFalse api*********");
        //发请求，获取结果
        String result = getResult(loginCase,TestConfig.loginUrl);
        log.info("请求http:localhost/v1/login的结果是------>" + result);
        //验证返回结果
        Assert.assertEquals(result,loginCase.getExpected());
        log.info("********loginFalse api 验证通过*********");
    }
}
