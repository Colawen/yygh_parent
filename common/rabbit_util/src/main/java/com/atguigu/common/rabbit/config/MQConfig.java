package com.atguigu.common.rabbit.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dzwstart
 * @date 2021/11/6 - 20:04
 *
 * 说明：默认是字符串转换器
 */
@Configuration
public class MQConfig {

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }



}
