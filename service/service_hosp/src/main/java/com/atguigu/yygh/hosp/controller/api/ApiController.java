package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;

import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/9/10 - 18:21
 */

@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;


    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest httpServletRequest){
        //获取传递过来的信息
        Map<String, String[]> requestMap = httpServletRequest.getParameterMap();

        Map<String, Object> switchMap = HttpRequestHelper.switchMap(requestMap);

        //1 获取医院系统中传递过来的签名
        String hospSign = (String)switchMap.get("sign");

        //2  根据传递过来医院编码，查询数据库，查询签名
        String hoscode = (String)switchMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);


        //3 把数据库查询签名进行MD5加密

        String encrypt = MD5.encrypt(signKey);

        //4 判断

        if(!hospSign.equals(encrypt)){

            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //5 logoData图片使用了base64转换，数据里的“+”变成了“ ”空格，需要转换回来
        String logoData = (String) switchMap.get("logoData");

        String logoDataString=logoData.replaceAll(" ","+");

        switchMap.put("logoData",logoDataString);


        //调用service的方法
        hospitalService.save(switchMap);

        return  Result.ok();
    }


    //查询医院
    @PostMapping("hospital/show")
    public  Result getHospital(HttpServletRequest httpServletRequest){
        //获取传递过来的信息
        Map<String, String[]> requestParameterMap = httpServletRequest.getParameterMap();

        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(requestParameterMap);

        String hospSign = (String) stringObjectMap.get("sign");
        //获取医院编号
        String hoscode = (String) stringObjectMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        //加密
        String encrypt = MD5.encrypt(signKey);

        //判断
        if (!encrypt.equals(hospSign)){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);

        }

        //调用service方法根据医院编号进行查询
       Hospital hospital= hospitalService.getByHoscode(hoscode);


        return Result.ok(hospital);




    }


    //上传科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest httpServletRequest){
        //获取传递过来的信息
        Map<String, String[]> requestMap = httpServletRequest.getParameterMap();

        Map<String, Object> switchMap = HttpRequestHelper.switchMap(requestMap);

        //1 获取医院系统中传递过来的签名
        String hospSign = (String)switchMap.get("sign");

        //2  根据传递过来医院编码，查询数据库，查询签名
        String hoscode = (String)switchMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);


        //3 把数据库查询签名进行MD5加密

        String encrypt = MD5.encrypt(signKey);

        //4 判断

        if(!hospSign.equals(encrypt)){

            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }




        //调用service的方法
        departmentService.save(switchMap);

        return  Result.ok();
    }

    //查询科室接口

    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest httpServletRequest){
        //获取传递过来的科室信息
        Map<String, String[]> requestParameterMap = httpServletRequest.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //获取医院编号
        String hoscode = (String) objectMap.get("hoscode");


        //当前页 和 每页记录数设置赋值条件
        int page = StringUtils.isEmpty(objectMap.get("page"))?1:Integer.parseInt((String) objectMap.get("page"));


        int limit = StringUtils.isEmpty(objectMap.get("limit"))?1:Integer.parseInt((String) objectMap.get("limit"));


        //秘钥判断

        //1 获取医院系统中传递过来的签名
        String hospSign = (String)objectMap.get("sign");

        //2  根据传递过来医院编码，查询数据库，查询签名
        String signKey=hospitalSetService.getSignKey(hoscode);


        //3 把数据库查询签名进行MD5加密

        String encrypt = MD5.encrypt(signKey);

        //4 判断

        if(!hospSign.equals(encrypt)){

            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }


        //封装查询条件
        DepartmentQueryVo departmentQueryVo=new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        //调用service方法
        Page<Department> pageModel=departmentService.findPageDepartment(page,limit,departmentQueryVo);





        return  Result.ok(pageModel);
    }


    //删除科室接口
    @PostMapping("department/remove")

    public Result removeDepartment(HttpServletRequest httpServletRequest){
        //获取传递过来的科室信息
        Map<String, String[]> requestParameterMap = httpServletRequest.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //获取医院编号
        String hoscode = (String) objectMap.get("hoscode");
        String depcode= (String) objectMap.get("depcode");




        //秘钥判断

        //1 获取医院系统中传递过来的签名
        String hospSign = (String)objectMap.get("sign");

        //2  根据传递过来医院编码，查询数据库，查询签名
        String signKey=hospitalSetService.getSignKey(hoscode);


        //3 把数据库查询签名进行MD5加密

        String encrypt = MD5.encrypt(signKey);

        //4 判断

        if(!hospSign.equals(encrypt)){

            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.remove(hoscode,depcode);



        return  Result.ok();
    }





    //上传排班接口

    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest httpServletRequest){
        //获取传过来的排班信息
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        Map<String, Object> schMap = HttpRequestHelper.switchMap(parameterMap);

        String hoscode = (String) schMap.get("hoscode");

        String sign = (String) schMap.get("sign");

        String signKey = hospitalSetService.getSignKey(hoscode);

        String signMD = MD5.encrypt(signKey);

        if(!sign.equals(signMD)){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }


        scheduleService.save(schMap);






        return  Result.ok();
    }



    //查询排班信息
    @PostMapping("schedule/list")
        public Result findSchedule(HttpServletRequest request){

        //获取传过来的排班信息
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> schMap = HttpRequestHelper.switchMap(parameterMap);


        //获取医院编号
        String hoscode = (String) schMap.get("hoscode");

        //当前页 和 每页记录数设置赋值条件
        int page = StringUtils.isEmpty(schMap.get("page"))?1:Integer.parseInt((String) schMap.get("page"));


        int limit = StringUtils.isEmpty(schMap.get("limit"))?1:Integer.parseInt((String) schMap.get("limit"));


        //秘钥判断

        //1 获取医院系统中传递过来的签名
        String hospSign = (String)schMap.get("sign");

        //2  根据传递过来医院编码，查询数据库，查询签名
        String signKey=hospitalSetService.getSignKey(hoscode);


        //3 把数据库查询签名进行MD5加密

        String encrypt = MD5.encrypt(signKey);

        //4 判断

        if(!hospSign.equals(encrypt)){

            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //封装查询语句
        ScheduleQueryVo scheduleQueryVo=new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);


        Page<Schedule> pageSchedule= scheduleService.findPageSchedule(page,limit,scheduleQueryVo);





        return Result.ok(pageSchedule);

        }


        //删除排班信息


    @PostMapping("schedule/remove")
    public  Result remove(HttpServletRequest request){

        //获取传递过来的科室信息
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //获取医院编号
        String hoscode = (String) objectMap.get("hoscode");
        String hosScheduleId= (String) objectMap.get("hosScheduleId");




        //秘钥判断

        //1 获取医院系统中传递过来的签名
        String hospSign = (String)objectMap.get("sign");

        //2  根据传递过来医院编码，查询数据库，查询签名
        String signKey=hospitalSetService.getSignKey(hoscode);


        //3 把数据库查询签名进行MD5加密

        String encrypt = MD5.encrypt(signKey);

        //4 判断

        if(!hospSign.equals(encrypt)){

            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.remove(hoscode,hosScheduleId);



        return  Result.ok();
    }









}
