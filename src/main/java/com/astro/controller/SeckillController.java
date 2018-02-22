package com.astro.controller;

import com.astro.VO.GoodsVo;
import com.astro.domain.OrderInfo;
import com.astro.domain.SeckillOrder;
import com.astro.domain.User;
import com.astro.rabbitmq.MQSender;
import com.astro.rabbitmq.SeckillMsg;
import com.astro.redis.GoodsKey;
import com.astro.redis.RedisService;
import com.astro.result.CodeMsg;
import com.astro.result.Result;
import com.astro.service.GoodsService;
import com.astro.service.OrderService;
import com.astro.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by astro on 2018/2/16.
 */
@Controller
@RequestMapping("seckill")
@Slf4j
public class SeckillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    /*
    * 系统初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        if (goodsVoList == null) {
            return;
        }
        log.info("-----------执行了init");
        for (GoodsVo goods : goodsVoList) {
            redisService.set(GoodsKey.getSeckillGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(),false);
        }

    }

    //b7797cce01b4b131b433b6acf4add449
    //添加rabbitMq
    @RequestMapping("/do_seckill")
    @ResponseBody
    public Result<Integer> lis(Model model, User user,
                               @RequestParam("goodsId") long goodId) {
        //判断登录状态
        if (user == null) {
            return Result.errer(CodeMsg.SESSION_ERROR);
        }

        Boolean isOver = localOverMap.get(goodId);
        if (isOver){
            return Result.errer(CodeMsg.SECKILL_OVER);
        }
        //判断是否多次秒杀
        log.info("-------判断是否多次秒杀");
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodId);
        if (order != null) {
            return Result.errer(CodeMsg.SECKILL_REPEATE);
        }
        //判断库存
        //预减库存
        log.info("-------预减库存");
        Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodId);

        if (stock < 0) {
            localOverMap.put(goodId,true);
            return Result.errer(CodeMsg.SECKILL_OVER);
        }
        //入队
        log.info("--------入队");
        SeckillMsg sm = new SeckillMsg();
        sm.setUser(user);
        sm.setGoodsId(goodId);
        mqSender.sendSeckillMsg(sm);
        //排队中
        return Result.success(0);
    }

    /*
    * 返回秒杀结果
    * 成功：orderId
    * 失败：-1
    * 排队中：0
    * */
    @GetMapping("/result")
    @ResponseBody
    public Result<Long> seckillResult(Model model, User user,
                                      @RequestParam("goodsId") long goodId) {
        //判断登录状态
        if (user == null) {
            return Result.errer(CodeMsg.SESSION_ERROR);
        }
        long result = seckillService.getSeckillResult(user.getId(), goodId);
        return Result.success(result);

    }


    //页面静态化 直接返回Result（data）
    @RequestMapping("/do_seckill1")
    @ResponseBody
    public Result<OrderInfo> list(Model model, User user,
                                  @RequestParam("goodsId") long goodId) {
        //判断登录状态
        if (user == null) {
            return Result.errer(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodId);
        int stock = goods.getGoodsStock();
        if (stock <= 0) {
            return Result.errer(CodeMsg.SECKILL_OVER);
        }
        //判断是否多次秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodId);
        if (order != null) {
            return Result.errer(CodeMsg.SECKILL_REPEATE);
        }
        //秒杀成功
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        return Result.success(orderInfo);
    }

    //最初始
    //用thymeleaf
    @RequestMapping("/do_seckill2")
    @ResponseBody
    public String list2(Model model, User user,
                        @RequestParam("goodsId") long goodId) {
        if (user == null) {
            return "login";
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodId);
        int stock = goods.getGoodsStock();
        if (stock <= 0) {
            model.addAttribute("errMsg", CodeMsg.SECKILL_OVER.getMsg());
            return "seckill_fail";

        }
        //判断是否多次秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodId);
        if (order != null) {
            model.addAttribute("errMsg", CodeMsg.SECKILL_REPEATE.getMsg());
            return "seckill_fail";

        }
        //秒杀成功
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";

    }


}
