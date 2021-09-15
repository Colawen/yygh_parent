package com.atguigu.yygh.hosp.service.impl;

import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author dzwstart
 * @date 2021/8/25 - 17:30
 */
@Service
public class HospitalSetServiceImpl  extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Override
    public String getSignKey(String hoscode) {

        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("hoscode",hoscode);

        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);



        return hospitalSet.getSignKey();
    }
}
