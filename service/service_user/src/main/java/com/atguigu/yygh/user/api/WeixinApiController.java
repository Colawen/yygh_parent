package com.atguigu.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.ConstantWxPropertiesUtils;
import com.atguigu.yygh.user.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/10/26 - 14:16
 */

//微信操作接口
@Controller    //不用restcontroller（作用  返回json数据） 的原因是方便页面跳转 或重定向
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;


    // 生成微信扫描二维码  返回生成二维码需要的参数

    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect() {


        try {

            Map<String, Object> map = new HashMap<>();

            map.put("appid", ConstantWxPropertiesUtils.WX_OPEN_APP_ID);
            map.put("scope", "snsapi_login");

            //wxOpenRedirectUrl 需要进行URLencode编码
            String wxOpenRedirectUrl = ConstantWxPropertiesUtils.WX_OPEN_REDIRECT_URL;
            wxOpenRedirectUrl = URLEncoder.encode(wxOpenRedirectUrl, "utf-8");

            map.put("redirectUri", wxOpenRedirectUrl);
            map.put("state", System.currentTimeMillis() + "");//System.currentTimeMillis()+""
            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    //回调的方法  得到扫描人的信息

    @GetMapping("callback")
    public String callback(String code, String state) {
//获取授权临时票据
        System.out.println("微信授权服务器回调。。。。。。");
        System.out.println("state = " + state);
        System.out.println("code = " + code);


        //使用code和appid以及appscrect换取access_token
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantWxPropertiesUtils.WX_OPEN_APP_ID,
                ConstantWxPropertiesUtils.WX_OPEN_APP_SECRET,
                code);

        String result = null;
        try {   //请求httpclient请求这个地址
            result = HttpClientUtils.get(accessTokenUrl);
            System.out.println("使用code换取的access_token结果 = " + result);

            //从返回字符串获取两个值 openid 和 acess_token
            JSONObject jsonObject=JSONObject.parseObject(result);

            String access_token=jsonObject.getString("access_token");
            String openid=jsonObject.getString("openid");

            //判断数据库是否存在微信的扫码人信息
            //根据openid判断
           UserInfo userInfo= userInfoService.selectWxInfoOpenid(openid);

           if(userInfo==null){
               //第三步 拿着两个值请求微信地址获取扫码人信息
               String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                       "?access_token=%s" +
                       "&openid=%s";
               String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);

               String resultInfo=HttpClientUtils.get(userInfoUrl);

               System.out.println("resultInfo:"+resultInfo);

               JSONObject jsonObject2=JSONObject.parseObject(resultInfo);

               //解析用户

               //用户昵称
               String nickname=jsonObject2.getString("nickname");

               //用户头像
               String headimgurl=jsonObject2.getString("headimgurl");


               //获取扫码人信息添加入数据库
               userInfo=new UserInfo();
               userInfo.setNickName(nickname);
               userInfo.setOpenid(openid);
               userInfo.setStatus(1);
               userInfoService.save(userInfo);

           }

            //返回name和token字符串
            Map<String ,Object> map=new HashMap<>();
            String name=userInfo.getName();
            if(StringUtils.isEmpty(name)){
                name=userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)){
                name=userInfo.getPhone();
            }
            map.put("name",name);

            if(StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            return "redirect:" + ConstantWxPropertiesUtils.YYGH_BASE_URL + "/weixin/callback?token="+map.get("token")+"&openid="+map.get("openid")+"&name="+URLEncoder.encode((String)map.get("name"));


        } catch (Exception e) {
            throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);

        }


    }
}