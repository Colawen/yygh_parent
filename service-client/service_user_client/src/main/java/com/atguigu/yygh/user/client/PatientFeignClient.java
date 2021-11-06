package com.atguigu.yygh.user.client;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author dzwstart
 * @date 2021/11/6 - 17:03
 */

@FeignClient(value = "service-user")
@Repository
public interface PatientFeignClient {

    //根据就诊人id获取就诊人信息
    @GetMapping("/api/user/patient/inner/get/{id}")
    Patient getPatientOrder(@PathVariable("id") Long id);




}
