package com.atguigu.yygh.user.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author dzwstart
 * @date 2021/10/31 - 14:34
 */
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;
    //获取就诊人列表
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request){
        //获取当前登录用户的userid  调用工具类获取Http的header获取
        Long userId = AuthContextHolder.getUserId(request);


       List<Patient> list= patientService.findAllUserId(userId);

        return Result.ok(list);
    }


    //添加就诊人
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient,HttpServletRequest request){

        //获取当前登录用户的userid  调用工具类获取Http的header获取
        Long userId = AuthContextHolder.getUserId(request);

        patient.setUserId(userId);

        patientService.save(patient);

        return  Result.ok();

    }

    //根据id获取就诊人信息(用id 而不是userid查询)
    @GetMapping("auth/get/{id}")
    public  Result getPatient(@PathVariable Long id){

      Patient patient=  patientService.getPatient(id);
        return  Result.ok(patient);
    }



    //修改就诊人
    @PostMapping("auth/update")
    public  Result updatePatient(@RequestBody  Patient patient){

         patientService.updateById(patient);
        return  Result.ok();
    }

    //删除就诊人
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id){
        patientService.removeById(id);

        return  Result.ok();
    }



}
