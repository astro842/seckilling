package com.astro.controller;

import com.astro.VO.GoodsVo;
import com.astro.domain.OrderInfo;
import com.astro.domain.SeckillOrder;
import com.astro.domain.User;
import com.astro.result.CodeMsg;
import com.astro.result.Result;
import com.astro.service.GoodsService;
import com.astro.service.OrderService;
import com.astro.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by astro on 2018/2/16.
 */
@Controller
@RequestMapping("seckill")
public class SeckillController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SeckillService seckillService;

    @RequestMapping("/do_seckill")
    @ResponseBody
    public Result<OrderInfo> list(Model model, User user,
                       @RequestParam("goodsId") long goodId) {
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
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);
        return "order_detail";

    }

}
