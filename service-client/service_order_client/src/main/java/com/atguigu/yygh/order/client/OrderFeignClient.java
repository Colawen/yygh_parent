package com.atguigu.yygh.order.client;

import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/11/14 - 21:53
 */
@FeignClient(value = "service-order")
@Repository
public interface OrderFeignClient {

    //获取订单统计数据
    @PostMapping("/api/order/orderInfo/inner/getCountMap")
    public Map<String,Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo);

}
