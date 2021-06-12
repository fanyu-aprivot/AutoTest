package com.course.cases;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.course.config.TestConfig;
import com.course.model.GetUserInfoCase;
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
 * @Date: 2021/6/9 10:07
 * @Description:
 */
@Log4j2
public class GetUserInfoTest {
    @Test(dependsOnGroups = "loginTrue",description = "获取用户信息接口")
    public void getUserInfo(){
        SqlSession session = MybatisUtils.openSession();
        GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase",1);
        System.out.println(getUserInfoCase);
        System.out.println(TestConfig.getUserInfoUrl);

        log.info("********开始验证getUserInfo api*********");

        //1.发请求，获取结果
       JSONObject resJsonObject = getResult(getUserInfoCase,TestConfig.getUserInfoUrl);
        log.info("请求http:localhost/v1/getUserInfo的结果是------>" + resJsonObject);
        //2.验证返回结果
        //模拟从数据库查询到的user
        User resFromDatabase = session.selectOne(getUserInfoCase.getExpected(),getUserInfoCase);
        //fastjson将java对象转化为JSONObject
        JSONObject resJSONObjectFromDb = (JSONObject) JSONObject.toJSON(resFromDatabase);
        System.out.println("resJSONObjectFromDb如下：" + resJSONObjectFromDb);
        //因为只有一个元素，直接相比即可

        Assert.assertEquals(resJsonObject,resJSONObjectFromDb);
        log.info("********getUserInfo api 验证通过*********");
    }

    private JSONObject getResult(GetUserInfoCase getUserInfoCase, String getUserInfoUrl) {
        //1.创建post方法
        HttpPost post = new HttpPost(getUserInfoUrl);
        //2.获得client对象,并设置cookiestore(取自TestConfig)
        TestConfig.client = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
        //3.JSONObject设置请求json数据，利用StringEntity携带在post请求里
        JSONObject param = new JSONObject();
        param.put("id",getUserInfoCase.getUserId());

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

        //7.json字符串--->jsonarray返回
        //去掉前后[]即[{}]------------->{}
        System.out.println("转化之前的res:" + res);
        //字符串转化为JSONArray对象
        JSONArray jsonArray = JSONArray.parseArray(res);
//        res = res.substring(1,res.length()-1);
//        System.out.println("res返回如下：" + res);
//        //将string字符串转化为JSONObject
//        JSONObject resJsonObject = JSON.parseObject(res);

        //从jsonarray取得jsonobject
        JSONObject resJsonObject = jsonArray.getJSONObject(0);
        System.out.println("resJsonObject如下：" + resJsonObject);

        return resJsonObject;
    }

}
