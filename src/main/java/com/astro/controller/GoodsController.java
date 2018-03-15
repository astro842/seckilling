package com.astro.controller;


import com.astro.Util.ValidatorUtil;
import com.astro.VO.GoodsDetailVo;
import com.astro.VO.GoodsVo;
import com.astro.VO.LoginVO;
import com.astro.dao.GoodsDao;
import com.astro.domain.Goods;
import com.astro.domain.User;
import com.astro.redis.GoodsKey;
import com.astro.redis.RedisService;
import com.astro.result.CodeMsg;
import com.astro.result.Result;
import com.astro.service.GoodsService;
import com.astro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by astro on 2018/2/13.
 */
@Controller
@RequestMapping("goods")
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    ApplicationContext applicationContext;

    // UserKey:tkb8d1e312f32840449526a6ec661eb141
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response,
                         Model model, User user) {

        model.addAttribute("user", user);
        //先查redis有没缓存页面
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        log.info("---------------第一次加入goodsList缓存");
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        log.info("---------------goodsVoList:" + goodsVoList);
        model.addAttribute("goodsVoList", goodsVoList);
        //return "goods_list";

        //手动渲染
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);

        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }


    @RequestMapping(value = "/to_detail/{goodsid}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response,
                                        Model model, User user, @PathVariable("goodsid") long goodId) {

        GoodsVo good = goodsService.getGoodsVoByGoodsId(goodId);
        long startAt = good.getStartDate().getTime();
        long endAt = good.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        if (now < startAt) {
            seckillStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
            remainSeconds = 0;
        }

        GoodsDetailVo goodsDetailVo=new GoodsDetailVo();
        goodsDetailVo.setGoods(good);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setSeckillStatus(seckillStatus);
        goodsDetailVo.setUser(user);

        return Result.success(goodsDetailVo);
    }


    @RequestMapping(value = "/to_detail2/{goodid}", produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response,
                         Model model, User user, @PathVariable("goodid") long goodId) {

        model.addAttribute("user", user);
        //先查redis有没缓存页面
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        GoodsVo good = goodsService.getGoodsVoByGoodsId(goodId);
        model.addAttribute("goods", good);
        long startAt = good.getStartDate().getTime();
        long endAt = good.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        if (now < startAt) {
            seckillStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        //return "goods_detail";

        //手动渲染
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_Detail", ctx);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail, ""+goodId, html);
        }
        return html;
    }

}

//   @RequestMapping("/to_list")
//    public String toLogin(HttpServletResponse response,Model model, @CookieValue(value=UserService.COOKI_NAME_TOKEN,required = false) String cookieToken,
//                          @RequestParam(value=UserService.COOKI_NAME_TOKEN ,required = false) String paramToken){
//       if (StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
//           return "login";
//       }
//       String token =StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//       User user=userService.getByToken(response,token);
//       model.addAttribute("user",user);
//       return "goods_list";
//   }

