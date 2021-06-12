package com.course.cases;

import com.alibaba.fastjson.JSONObject;
import com.course.config.TestConfig;
import com.course.model.AddUserCase;
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
 * @Date: 2021/6/9 10:01
 * @Description:
 */
@Log4j2
public class AddUserTest {
    @Test(dependsOnGroups = "loginTrue",description = "添加用户接口")
    public void addUser(){
        SqlSession session = null;
        AddUserCase addUserCase;
        try {
             session = MybatisUtils.openSession();
              addUserCase = session.selectOne("addUserCase", 2);
        }finally {
            session.close();
        }

        System.out.println(addUserCase);
        System.out.println(TestConfig.addUserUrl);

        log.info("********开始验证addUser api*********");
        //发请求，获取结果
        String result = getResult(addUserCase,TestConfig.addUserUrl);
        log.info("请求http:localhost/v1/addUser的结果是------>" + result);
        //验证返回结果
        Assert.assertEquals(result,addUserCase.getExpected());
//        User insertUser = new User();
//        BeanUtils.copyProperties(addUserCase,insertUser);
//        insertUser.setId(null);
        User insertUser;
        try{
             session = MybatisUtils.openSession();
             insertUser = session.selectOne("getAddedUser",addUserCase);
        }finally {
            session.close();
        }

        Assert.assertNotNull(insertUser);
        log.info("********addUser api 验证通过*********");
    }

    private String getResult(AddUserCase addUserCase,String testUrl){
        //1.创建post方法
        HttpPost post = new HttpPost(testUrl);
        //2.获得client对象,并设置cookiestore(取自TestConfig)

        TestConfig.client = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
        //3.JSONObject设置请求json数据，利用StringEntity携带在post请求里
        JSONObject param = new JSONObject();
        param.put("userName",addUserCase.getUserName());
        param.put("password",addUserCase.getPassword());
        param.put("sex",addUserCase.getSex());
        param.put("age",addUserCase.getAge());
        param.put("permission",addUserCase.getPermission());
        param.put("isDelete",addUserCase.getIsDelete());


        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //4.设置请求头
        post.setHeader("content-type","application/json");
        //5.设置cookie,已在2设置
        //6.请求api，获得响应结果
        CloseableHttpResponse response = null;
        String res = "false";

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

        //7.返回
        return res;
    }
}
