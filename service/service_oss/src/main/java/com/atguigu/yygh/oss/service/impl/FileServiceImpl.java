package com.atguigu.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.oss.service.FileService;
import com.atguigu.yygh.oss.utils.ConstantOssPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author dzwstart
 * @date 2021/10/29 - 13:39
 */

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file) {

        String endpoint= ConstantOssPropertiesUtils.ENDPOINT;

        String accessKeyId=ConstantOssPropertiesUtils.ACCESS_KEY_ID;

        String accesKeySecret=ConstantOssPropertiesUtils.SECRET;

        String bucketName=ConstantOssPropertiesUtils.BUCKET;


        try {
            //创建OSSclient实例
            OSS ossClient=new OSSClientBuilder().build(endpoint,accessKeyId,accesKeySecret);

            //上传文件流
            InputStream inputStream=file.getInputStream();

            String fileName=file.getOriginalFilename();//路径加文件名  1.jpg /a/b/1.jpg

            //生成随机唯一值，使用uuid，添加到文件名称里面  uuid ：99wa-8800-772s  去掉里面的“-” 生成99wa8800772s
            String uuid= UUID.randomUUID().toString().replaceAll("-","");
            fileName=uuid+fileName;

            //按照当前的日期创建文件夹，上传到创建文件夹里面
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            fileName=timeUrl+"/"+fileName;

            //调用方法实现上传
            ossClient.putObject(bucketName,fileName,inputStream);


            //关闭OSSClient
            ossClient.shutdown();

            //返回上传之后的文件路径
            //https://yygh-dzw.oss-cn-shenzhen.aliyuncs.com/QQ%E5%9B%BE%E7%89%8720190703192137.jpg
            String url="https://"+bucketName+"."+endpoint+"/"+fileName;




            return url;


        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }



    }
}
