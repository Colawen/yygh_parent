package com.oss.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

/**
 * @author dzwstart
 * @date 2021/10/29 - 13:22
 */
public class OssTest {
    public static void main(String[] args) {

        String endpoint="https://oss-cn-shenzhen.aliyuncs.com";

        String accessKeyId="LTAI5tGdbYqRi9QNUuvV9kgi";

        String accesKeySecret="MLE4KFCpRroZRDMRU3dvsPeSEu9bIs";

        String bucketName="yygh-ossdzw";

        OSS ossClient=new OSSClientBuilder().build(endpoint,accessKeyId,accesKeySecret);

        ossClient.createBucket(bucketName);

        ossClient.shutdown();

    }
}
