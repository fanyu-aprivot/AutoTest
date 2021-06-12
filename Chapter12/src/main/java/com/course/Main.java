package com.course;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.List;

public class Main {
            public static void main(String[] args) {
                XmlSuite suite = new XmlSuite();
                suite.setName("用户管理系统测试套件");
                List<XmlClass> classes = new ArrayList<>();
                classes.add(new XmlClass("com.course.cases.LoginTest"));
                classes.add(new XmlClass("com.course.cases.AddUserTest"));
                classes.add(new XmlClass("com.course.cases.GetUserInfoTest"));
                classes.add(new XmlClass("com.course.cases.GetUserListTest"));
                classes.add(new XmlClass("com.course.cases.UpdateUserInfoTest"));
                XmlTest test = new XmlTest(suite);
                test.setName("用户管理系统测试");
                test.setXmlClasses(classes);
                List<XmlSuite> suites = new ArrayList<>();
                suites.add(suite);

                TestNG tng = new TestNG();
                tng.setXmlSuites(suites);
                tng.run();
                System.out.println("-------------》main函数代码执行");
     }
}
