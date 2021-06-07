package com.course.httpclient.cookies;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/5 16:45
 * @Description:
 */
public class MyCookiesForPost {
    private String url;
    private ResourceBundle bundle;
    //用来存储cookies信息的变量
    private CookieStore cookieStore;

    //在测试方法执行前加载配置文件
    @BeforeTest
    public void beforeTest(){
        //如何读取配置文件？---java.util.ResourceBundle,只需要传参为配置文件的前缀即可
        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        url = bundle.getString("test.url");
    }
    //新版httpclient4.5.2
    @Test
    public void testGetCookies() throws IOException {
        //从配置文件拼接测试的URL
        String uri;
        String testUrl;
        uri = bundle.getString("getCookies.uri");
        testUrl = this.url + uri;

        //获取get方法
        HttpGet get = new HttpGet(testUrl);

        //创建cookiestore
        CookieStore cookieStore = new BasicCookieStore();

        //创建CloseableHttpClient对象,同时设置cookiestore
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        //初始化response对象和正文响应字符串为null
        CloseableHttpResponse response = null;
        String result = null;
        //执行get方法
        try{
            response = client.execute(get);
            //为响应数据指定utf-8格式
            result = EntityUtils.toString(response.getEntity(),"utf-8");
            System.out.println(result);

            //获取cookiestore,为全局变量cookieStore赋值
            this.cookieStore = cookieStore;

            //打印输出
            List<Cookie> cookieList = this.cookieStore.getCookies();
            for (Cookie c:
                    cookieList) {
                System.out.println("cookie name=" + c.getName() + ";cookie value=" + c.getValue());
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test(dependsOnMethods = {"testGetCookies"})
    public void testPostWithCookiesAndJsonData() throws IOException {
        //1.拼装URL
        String uri = bundle.getString("test.post.with.cookiesAndJsonData");
        String testUrl = this.url + uri;

        //2.创建client对象用来执行方法，创建post方法
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();
        //！！！错误1：未传参为testUrl,导致 HttpResponse response = client.execute(post);
        // 出现空指针异常***********************************
        HttpPost post = new HttpPost(testUrl);

        //3.设置请求头信息，设置header
        post.setHeader("content-type","utf-8");

        //4.将json请求参数添加到post方法
        JSONObject param = new JSONObject();
        param.put("name","lucy");
        param.put("age","18");
        //??????***********JSONObject如何转换为HttpEntity对象
        //错误2：JSONObject无法转换为HttpEntity对象，会出现java.lang.ClassCastException
//        HttpEntity entity = (HttpEntity)param;
//        post.setEntity(entity);
//        System.out.println("json请求参数转换为string类型：" + param.toString());
        //创建StringEntity对象
        StringEntity stringEntity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(stringEntity);

        //5.设置cookies信息  这个是httpclient4.3以前的写法
//        client.setCookieStore(this.cookieStore);

        //6.声明result对象来进行结果的存储；声明response对象
        String result;
        CloseableHttpResponse response = null;
        try {
            //7.执行post方法
           response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200) {
                System.out.println("the status code is totally right,then let us get the response data...");
                //8.获取响应数据
                HttpEntity entity1 = response.getEntity();
                //错误3！！！entity1直接转换为string是org.apache.http.conn.BasicManagedEntity@77ec78b9
//        result = entity1.toString();
                result = EntityUtils.toString(entity1, "utf-8");
                System.out.println(result);

                //9.断言判断返回结果是否符合预期
                //将返回的结果字符串转换为json对象
                JSONObject jsonRes = new JSONObject(result);
                //具体的判断返回结果的值
                Assert.assertEquals(jsonRes.get("lucy"), "success");
                Assert.assertEquals(jsonRes.get("status"), "0");

                System.out.println("duanyan execute successfully!");
            }else {
                System.out.println("the expected status is 200 but not found");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test(dependsOnMethods = {"testGetCookies"})
    public void testPostWithCookiesAndFormData() throws IOException {
        //1.拼接形成最终的测试URL
        String url = bundle.getString("test.post.with.cookiesAndFormData");
        String testUrl = this.url + url;

        //2.创建client对象，创建post方法
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();
        HttpPost post = new HttpPost(testUrl);

        //3.设置请求头信息header
        post.setHeader("content-type","utf-8");

        //4.设置请求参数;
        // 错误1！！！：这里用StringEntity+JSONObject/Map都是无效的，需要采用UrlEncodedFormEntity
        Map<String,String> map = new HashMap<>();
        map.put("name","lucy");
        map.put("age","18");
        List<NameValuePair> paramList = new ArrayList<>();
        for (String key : map.keySet()) {
            //BasicNameValuePair是NameValuePair接口的实现类
            paramList.add(new BasicNameValuePair(key, map.get(key)));
        }
        //模拟表单
        UrlEncodedFormEntity entity = null;
        try {
//            entity = new StringEntity(map.toString(),"utf-8");
            entity = new UrlEncodedFormEntity(paramList,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setEntity(entity);

        //5.创建response对象和result对象
        CloseableHttpResponse response = null;
        String result;

        //6.client执行post方法
        try {
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("status expect 200 and actually 200");

                //7.获取响应数据并输出
                HttpEntity entityRes = response.getEntity();
                String res = EntityUtils.toString(entityRes);
                System.out.println(res);

                //8.断言判断响应数据是否正确
                JSONObject jsonRes = new JSONObject(res);
                Assert.assertEquals(jsonRes.getString("status"), "0");
                Assert.assertEquals(jsonRes.getString("cookie"), "success");
                Assert.assertEquals(jsonRes.getString("lucy"), "success");
                System.out.println("the returned data is totally right and duanyan success");
            } else {
                System.out.println("run fail! status expect 200 but not 200 found");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
