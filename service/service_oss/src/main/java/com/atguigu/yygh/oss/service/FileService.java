package com.atguigu.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author dzwstart
 * @date 2021/10/29 - 13:39
 */
public interface FileService {
    String upload(MultipartFile file);
}
