package com.astro.service;

import com.astro.Util.MD5Util;
import com.astro.Util.UUIDUtil;
import com.astro.VO.GoodsVo;
import com.astro.domain.OrderInfo;
import com.astro.domain.SeckillOrder;
import com.astro.domain.User;
import com.astro.redis.RedisService;
import com.astro.redis.SeckillKey;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by astro on 2018/2/16.
 */
@Service
@Slf4j
public class SeckillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;


    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods) {
        //减库存
        boolean b = goodsService.reduceStock(goods);
        if (b) {
            //写order_info  seckill_order
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }

    }

    public long getSeckillResult(Long userId, long goodId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodId);
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, "" + goodsId);
    }





    public boolean checkPath(User user, long goodId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathFromReids = redisService.get(SeckillKey.getSeckillPath, "" + user.getId() + "" + goodId, String.class);
        return path.equals(pathFromReids);
    }

    public String createPath(User user, long goodId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SeckillKey.getSeckillPath, "" + user.getId() + "" + goodId, str);
        return str;
    }

    public BufferedImage createVerifyCode(User user, long goodId) {
        if (user == null || goodId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码放进缓存中
        int rnd = calc(verifyCode);
        log.info("-----------verifyCode:" + verifyCode);
        log.info("-----------rnd:" + rnd);
        redisService.set(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodId, rnd);
        //输出图片
        return image;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine e = manager.getEngineByName("JavaScript");
            return (Integer) e.eval(exp);
        } catch (ScriptException e1) {
            e1.printStackTrace();
            return 0;
        }

    }

    //生成验证码
    private static char[] op = new char[]{'+', '-', '*'};

    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = op[rdm.nextInt(3)];
        char op2 = op[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(User user, long goodId, int verifyCode) {
        Integer verifyCodeFromRedis = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodId, Integer.class);
        if (verifyCodeFromRedis==null || verifyCode-verifyCodeFromRedis!=0){
            return false;
        }
        redisService.delete(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodId);
        return true;
    }

//    public static void main(String[] a){
//        System.out.println(calc("1+9*2"));
//    }
}
