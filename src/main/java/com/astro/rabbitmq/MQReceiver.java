package com.astro.rabbitmq;

import com.astro.VO.GoodsVo;
import com.astro.domain.SeckillOrder;
import com.astro.domain.User;
import com.astro.redis.RedisService;
import com.astro.result.CodeMsg;
import com.astro.result.Result;
import com.astro.service.GoodsService;
import com.astro.service.OrderService;
import com.astro.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by astro on 2018/2/21.
 */
@Service
@Slf4j
public class MQReceiver {


    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisService redisService;


    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receive(String msg) {
        log.info("--------------receive msg:" + msg);
        SeckillMsg sm = RedisService.stringToBean(msg, SeckillMsg.class);
        User user = sm.getUser();
        long goodsId = sm.getGoodsId();


        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getGoodsStock();
        if (stock <= 0) {
            return;
        }
        //判断是否多次秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        seckillService.seckill(user, goods);


    }

//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String msg){
//        log.info("---------receive msg:"+msg);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String msg){
//        log.info("---------receive topic queue1 msg:"+msg);
//    }
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String msg){
//        log.info("---------receive topic queue2 msg:"+msg);
//    }
//
//    @RabbitListener(queues = MQConfig.FANOUT_QUEUE1)
//    public void receiveFanout1(String msg){
//        log.info("---------receive fanout queue1 msg:"+msg);
//    }
//    @RabbitListener(queues = MQConfig.FANOUT_QUEUE2)
//    public void receiveFanout2(String msg){
//        log.info("---------receive fanout queue2 msg:"+msg);
//     }

}
