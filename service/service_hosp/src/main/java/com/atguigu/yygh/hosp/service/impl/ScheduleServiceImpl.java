package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/9/14 - 20:42
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private  ScheduleRepository scheduleRepository;

    //上传排班
    @Override
    public void save(Map<String, Object> schMap) {

        String toJSONString = JSONObject.toJSONString(schMap);
        Schedule schedule=JSONObject.parseObject(toJSONString,Schedule.class);

        Schedule scheduleExist=scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());

        if(scheduleExist!=null){
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleExist.setStatus(1);
             scheduleRepository.save(scheduleExist);


        }else{

            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setStatus(1);
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);


        }





    }


    //查询排班
    @Override
    public Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {

        Pageable pageable= PageRequest.of(page-1,limit);


        ExampleMatcher matcher=ExampleMatcher.matching()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase(true);

        Schedule schedule=new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        schedule.setIsDeleted(0);



        Example<Schedule> example=Example.of(schedule,matcher);



        Page<Schedule> all=scheduleRepository.findAll(example,pageable);


        return all;
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {



        //根据医院编号 和 科室编号查询科室信息
        Schedule schedule = scheduleRepository.getDepartmentByHoscodeAndHosScheduleId(hoscode, hosScheduleId);

        if(schedule!=null){

            //调用方法删除
            scheduleRepository.deleteById(schedule.getId());
        }

    }
}
