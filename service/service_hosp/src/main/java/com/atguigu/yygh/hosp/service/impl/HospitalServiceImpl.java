package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/9/10 - 18:14
 */

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> switchMap) {
        //把参数map集合转换成string字符串再转换成对象 Hospital
        String toJSONString = JSONObject.toJSONString(switchMap);
        Hospital hospital = JSONObject.parseObject(toJSONString, Hospital.class);//传参信息

        //判断数据是否已经存在,使用mongodb特性，查询方法名使用find、get、read命名即可无需写代码进行查询

        String hoscode=hospital.getHoscode();
        Hospital hospitalIf=hospitalRepository.getHospitalByHoscode(hoscode);//数据库已有信息

        //如果存储进行修改

        if(hospitalIf != null){
            hospital.setStatus(hospitalIf.getStatus());
            hospital.setCreateTime(hospitalIf.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);

            hospitalRepository.save(hospital);

        }else{//不存在就添加

            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);

            hospitalRepository.save(hospital);



        }




    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital=hospitalRepository.getHospitalByHoscode(hoscode);



        return hospital;
    }
}
