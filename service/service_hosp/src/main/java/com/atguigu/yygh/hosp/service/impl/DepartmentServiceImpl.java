package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    //根据医院编号查询所有科室列表
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建list 集合 用于最终封装
        List<DepartmentVo> resultList=new ArrayList<>();

        //根据医院编号，查询医院所有科室信息
        Department departmentQuery=new Department();
        departmentQuery.setHoscode(hoscode);
        Example example=Example.of(departmentQuery);


        //该医院编号下所有科室列表
        List<Department> all = departmentRepository.findAll(example);

        //使用jdk1.8新特性使用流针对我们的集合和数组进行筛选计算以及结果输出，不改变all,根据大科室编号 bigcode分组，获取每个大科室里面的下级子科室

        Map<String, List<Department>> departmentCollect = all.stream().collect(Collectors.groupingBy(Department::getBigcode));

        //遍历map集合  departmentCollect

        for(Map.Entry<String,List<Department>> entry:departmentCollect.entrySet()){
            //大科室编号
            String bigcode = entry.getKey();

            //大科室编号对应的全局数据
            List<Department> departmentList = entry.getValue();

            //封装大科室
            DepartmentVo departmentVo=new DepartmentVo();

            departmentVo.setDepcode(bigcode);
            departmentVo.setDepname(departmentList.get(0).getBigname());


            //封装小科室
            List<DepartmentVo> children =new ArrayList<>();

            for(Department department:departmentList){
                DepartmentVo departmentVo1=new DepartmentVo();
                departmentVo1.setDepcode(department.getDepcode());
                departmentVo1.setDepname(department.getDepname());
                //封装到list集合
                children.add(departmentVo1);

            }
            //把小科室list集合放到大科室children里面
            departmentVo.setChildren(children);

            //放到最终result里面
            resultList.add(departmentVo);


        }




        return resultList;
    }

    //根据科室编号，和医院编号，查询科室名称
    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department != null) {
            return department.getDepname();
        }

        return null;
    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        return department;
    }
}
