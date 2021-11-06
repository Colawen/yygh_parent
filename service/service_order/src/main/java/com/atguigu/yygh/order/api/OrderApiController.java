package com.atguigu.yygh.order.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.order.service.OrderInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dzwstart
 * @date 2021/11/6 - 16:29
 */
@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    //生成挂号订单 需要排班id以及就诊人id
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")

    public Result saveOrders(@PathVariable String scheduleId,
                             @PathVariable Long patientId){


        //返回订单id
        Long orderId=orderInfoService.saveOrder(scheduleId,patientId);

        return  Result.ok(orderId);
    }

}
