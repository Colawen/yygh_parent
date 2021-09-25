package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/9/25 - 17:27 洒dddasd
 */
@RestController
@RequestMapping("/admin/hosp/schedule")
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService  scheduleService;


    //根据医院编号 和科室编号 ， 查询排班规则数据
    @ApiOperation(value = "查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable Integer page,
                                  @PathVariable Integer limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){

                Map<String,Object> map= scheduleService.getScheduleRule(page,limit,hoscode,depcode);


        return  Result.ok(map);
    }

    //根据医院编号，科室编号和工作日期，查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public  Result getScheduleDetail(@PathVariable String hoscode,
                                     @PathVariable String depcode,
                                     @PathVariable String workDate){


      List<Schedule> list= scheduleService.getDetailSchedule(hoscode,depcode,workDate);



        return  Result.ok(list);
    }



}
