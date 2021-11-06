package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.order.SignInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author dzwstart
 * @date 2021/9/10 - 18:14
 */

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;



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




    //查询医院列表
    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {

        //创建pageable 对象
        Pageable pageable= PageRequest.of(page-1,limit);

        //创建ExampleMatcher

        ExampleMatcher matcher=ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        //创建Example

        Hospital hospital=new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        hospital.setIsDeleted(0);


        Example<Hospital> example=Example.of(hospital,matcher);

        //调用mongodb方法查询分页
        Page<Hospital> all = hospitalRepository.findAll(example, pageable);


        //获取查询list集合，遍历进行医院等级封装


        all.getContent().stream().forEach(item->{

            this.setHospitalHostype(item);
        });




        return all;
    }


    //更新医院状态

    @Override
    public void updateStatus(String id, Integer status) {
        //根据id查询MongoDB的值
        Hospital hospital = hospitalRepository.findById(id).get();


        //设置需要修改的值
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());

        hospitalRepository.save(hospital);


    }



    @Override
    public Map<String,Object> getHospById(String id) {
        Map<String,Object> result=new HashMap<>();

        Hospital hospital = this.setHospitalHostype(hospitalRepository.findById(id).get());

        //医院基本信息
        result.put("hospital",hospital);

        //单独处理更直观
        result.put("bookingRule",hospital.getBookingRule());

        //不需要重复返回
        hospital.setBookingRule(null);



        return result;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if(hospital!=null){
            return  hospital.getHosname();
        }
        return null;
    }


    //根据医院名称进行查询
    @Override
    public List<Hospital> findByHosName(String hosname) {

       List<Hospital> list= hospitalRepository.findHospitalByHosnameLike(hosname);

        return list;
    }


    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalHostype(this.getByHoscode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;

    }




    //获取查询list集合，遍历进行医院等级封装
    private Hospital setHospitalHostype(Hospital item) {
        //根据dictCode和value获取医院等级名称
     String hostypeString= dictFeignClient.getName("Hostype",item.getHostype());

      //查询省 市 地区
        String provinceCodeString=  dictFeignClient.getName(item.getProvinceCode());
        String cityCodeString=  dictFeignClient.getName(item.getCityCode());
        String districtCodeString=  dictFeignClient.getName(item.getDistrictCode());

        item.getParam().put("fullAddress",provinceCodeString+cityCodeString+districtCodeString);
        item.getParam().put("hostypeString",hostypeString);

        return  item;
    }
}
