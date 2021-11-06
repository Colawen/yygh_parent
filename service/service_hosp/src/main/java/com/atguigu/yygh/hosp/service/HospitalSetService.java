package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.order.SignInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dzwstart
 * @date 2021/8/25 - 17:28
 */
public interface HospitalSetService extends IService<HospitalSet> {



    String getSignKey(String hoscode);

    //"获取医院签名信息"
    SignInfoVo getSignInfoVo(String hoscode);
}
