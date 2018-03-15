package com.astro.redis;

/**
 * Created by astro on 2018/2/14.
 */
public class SeckillKey extends BasePrefix{

    public SeckillKey(int expireSeconds,String predix) {
        super(predix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey(0,"isGover");
    public static SeckillKey getSeckillPath = new SeckillKey(60,"getSeckillPath");
    public static SeckillKey getSeckillVerifyCode = new SeckillKey(100,"getSeckillVerifyCode");

}
