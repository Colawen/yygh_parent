package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.enums.AuthStatusEnum;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/10/13 - 16:01
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Map<String, Object> Login(LoginVo loginVo) {
        //从loginvo获取输入的手机号和验证码
        String phone=loginVo.getPhone();
        String code=loginVo.getCode();


        //判断手机号和验证码是否为空
        if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(code)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //判断手机验证码和输入的验证码是否一致
        String mobleCode= redisTemplate.opsForValue().get(phone);

        if(!code.equals(mobleCode)){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }


        //绑定手机号

        UserInfo userInfo=null;
        if(!StringUtils.isEmpty(loginVo.getOpenid())){
            userInfo=this.selectWxInfoOpenid(loginVo.getOpenid());
            if(null!=userInfo){
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            }else{
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }


        if(userInfo==null){
            //判断是否第一次登入：根据手机号查询数据库，没有就注册，有就登入

            QueryWrapper<UserInfo> wrapper=new QueryWrapper<>();

            wrapper.eq("phone",phone);


             userInfo= baseMapper.selectOne(wrapper);

            if(userInfo==null){//第一次使用这个手机号登入
                //添加信息到数据库
                userInfo=new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);

            }

        }





        //校验是否被禁用
        if(userInfo.getStatus()==0){
            throw  new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //返回登录信息
        //返回登录用户名
        //返回token
        Map<String,Object> map=new HashMap<>();
        String name=userInfo.getName();

        if(StringUtils.isEmpty(name)){
            name=userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)){
            name=userInfo.getPhone();
        }

        map.put("name",name);
        //jwt生成token字符串
        String token= JwtHelper.createToken(userInfo.getId(),name);
        map.put("token",token);




        return map;
    }

    @Override
    public UserInfo selectWxInfoOpenid(String openid) {

        QueryWrapper<UserInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        UserInfo userInfo=baseMapper.selectOne(queryWrapper);
        return userInfo;
    }

    //用户认证
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {

        //根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);

        //设置认证信息
        //认证姓名
        userInfo.setName(userAuthVo.getName());
        //其他认证信息
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());

        //进行信息更新
        baseMapper.updateById(userInfo);
    }
}
