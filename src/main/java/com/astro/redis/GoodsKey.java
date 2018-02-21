package com.astro.redis;

import static com.astro.redis.UserKey.TOKEN_EXPIRE;

/**
 * Created by astro on 2018/2/14.
 */
public class GoodsKey extends BasePrefix{

    public GoodsKey(int expireSeconds,String predix) {
        super(expireSeconds,predix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(600,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(600,"gd");
}
