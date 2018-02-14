package com.astro.controller;


import com.astro.domain.User;
import com.astro.domain.User1;
import com.astro.redis.UserKey;
import com.astro.redis.UserKey1;
import com.astro.result.Result;
import com.astro.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by astro on 2018/2/13.
 */
@Controller
@RequestMapping("demo")
public class SampleController {

   @Autowired
   private RedisService redisService;

   @RequestMapping("/redis/get")
   @ResponseBody
   public Result<User> redisGet(){
       User user=redisService.get(UserKey1.getById,""+1,User.class);
       return Result.success(user);
   }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User1 user=new User1();
        user.setId(2);
        user.setName("嘻嘻哈哈嘻嘻哈哈");
        redisService.set(UserKey1.getById,""+1,user);
        return Result.success(true);
    }

}
