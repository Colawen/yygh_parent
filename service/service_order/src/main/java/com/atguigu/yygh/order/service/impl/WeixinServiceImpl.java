package com.atguigu.yygh.order.service.impl;

import com.atguigu.yygh.enums.PaymentStatusEnum;
import com.atguigu.yygh.enums.PaymentTypeEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.order.service.PaymentInfoService;
import com.atguigu.yygh.order.service.WeixinService;
import com.atguigu.yygh.order.utils.ConstantPropertiesUtils;
import com.atguigu.yygh.order.utils.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author dzwstart
 * @date 2021/11/8 - 17:35
 */
@Service
public class WeixinServiceImpl implements WeixinService {


    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    //生成微信支付二维码

    @Override
    public Map createNativ(Long orderId) {


        try {
            //从redis获取数据 有的话返回payMap数据  没的话再继续
            Map payMap = (Map) redisTemplate.opsForValue().get(orderId.toString());

            if(payMap!=null){
                return payMap;
            }


            //1 根据orderId获取订单信息
            OrderInfo order=orderInfoService.getById(orderId);

            //2 支付记录添加一条信息
            paymentInfoService.savePaymentInfo(order, PaymentTypeEnum.WEIXIN.getStatus());


            //设置一些参数，调用微信生成二维码的接口

            //把参数转换xml格式，使用商户key进行加密

            //3设置参数
            Map paramMap = new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            String body = order.getReserveDate() + "就诊"+ order.getDepname();
            paramMap.put("body", body);
            paramMap.put("out_trade_no", order.getOutTradeNo());
            //paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");
            paramMap.put("total_fee", "1"); //支付金额  为了测试统一写成0.01元
            paramMap.put("spbill_create_ip", "127.0.0.1");
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            paramMap.put("trade_type", "NATIVE");


            //4 调用微信生成二维码接口，httpclient调用
            HttpClient client=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //5 设置map参数转换成xml格式
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,ConstantPropertiesUtils.PARTNERKEY));

            client.setHttps(true);
            client.post();


            //6 返回第三方的数据,如返回的字节码code和code_url
            String xml = client.getContent();
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(xml);

            //7 封装返回结果集
            Map map=new HashMap<>();
            map.put("orderId", orderId);
            map.put("totalFee", order.getAmount());
            map.put("resultCode", xmlToMap.get("result_code"));
            map.put("codeUrl", xmlToMap.get("code_url"));

            System.out.println("----"+map);
            System.out.println("resultXml"+xmlToMap);

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            if(xmlToMap.get("result_code")!=null) {
                redisTemplate.opsForValue().set(orderId.toString(), map, 120, TimeUnit.MINUTES);
            }
            return  map;



        } catch (Exception e) {
            e.printStackTrace();

            return null;

        }


    }

    //调用微信接口实现支付状态查询
    @Override
    public Map<String, String> queryPayStatus(Long orderId) {

        try {
            //1 根据orderId获取订单信息
            OrderInfo orderInfo = orderInfoService.getById(orderId);

            //2 封装提交参数

            Map paramMap=new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());


            //3 设置请求内容
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();


            //4 得到微信接口返回数据

            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            System.out.println("支付状态的resultMap"+resultMap);


            //5 把接口数据返回
            return resultMap;

        }catch (Exception e){
            return null;
        }


    }
}
