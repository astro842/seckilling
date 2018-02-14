package com.astro.redis;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * Created by astro on 2018/2/14.
 */
public class OrderKey extends BasePrefix{

    public OrderKey(int expireSeconds, String predix) {
        super(expireSeconds, predix);
    }
}
