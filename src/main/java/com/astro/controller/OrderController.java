package com.astro.controller;


import com.astro.VO.GoodsVo;
import com.astro.VO.OrderDetailVo;
import com.astro.domain.OrderInfo;
import com.astro.domain.SeckillOrder;
import com.astro.domain.User;
import com.astro.result.CodeMsg;
import com.astro.result.Result;
import com.astro.service.GoodsService;
import com.astro.service.OrderService;
import com.astro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by astro on 2018/2/13.
 */
@Controller
@RequestMapping("order")
@Slf4j
public class OrderController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, User user,
                                      @RequestParam("orderId") long orderId) {

        if (user==null){
            return Result.errer(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order==null){
            return Result.errer(CodeMsg.ORDER_ORDER_EXIST);
        }
        long goodsId=order.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo=new OrderDetailVo();
        vo.setGoods(goodsVo);
        vo.setOrder(order);
        return Result.success(vo);
    }

}
