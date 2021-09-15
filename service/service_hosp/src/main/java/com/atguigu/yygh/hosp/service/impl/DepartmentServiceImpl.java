package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/9/14 - 16:41
 */

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;


    //上传科室接口
    @Override
    public void save(Map<String, Object> switchMap) {
        //switchMap转换成department对象
        String switchMapString = JSONObject.toJSONString(switchMap);
        Department department = JSONObject.parseObject(switchMapString, Department.class);

        Department departmentExist=
        departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());

        //根据医院编号 和 科室编号判断查询

        if(departmentExist!=null){
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);

        }else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);



        }





    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {

        //创建PageAble对象,设置当前页和每页记录数
        Pageable pageable= PageRequest.of(page-1,limit);

        //创建ExampleMatcher规则,模糊查询，再创建Example对象
        ExampleMatcher matcher=ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        //将departmentQueryVo复制到department对象
        Department department=new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);

        Example<Department> example=Example.of(department,matcher);

       Page<Department> all = departmentRepository.findAll(example, pageable);


        return all;
    }


    //删除科室
    @Override
    public void remove(String hoscode, String depcode) {

        //根据医院编号 和 科室编号查询科室信息
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);

        if(department!=null){

            //调用方法删除
            departmentRepository.deleteById(department.getId());
        }


    }
}
