package com.astro.redis;

/**
 * Created by astro on 2018/2/14.
 */
public class UserKey extends BasePrefix{


    public static final  int TOKEN_EXPIRE=3600*24*2;

    //登录
    public static UserKey token = new UserKey(120,"userToken");
    public static UserKey getById = new UserKey(0,"id");

    public UserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }



}
