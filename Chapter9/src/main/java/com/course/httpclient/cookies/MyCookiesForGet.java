package com.course.httpclient.cookies;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/5 11:14
 * @Description:
 */
public class MyCookiesForGet {
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
    //旧版httpclient4.1.2
    @Test
    public void testGetCookies1() throws IOException {
        //1.拼接组装URL
        String uri = bundle.getString("getCookies.uri");
        String testUrl = this.url + uri;
        //2.创建client对象(HttpClient无法获取cookie，只能通过DefaultHttpClient)，get请求
        HttpGet get = new HttpGet(testUrl);
        DefaultHttpClient client = new DefaultHttpClient();

        //3.执行get请求，获得response对象
        HttpResponse response = client.execute(get);
        //4.获得响应正文
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        //5.获得cookiestore并为此类的全局变量赋值
        this.cookieStore = client.getCookieStore();
        //6.获取cookie，并打印输出
        List<Cookie> cookieList = this.cookieStore.getCookies();
        for (Cookie c:
             cookieList) {
            System.out.println("cookie name=" + c.getName() + ";cookie value=" + c.getValue());
        }

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

        //获取cookies信息（HttpClient无法获取，只能通过DefaultHttpClient）,
            //故需要修改DefaultHttpClient client = new DefaultHttpClient();
//        CookieStore cookieStore = client.getCookieStore();
//        this.cookieStore = client.getCookieStore();
//        List<Cookie> cookieList =  cookieStore.getCookies();
//        List<Cookie> cookieList = this.cookieStore.getCookies();
//        for (Cookie c:
//                cookieList) {
//            System.out.println("cookie name=" + c.getName() + ";cookie value=" + c.getValue());
//        }


    }
    @Test(dependsOnMethods = {"testGetCookies"})
    public void testGetWithCookies() throws IOException {
        //1.拼接URL
        String uri = bundle.getString("test.get.with.cookies");
        String testUrl = this.url + uri;

        //2.创建get请求对象 + 创建client对象，设置cookie + 创建response对象
        HttpGet get = new HttpGet(testUrl);
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();
        CloseableHttpResponse response = null;

        //3.执行get方法获取response，获取状态码，获取响应数据
        try {
            //执行get方法
            response = client.execute(get);
            //获取状态码
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("statusCode is :" + statusCode);

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "utf-8");
                System.out.println("请求接口成功");
                System.out.println("the result is :" + result);
            }else {
                System.out.println("请求接口失败");
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
    public void testGetWithCookiesAndParams() throws IOException, URISyntaxException {
        //1.拼接最终测试的URL+设置参数在URL后面可实现，但是不推荐
        String url = bundle.getString("test.get.with.cookiesAndParams");
        String testUrl = this.url + url;
//        String paramsUrl = "name=lucy&age=17";
//        testUrl = testUrl + "?" + paramsUrl;

        //2.创建client对象，的同时设置好cookie
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();

        //3.创建URLBuilder对象,get对象
        URIBuilder uriBuilder = new URIBuilder(testUrl);
        //URLBuilder设置参数
        uriBuilder.setParameter("name", "lucy").setParameter("age","17");
        //创建HttpGet对象，设置URL地址
        HttpGet get = new HttpGet(uriBuilder.build());

        // 4.设置请求头信息设置header
        get.setHeader("content-type", "utf-8");

        //5.执行get请求
        CloseableHttpResponse response=null;
        try {
            //使用CloseableHttpClient发起响应获取repsonse
             response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("状态码正确，请求成功，下面开始获取响应数据");
                //6.获取响应数据
                HttpEntity entity = response.getEntity();
                // EntityUtils.toString（）获取响应正文中的内容
                String result = EntityUtils.toString(entity, "utf-8");
                System.out.println(result);
                //7.断言判断响应数据
                //!!!!错误1：此处应该传入result而非entity
//            JSONObject jsonObject = new JSONObject(entity);
                JSONObject jsonObject = new JSONObject(result);
                System.out.println(jsonObject.toString());
                Assert.assertEquals(jsonObject.getString("cookie"), "success");
                Assert.assertEquals(jsonObject.getString("lucy"), "success");
                System.out.println("断言判断成功");
            } else {
                System.out.println("状态码不对，请求失败");
            }
        }catch (Exception e){
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
}
