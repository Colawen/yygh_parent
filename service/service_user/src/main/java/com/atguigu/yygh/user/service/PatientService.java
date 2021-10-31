package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dzwstart
 * @date 2021/10/31 - 14:35
 */
public interface PatientService extends IService<Patient>{
    //获取就诊人列表
    List<Patient> findAllUserId(Long userId);

    //根据id获取就诊人信息(用id 而不是userid查询)
    Patient getPatient(Long id);
}
