package com.astro.controller;


import com.astro.VO.GoodsVo;
import com.astro.domain.User;
import com.astro.result.Result;
import com.astro.service.GoodsService;
import com.astro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Created by astro on 2018/2/13.
 */
@Controller
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<User> info(Model model, User user) {

        return Result.success(user);
    }

}
