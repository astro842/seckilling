package com.astro.redis;

/**
 * Created by astro on 2018/2/14.
 */
public class UserKey extends BasePrefix{


    public static final  int TOKEN_EXPIRE=3600*24*2;

    public static UserKey token = new UserKey(TOKEN_EXPIRE,"tk");

    public UserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }



}
