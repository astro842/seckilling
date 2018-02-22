package com.astro.controller;


import com.astro.Util.ValidatorUtil;
import com.astro.VO.LoginVO;
import com.astro.domain.User;
import com.astro.redis.RedisService;
import com.astro.redis.UserKey;
import com.astro.result.CodeMsg;
import com.astro.result.Result;

import com.astro.service.UserService;
import com.sun.xml.internal.ws.util.Pool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


/**
 * Created by astro on 2018/2/13.
 */
@Controller
@RequestMapping("login")
@Slf4j
public class LoginController {

   @Autowired
   private RedisService redisService;
   @Autowired
   private UserService userService;

//   @RequestMapping("/to_login")
//    public String toLogin(){
//       return "login";
//   }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response,LoginVO loginVO){
        log.info(loginVO.toString());
        //校验参数
        String password=loginVO.getPassword();
        String mobile=loginVO.getMobile();
        if (StringUtils.isEmpty(password)){
            return Result.errer(CodeMsg.PASSWORD_EMPTY);
        }
        if (StringUtils.isEmpty(mobile)){
            return Result.errer(CodeMsg.MOBILE_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.errer(CodeMsg.MOBILE_ERROR);
        }

        //登录
        CodeMsg cm = userService.login(response,loginVO);
        if (cm.getCode()==0){
            return Result.success(true);
        }else {
            return Result.errer(cm);
        }

//        String token = userService.login(response,loginVO);
//        return Result.success(token);
    }

}
