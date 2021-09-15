package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;

import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/9/10 - 18:13
 */
public interface HospitalService {

    //上传医院的方法
    void save(Map<String, Object> switchMap);

    Hospital getByHoscode(String hoscode);
}
