package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/10/13 - 16:00
 */
public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> Login(LoginVo loginVo);

    UserInfo selectWxInfoOpenid(String openid);
}
