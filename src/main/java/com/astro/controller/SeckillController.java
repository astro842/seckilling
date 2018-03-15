package com.astro.controller;

import com.astro.Util.MD5Util;
import com.astro.Util.UUIDUtil;
import com.astro.VO.GoodsVo;
import com.astro.access.AccessLimit;
import com.astro.domain.OrderInfo;
import com.astro.domain.SeckillOrder;
import com.astro.domain.User;
import com.astro.rabbitmq.MQSender;
import com.astro.rabbitmq.SeckillMsg;
import com.astro.redis.AccessKey;
import com.astro.redis.GoodsKey;
import com.astro.redis.RedisService;
import com.astro.redis.SeckillKey;
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
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
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
            localOverMap.put(goods.getId(), false);
        }

    }

    @AccessLimit(seconds = 10, maxCount = 2, needLogin = true)
    @RequestMapping("/path")
    @ResponseBody
    public Result<String> getSeckillPath(HttpServletRequest request, User user,
                                         @RequestParam("goodsId") long goodId,
                                         @RequestParam(value = "verifyCode") int verifyCode) {
        //判断登录状态
        if (user == null) {
            return Result.errer(CodeMsg.SESSION_ERROR);
        }
        //查询访问次数
//        String uri = request.getRequestURI();
//        StringBuffer url=request.getRequestURL();
//        log.info("------------uri:"+uri);
//        log.info("------------url:"+url);
//        String key=uri+"_"+user.getId();
//        Integer count = redisService.get(AccessKey.accessKey, key, Integer.class);
//        if (count==null){
//            redisService.set(AccessKey.accessKey, key, 1);
//        }else if(count<5){
//            redisService.incr(AccessKey.accessKey,key);
//        }else {
//            return Result.errer(CodeMsg.ACCESS_LIMIT);
//        }


        //验证验证码
        boolean check = seckillService.checkVerifyCode(user, goodId, verifyCode);
        if (!check) {
            return Result.errer(CodeMsg.VERIFYCODE_ERROR);
        }
        //生成path
        String path = seckillService.createPath(user, goodId);
        return Result.success(path);
    }


    //b7797cce01b4b131b433b6acf4add449
    //添加rabbitMq
    @RequestMapping("/{path}/do_seckill")
    @ResponseBody
    public Result<Integer> lis(Model model, User user,
                               @RequestParam("goodsId") long goodId,
                               @PathVariable("path") String path) {
        //判断登录状态
        if (user == null) {
            return Result.errer(CodeMsg.SESSION_ERROR);
        }
        //验证 path
        boolean check = seckillService.checkPath(user, goodId, path);
        if (!check) {
            return Result.errer(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存標記 较少redis访问
        Boolean isOver = localOverMap.get(goodId);
        if (isOver) {
            return Result.errer(CodeMsg.SECKILL_OVER);
        }
        //判断是否多次秒杀
        log.info("-------判断是否多次秒杀(redis)");
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodId);
        if (order != null) {
            return Result.errer(CodeMsg.SECKILL_REPEATE);
        }
        //判断库存
        //预减库存
        log.info("-------预减库存");
        Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodId);

        if (stock < 0) {
            localOverMap.put(goodId, true);
            return Result.errer(CodeMsg.SECKILL_OVER);
        }


        //入队---------------------------------------
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

    @GetMapping("/verifyCode")
    @ResponseBody
    public Result<Long> getSeckillverifyCodeerifyCode(HttpServletResponse response, User user,
                                                      @RequestParam("goodsId") long goodId) {
        //判断登录状态
        if (user == null) {
            return Result.errer(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = seckillService.createVerifyCode(user, goodId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.errer(CodeMsg.SECKILL_FAIL);
        }


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
