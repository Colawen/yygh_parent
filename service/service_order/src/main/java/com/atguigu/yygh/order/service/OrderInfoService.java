package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/11/6 - 16:22
 */
public interface OrderInfoService extends IService<OrderInfo> {

   //生成订单
    Long saveOrder(String scheduleId, Long patientId);

    //订单详情
    OrderInfo getOrder(String orderId);

    //查询订单列表
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    //取消预约
    Boolean cancelOrder(Long orderId);

    //每天8点发送就医提醒
    void patientTips();

    //预约统计
    Map<String,Object> getCountMap(OrderCountQueryVo orderCountQueryVo);

}
