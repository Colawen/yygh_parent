package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Random;

/**
 * @author dzwstart
 * @date 2021/8/25 - 17:37
 *
 * @RestController主要用到里面包含的
 * （@Controller交给spring管理 @responseBody默认返回data数据）
 *
 *
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {

    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;

    //http://localhost:8201/admin/hosp/hospitalSet/findAll
    //1查询医院设置表里的所有信息         rest风格@GetMapping


    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet(){
        //调用service的方法
        List<HospitalSet> list = hospitalSetService.list();

        return   Result.ok(list);
    }


    //2.逻辑删除医院设置  {id}自己在路径上输入想要的id http://localhost:8201/admin/hosp/hospitalSet/{id}
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    //路径传值 所以要注入@PathVariable (路径变量) @PathParam(路径传参)
    public Result removeHospSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
    if(flag){
        return Result.ok();
    }
    return Result.fail();
    }


    //3 条件查询带分页   条件（当前页，范围，封装条件） 注意 用@reqestBody后封装前端数据为json传给vo当做参数，
    // 不能使用@GetMapping得改成@PostMapping   required = false参数可为空 HospitalSetQueryVo封装的条件实体类

    @ApiOperation(value = "条件查询带分页")
    @PostMapping ("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false)  HospitalSetQueryVo hospitalSetQueryVo){
        //mp步骤 创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);
        //构建查询条件
        QueryWrapper<HospitalSet> wrapper=new QueryWrapper<>();

        String hosname=hospitalSetQueryVo.getHosname();
        String hoscode=hospitalSetQueryVo.getHoscode();

        if(!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }

        if(!StringUtils.isEmpty(hoscode)){

            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }


        //调用方法实现分页查询
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page,wrapper);

        return Result.ok(hospitalSetPage);


    }


    //4 添加医院设置窗口

    @ApiOperation(value = "添加医院设置窗口")
    @PostMapping("hospAdd")
    public Result HospAdd(@RequestBody HospitalSet hospitalSet ){
        //设置状态1使用0不能使用
        hospitalSet.setStatus(1);

        //设置签名秘钥 生成一个随机数再用MD5加密
        Random random=new Random();

        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

        //调用service
        boolean save = hospitalSetService.save(hospitalSet);

        if(save){
            return Result.ok();
        }else {
            return  Result.fail();
        }


    }


    //5 根据id获取医院设置
    @ApiOperation(value = "根据id获取医院设置")
    @GetMapping("getHospSet/{id}")
    public  Result getHospSet(@PathVariable long id){



        HospitalSet hospitalSet = hospitalSetService.getById(id);

        return Result.ok(hospitalSet);
    }

    //6 修改医院设置    不加判断id是否为空是因为 @requestBody 的 require=true 值不能为空
    @ApiOperation(value ="修改医院设置")
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);

        if(flag){
            return  Result.ok();
        }else {
            return  Result.fail();
        }


    }

    //7 批量删除医院设置
    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean ids = hospitalSetService.removeByIds(idList);

        return Result.ok();

    }

    //8 医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status){
        //根据id查询医院设置信息
        HospitalSet byId = hospitalSetService.getById(id);
        //设置状态
        byId.setStatus(status);
        //调用方法
        hospitalSetService.updateById(byId);

        return  Result.ok();

    }




    //9 发送签名秘钥
    @PutMapping("sendKey/{id}")
    public Result sendKey(@PathVariable Long id){
        //获取id医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);

        String signKey=hospitalSet.getSignKey();
        String hoscode=hospitalSet.getHoscode();

        //TODO 发送信息

        return Result.ok();

    }






}
