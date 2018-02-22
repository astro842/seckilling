package com.astro.service;

import com.astro.VO.GoodsVo;
import com.astro.dao.GoodsDao;
import com.astro.dao.OrderDao;
import com.astro.domain.OrderInfo;
import com.astro.domain.SeckillOrder;
import com.astro.domain.User;
import com.astro.redis.OrderKey;
import com.astro.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by astro on 2018/2/15.
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisService redisService;


    public SeckillOrder getSeckillOrderByUserIdGoodsId(Long userId, long goodId) {
        //return orderDao.getSeckillOrderByUserIdGoodsId(userId,goodId);
       return redisService.get(OrderKey.getSeckillOrderByUidGid,""+userId+"_"+goodId,SeckillOrder.class);
    }

    public OrderInfo getOrderById(long orderId){
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0l);
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());

        orderDao.insert(orderInfo);

        //seckill order
        SeckillOrder order=new SeckillOrder();
        order.setGoodsId(goods.getId());
        order.setUserId(user.getId());
        order.setOrderId(orderInfo.getId());
        orderDao.insertSeckillOrder(order);

        redisService.set(OrderKey.getSeckillOrderByUidGid,""+user.getId()+"_"+goods.getId(),order);
        return orderInfo;
    }
}
