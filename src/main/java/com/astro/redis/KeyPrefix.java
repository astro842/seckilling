package com.astro.redis;

/**
 * Created by astro on 2018/2/14.
 */
public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
