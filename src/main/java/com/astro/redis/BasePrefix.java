package com.astro.redis;

/**
 * Created by astro on 2018/2/14.
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String predix;

    public BasePrefix(int expireSeconds, String predix) {
        this.expireSeconds = expireSeconds;
        this.predix = predix;
    }

    public BasePrefix(String prefix){
        this(0,prefix);
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className=getClass().getSimpleName();
        return className+":"+predix;
    }
}
