package com.astro.rabbitmq;

import com.astro.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by astro on 2018/2/21.
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    RedisService redisService;

    public void sendSeckillMsg(SeckillMsg sm) {
        String msgg=redisService.beanToString(sm);
        log.info("--------------入队了---sender msg:"+msgg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE,msgg);
    }

//    public void send(Object msg){
//        String msgg=redisService.beanToString(msg);
//        log.info("--------------sender msg:"+msgg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
//    }
//
//    public void sendTopic(Object msg){
//        String msgg=redisService.beanToString(msg);
//        log.info("--------------sender topic msg:"+msgg);
//        //
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msg+"1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");
//    }
//
//    public void sendFanout(Object msg){
//        String msgg=redisService.beanToString(msg);
//        log.info("--------------sender topic msg:"+msgg);
//        //
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,msg);
//
//    }


}
