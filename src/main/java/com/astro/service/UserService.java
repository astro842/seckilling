package com.astro.service;

import com.astro.Util.MD5Util;
import com.astro.Util.UUIDUtil;
import com.astro.VO.LoginVO;
import com.astro.dao.User1Dao;
import com.astro.dao.UserDao;
import com.astro.domain.User;
import com.astro.domain.User1;
import com.astro.redis.RedisService;
import com.astro.redis.UserKey;
import com.astro.result.CodeMsg;
import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by astro on 2018/2/12.
 */
@Service
@Slf4j
public class UserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService redisService;

    public User getById(long i) {
        return userDao.getById(1);
    }

    public CodeMsg login(HttpServletResponse response, LoginVO loginVO) {
        if (loginVO == null) {
            return CodeMsg.SERVER_ERROR;
        }
        String mobile = loginVO.getMobile();
        String formPass = loginVO.getPassword();
        //判断手机号是否存在
        User user = userDao.getById(Long.parseLong(mobile));
        if (user == null) {
            return CodeMsg.MOBILE_NOT_EXIST;
        }
        String dbPass = user.getPassword();
        log.info("------------------daPass:" + dbPass);
        String saltDb = user.getSalt();
        log.info("------------------saltDb:" + saltDb);
        String validataPass = MD5Util.FormPassToDbPass(formPass, saltDb);
        log.info("------------------validataPass:" + validataPass);
        if (!validataPass.equals(dbPass)) {
            return CodeMsg.PASSWORD_ERROR;
        }


        //用户登录成功后  生成token返回去给客户端
        //生成cookis
        addCookie(response, user);

        return CodeMsg.SUCCESS;
    }

    private void addCookie(HttpServletResponse response, User user) {
        String token = UUIDUtil.uuid();
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //重新addcookie 延长有效期
        if (user != null){
            addCookie(response,user);
        }
            return user;
    }
}
