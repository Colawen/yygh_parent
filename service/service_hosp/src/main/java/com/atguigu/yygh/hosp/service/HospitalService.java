package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.order.SignInfoVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/9/10 - 18:13
 */
public interface HospitalService {

    //上传医院的方法
    void save(Map<String, Object> switchMap);


    Hospital getByHoscode(String hoscode);

    //查询医院
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);


    //更新上线功能
    void updateStatus(String id, Integer status);


    //医院详细信息
    Map<String,Object> getHospById(String id);

    String getHospName(String hoscode);

    //根据医院名称进行查询
    List<Hospital> findByHosName(String hosname);

    Map<String, Object> item(String hoscode);


}
