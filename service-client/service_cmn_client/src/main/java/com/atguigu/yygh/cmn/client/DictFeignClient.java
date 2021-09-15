package com.atguigu.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author dzwstart
 * @date 2021/9/15 - 18:19
 *数据字典API接口
 *
 *
 */


@FeignClient("service-cmn")
public interface DictFeignClient {

    //根据dictcode和value查询
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
     String getName(@PathVariable("dictCode") String dictCode,@PathVariable("value") String value) ;

    //根据value查询
    @GetMapping("/admin/cmn/dict/getName/{value}")
     String getName(@PathVariable("value") String value);

}
