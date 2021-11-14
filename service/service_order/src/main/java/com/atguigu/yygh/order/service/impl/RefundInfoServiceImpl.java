package com.atguigu.yygh.order.service.impl;

import com.atguigu.yygh.enums.RefundStatusEnum;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.model.order.RefundInfo;
import com.atguigu.yygh.order.mapper.RefundInfoMapper;
import com.atguigu.yygh.order.service.RefundInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author dzwstart
 * @date 2021/11/14 - 15:02
 */
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {

    /**
     * 保存退款记录
     * @param paymentInfo
     */
    @Override
    public RefundInfo saveRefundInfo(PaymentInfo paymentInfo) {
        //判断是否有重复数据
        QueryWrapper<RefundInfo> wrapper=new QueryWrapper<>();
        wrapper.eq("order_Id",paymentInfo.getOrderId());
        wrapper.eq("payment_type",paymentInfo.getPaymentType());
        RefundInfo refundInfo = baseMapper.selectOne(wrapper);

        if(refundInfo!=null){//有相同数据
            return refundInfo;
        }
        //添加记录
        refundInfo=new RefundInfo();
        refundInfo.setCreateTime(new Date());
        refundInfo.setOrderId(paymentInfo.getOrderId());
        refundInfo.setPaymentType(paymentInfo.getPaymentType());
        refundInfo.setOutTradeNo(paymentInfo.getOutTradeNo());
        refundInfo.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());
        refundInfo.setSubject(paymentInfo.getSubject());
        refundInfo.setTotalAmount(paymentInfo.getTotalAmount());

         baseMapper.insert(refundInfo);


        return refundInfo;
    }
}
