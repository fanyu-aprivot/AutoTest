package com.course.model;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/8 11:48
 * @Description:
 */
public enum InterfaceName {
    LOGIN(0,"login"),
    GET_USER_LIST(1,"getUserList"),
    GET_USER_INFO(2,"getUserInfo"),
    ADD_USER(3,"addUser"),
    UPDATE_USER_INFO(4,"updateUserInfo");
    private Integer code;
    private String methodName;

    InterfaceName(Integer code, String methodName) {
        this.code = code;
        this.methodName = methodName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public static InterfaceName ofMethod(String methodName){
        for (InterfaceName i:
             values()) {
           if(i.getMethodName().equals(methodName)){
               return i;
           }
        }
        return null;
    }
}
