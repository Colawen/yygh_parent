package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.enums.DictEnum;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.mapper.PatientMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dzwstart
 * @date 2021/10/31 - 14:37
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> findAllUserId(Long userId) {
        
        //根据userid查询所有就诊人的信息列表
        QueryWrapper queryWrapper=new QueryWrapper();

        queryWrapper.eq("user_id",userId);

        List<Patient> patientList = baseMapper.selectList(queryWrapper);

        //通过远程调用，得到编码对应具体内容，查询数据字典表内容

        patientList.stream().forEach(item->{
            //其他参数封装 params

            this.packPatient(item);
        });





        return patientList;
    }

    @Override
    public Patient getPatient(Long id) {

        return this.packPatient(baseMapper.selectById(id));
    }

    //其他参数封装 params
    private Patient packPatient(Patient patient) {
        //根据证件类型编码，获取证件类型具体值
       String CertificatesTypeString=  dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getCertificatesType());

       //联系人证件类型
        String ContactsCertificatesTypeString = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());


        //省
        String ProvinceCodeString = dictFeignClient.getName(patient.getProvinceCode());

        //市
        String CityString = dictFeignClient.getName(patient.getCityCode());

        //区
        String DistrictString = dictFeignClient.getName(patient.getDistrictCode());

        patient.getParam().put("certificatesTypeString", CertificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", ContactsCertificatesTypeString);
        patient.getParam().put("provinceString", ProvinceCodeString);
        patient.getParam().put("cityString", CityString);
        patient.getParam().put("districtString", DistrictString);
        patient.getParam().put("fullAddress", ProvinceCodeString + CityString + DistrictString + patient.getAddress());


        return patient;

    }
}
