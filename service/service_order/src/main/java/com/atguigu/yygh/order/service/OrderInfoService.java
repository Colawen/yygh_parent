package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dzwstart
 * @date 2021/11/6 - 16:22
 */
public interface OrderInfoService extends IService<OrderInfo> {

   //生成订单
    Long saveOrder(String scheduleId, Long patientId);
}
