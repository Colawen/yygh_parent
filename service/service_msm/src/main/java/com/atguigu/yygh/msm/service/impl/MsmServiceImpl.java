package com.atguigu.yygh.msm.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.yygh.msm.service.MsmService;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.atguigu.yygh.msm.utils.ConstantPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author dzwstart
 * @date 2021/10/16 - 13:49
 */
@Service
public class MsmServiceImpl implements MsmService {

    @Override
    public boolean send(String phone, String code) {

        //判断手机号是否为空
        if(StringUtils.isEmpty(phone)){
            return false;
        }
            DefaultProfile profile = DefaultProfile.getProfile(ConstantPropertiesUtils.REGION_ID, ConstantPropertiesUtils.ACCESS_KEY_ID, ConstantPropertiesUtils.SECRET);
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            // 发给谁  ----
            request.putQueryParameter("PhoneNumbers", phone);
            // 签名名称
            request.putQueryParameter("SignName", "薄荷园项目");
            // 模板名称
            request.putQueryParameter("TemplateCode", "SMS_171857472");
            // 验证码 ----
            //request.putQueryParameter("TemplateParam", "{\"code\":\"" + map.get("code") + "\"}");

        Map<String,Object> param = new HashMap();
        param.put("code",code);

        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
            try {
                CommonResponse response = client.getCommonResponse(request);
                boolean success = response.getHttpResponse().isSuccess();
                return success;
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
            return false;




    }
}
