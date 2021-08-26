package com.atguigu.yygh.common.exception;

import com.atguigu.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dzwstart
 * @date 2021/8/26 - 21:19
 *
 * 全局异常处理 返回统一异常界面
 * @ControllerAdvice
 * Exception e所以异常都进行error方法
 *  @ExceptionHandler 统一异常类型（如class）触发error方法
 *  @ResponseBody 使得 结果用json输出
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){

        e.printStackTrace();
        return Result.fail();

    }

    //自定义异常处理
    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e){
        e.printStackTrace();

        return Result.fail();

    }


}
