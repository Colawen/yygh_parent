package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;


import java.util.Map;

/**
 * @author dzwstart
 * @date 2021/9/14 - 16:40
 */
public interface DepartmentService {
    //上传科室
    void save(Map<String, Object> switchMap);

    //查询科室
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    //删除科室
    void remove(String hoscode, String depcode);
}
