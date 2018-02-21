package com.astro.service;

import com.astro.VO.GoodsVo;
import com.astro.domain.OrderInfo;
import com.astro.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by astro on 2018/2/16.
 */
@Service
public class SeckillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods) {
        //减库存
        goodsService.reduceStock(goods);
        //写order_info  seckill_order
       return orderService.createOrder(user,goods);


    }
}
