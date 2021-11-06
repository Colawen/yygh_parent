package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/9/14 - 20:41
 */
public interface ScheduleService  extends IService<Schedule> {


    //上传排班
    void save(Map<String, Object> schMap);


    //查询排班
    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    //查询排班规则数据
    Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    //获取id获取排班信息
    Schedule getScheduleId(String scheduleId);
    //获取排班id获取预约下单数据
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);
}
