package com.course.cases;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.course.config.TestConfig;
import com.course.model.GetUserListCase;
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
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/9 10:10
 * @Description:
 */
@Log4j2
public class GetUserListTest {
    @Test(dependsOnGroups = "loginTrue",description = "获取用户列表接口")
    public void getUserList(){
        SqlSession session = MybatisUtils.openSession();
        GetUserListCase getUserListCase = session.selectOne("getUserListCase",1);
        System.out.println(getUserListCase);
        System.out.println(TestConfig.getUserListUrl);

        log.info("********开始验证getUserList api*********");

        //1.发请求，获取结果
        JSONArray resJsonArray = getResult(getUserListCase,TestConfig.getUserListUrl);
        log.info("请求http:localhost/v1/getUserList的结果是------>" + resJsonArray);
        //2.验证返回结果
        //模拟从数据库查询到的user
        List<User> resFromDatabase = session.selectList(getUserListCase.getExpected(),getUserListCase);
        //对象数组-》json串-》jsonarray
        JSONArray resJSONArrayFromDb = JSONArray.parseArray(JSON.toJSONString(resFromDatabase));

        //先比较长度
        Assert.assertEquals(resJsonArray.size(),resJSONArrayFromDb.size());
        //再比较内容
        for (int i = 0; i < resJsonArray.size(); i++) {
            JSONObject res = resJsonArray.getJSONObject(i);
            JSONObject db  = resJSONArrayFromDb.getJSONObject(i);
            Assert.assertEquals(res,db);
        }
        log.info("********getUserList api 验证通过*********");

    }

    private JSONArray getResult(GetUserListCase getUserListCase, String getUserListUrl) {
        //1.创建post方法
        HttpPost post = new HttpPost(getUserListUrl);
        //2.获得client对象,并设置cookiestore(取自TestConfig)
        TestConfig.client = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
        //3.JSONObject设置请求json数据，利用StringEntity携带在post请求里
        JSONObject param = new JSONObject();
        param.put("userName",getUserListCase.getUserName());
        param.put("sex",getUserListCase.getSex());
        param.put("age",getUserListCase.getAge());

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
        //7.json字符串--->jsonarray返回
        JSONArray jsonArray = JSONArray.parseArray(res);
        System.out.println("res jsonarray is:" + jsonArray);
        return jsonArray;
    }
}
