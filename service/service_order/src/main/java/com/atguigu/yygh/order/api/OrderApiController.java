package com.atguigu.yygh.order.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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


    //根据订单id查询订单详情
    @GetMapping("auth/getOrders/{orderId}")
     public Result getOrders(@PathVariable String orderId){

        OrderInfo orderInfo=orderInfoService.getOrder(orderId);

        return  Result.ok(orderInfo);
    }


    //订单列表（条件查询带分页）
    @GetMapping("auth/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       OrderQueryVo orderQueryVo,
                       HttpServletRequest request){

        //设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));

        //mp步骤 创建page对象，传递当前页，每页记录数
        Page<OrderInfo> pageParam=new Page<>(page,limit);

        //调用方法进行查询分页
        IPage<OrderInfo> pageModel=orderInfoService.selectPage(pageParam,orderQueryVo);

        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取订单状态")
    @GetMapping("auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    //取消预约
    @GetMapping("auth/cancelOrder/{orderId}")

    public Result cancelOrder(@PathVariable Long orderId){

        Boolean isOrder=  orderInfoService.cancelOrder(orderId);


        return  Result.ok(isOrder);
    }

    //获取订单统计数据

    @ApiOperation(value = "获取订单统计数据")
    @PostMapping("inner/getCountMap")
    public Map<String,Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo){

        return orderInfoService.getCountMap(orderCountQueryVo);
    }



}
