package com.astro.controller;


import com.astro.Util.ValidatorUtil;
import com.astro.VO.LoginVO;
import com.astro.domain.User;
import com.astro.redis.RedisService;
import com.astro.result.CodeMsg;
import com.astro.result.Result;
import com.astro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


/**
 * Created by astro on 2018/2/13.
 */
@Controller
@RequestMapping("goods")
@Slf4j
public class GoodsController {

   @Autowired
   private RedisService redisService;
   @Autowired
   private UserService userService;



    @RequestMapping("/to_list")
    public String toLogin(Model model,User user){
        model.addAttribute("user",user);
        return "goods_list";
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

