package com.course.testng.groups;

        import org.testng.annotations.AfterGroups;
        import org.testng.annotations.BeforeGroups;
        import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/3 10:35
 * @Description:
 */
public class GroupsOnMethod {
    @Test(groups = "server")
    public void test1(){
        System.out.println("这是服务端组---test1");
    }
    @Test(groups = "server")
    public void test2(){
        System.out.println("这是服务端组---test2");
    }
    @Test(groups = "client")
    public void test3(){
        System.out.println("这是客户端组---test3");
    }
    @Test(groups = "client")
    public void test4(){
        System.out.println("这是客户端组---test4");
    }
    @BeforeGroups("server")
    public void beforeGroupsOnServer(){
        System.out.println("这是服务端组运行之前的方法===========");
    }
    @AfterGroups("server")
    public void afterGroupsOnServer(){
        System.out.println("这是服务端组运行之后的方法===========");
    }
    @BeforeGroups("client")
    public void beforeGroupsOnClient(){
        System.out.println("这是客户端组运行之前的方法===========");
    }
    @AfterGroups("client")
    public void afterGroupsOnClient(){
        System.out.println("这是客户端组运行之后的方法===========");
    }

}
