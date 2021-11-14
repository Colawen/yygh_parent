package com.atguigu.yygh.task.scheduled;

import com.atguigu.common.rabbit.constant.MqConst;
import com.atguigu.common.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dzwstart
 * @date 2021/11/14 - 19:00
 */
@Component
@EnableScheduling
public class ScheduledTask {
    @Autowired
    private RabbitService rabbitService;

    //每天8点执行方法，就医提醒
    //cron表达式，设置执行间隔
    //0 0 8 * * ?

    @Scheduled(cron = "0/30 * * * * ?")
    public void taskPatient(){
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK,MqConst.ROUTING_TASK_8,"");

    }

}
