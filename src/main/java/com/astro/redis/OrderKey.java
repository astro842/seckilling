package com.astro.redis;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * Created by astro on 2018/2/14.
 */
public class OrderKey extends BasePrefix{

    public OrderKey(String predix) {
        super( predix);
    }

    public static OrderKey getSeckillOrderByUidGid = new OrderKey("seckillUidGid");


}
