package com.course.cases;

import com.alibaba.fastjson.JSONObject;
import com.course.config.TestConfig;
import com.course.model.UpdateUserInfoCase;
import com.course.model.User;
import com.course.utils.MybatisUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/9 10:12
 * @Description:
 */
@Log4j2
public class UpdateUserInfoTest {
    //更新用户信息和删除用户信息共用一个接口
    //假设数据库里第一条case是更新，第二条case是删除
    @Test(dependsOnGroups = "loginTrue",description = "更新用户信息接口")
    public void updateUserInfo(){
        UpdateUserInfoCase updateCase = MybatisUtils.openSession().selectOne("updateUserInfoCase",1);
        System.out.println(updateCase);
        System.out.println(TestConfig.updateUserInfoUrl);

        log.info("********开始验证updateUserInfo api*********");

        //1.发请求，获取结果
        int res = getResult(updateCase,TestConfig.updateUserInfoUrl);
        log.info("请求http:localhost/v1/updateUserInfo的结果是------>" + res);
        //2.验证返回结果
        Assert.assertEquals(res,1);
        //模拟从数据库查询到的user
//        Integer userId = updateCase.getUserId();//传参由updateCase改为用userId查询
        User resFromDatabase = MybatisUtils.openSession().selectOne(updateCase.getExpected(),updateCase);
        Assert.assertNotNull(resFromDatabase);
        System.out.println("db is:" + resFromDatabase);
        log.info("********updateUserInfo api 验证通过*********");
    }
    @Test(dependsOnGroups = "loginTrue",description = "删除用户信息接口")
    public void deleteUserInfo(){
        SqlSession session = MybatisUtils.openSession();
        UpdateUserInfoCase deleteCase = session.selectOne("updateUserInfoCase",2);
        session.close();
        System.out.println(deleteCase);
        System.out.println(TestConfig.updateUserInfoUrl);

        log.info("********开始验证deleteUserInfo api*********");

        //1.发请求，获取结果
        int res = getResult(deleteCase,TestConfig.updateUserInfoUrl);
        log.info("请求http:localhost/v1/updateUserInfo的结果是------>" + res);
        //2.验证返回结果
        Assert.assertEquals(res,1);
        //模拟从数据库查询到的user
//        Integer userId = updateCase.getUserId();//传参由updateCase改为用userId查询
        User resFromDatabase = MybatisUtils.openSession().selectOne(deleteCase.getExpected(),deleteCase);
        Assert.assertNotNull(resFromDatabase);
        System.out.println("db is:" + resFromDatabase);
        log.info("********deleteUserInfo api 验证通过*********");
    }

    private int getResult(UpdateUserInfoCase delOrUpdateCase, String updateUserInfoUrl) {
        //1.创建post方法
        HttpPost post = new HttpPost(updateUserInfoUrl);
        //2.获得client对象,并设置cookiestore(取自TestConfig)
        TestConfig.client = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
        //3.JSONObject设置请求json数据，利用StringEntity携带在post请求里
        JSONObject param = new JSONObject();
        param.put("userName",delOrUpdateCase.getUserName());
        param.put("sex",delOrUpdateCase.getSex());
        param.put("age",delOrUpdateCase.getAge());
        param.put("permission",delOrUpdateCase.getPermission());
        param.put("isDelete",delOrUpdateCase.getIsDelete());
        param.put("id",delOrUpdateCase.getUserId());

        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //4.设置请求头
        post.setHeader("content-type","application/json");
        //5.设置cookie,已在2设置
        //6.请求api，获得响应结果
        CloseableHttpResponse response = null;
        String res = null;

        try {
            response = TestConfig.client.execute(post);
            res = EntityUtils.toString(response.getEntity(),"utf-8");
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
        System.out.println("res is:" + res);
        //7.string--->int返回
        return Integer.parseInt(res);
    }
}
