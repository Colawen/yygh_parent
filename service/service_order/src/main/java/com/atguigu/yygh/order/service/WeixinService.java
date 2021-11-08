package com.atguigu.yygh.order.service;

import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/11/8 - 17:35
 */
public interface WeixinService {
    //生成微信支付二维码
    Map createNativ(Long orderId);

    //调用微信接口实现支付状态查询
    Map<String, String> queryPayStatus(Long orderId);
}
