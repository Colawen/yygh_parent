package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author dzwstart
 * @date 2021/9/14 - 20:40
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {


   Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId) ;

    Schedule getDepartmentByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
