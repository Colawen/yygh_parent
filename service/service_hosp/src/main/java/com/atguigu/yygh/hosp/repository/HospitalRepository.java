package com.atguigu.yygh.hosp.repository;


import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dzwstart
 * @date 2021/9/10 - 18:11
 */

@Repository
public interface HospitalRepository  extends MongoRepository<Hospital,String> {

    Hospital getHospitalByHoscode(String hoscode);
}
