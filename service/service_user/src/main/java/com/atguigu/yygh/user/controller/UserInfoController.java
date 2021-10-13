package com.atguigu.yygh.user.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/10/13 - 15:41asd
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;


    //用户手机号登入接口
    @ApiOperation(value = "登入验证")
    @PostMapping("login")
    public Result UserInfoLogin( @RequestBody LoginVo loginVo){

       Map<String,Object> map= userInfoService.Login(loginVo);

        return Result.ok(map);

    }


}
