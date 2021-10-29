package com.atguigu.yygh.common.utils;

import com.atguigu.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dzwstart
 * @date 2021/10/29 - 14:47
 */

//获取当前用户信息工具类
public class AuthContextHolder {

    //获取当前用户id
    public static Long getUserId(HttpServletRequest request){
        //从header获取token
        String token =request.getHeader("token");

        //jwt从token获取userid
        Long userId = JwtHelper.getUserId(token);

        return userId;
    }
    //获取当前用户名称
    public static String  getUserName(HttpServletRequest request){
        //从header获取token
        String token =request.getHeader("token");

        //jwt从token获取userName
        String userName = JwtHelper.getUserName(token);

        return userName;
    }
}
