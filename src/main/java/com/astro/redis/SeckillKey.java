package com.astro.redis;

/**
 * Created by astro on 2018/2/14.
 */
public class SeckillKey extends BasePrefix{

    public SeckillKey(String predix) {
        super(predix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey("isGover");

}
