package com.astro.redis;

/**
 * Created by astro on 2018/2/14.
 */
public class AccessKey extends BasePrefix{

    public AccessKey(int expireSeconds, String predix) {
        super(expireSeconds,predix);
    }

   // public static AccessKey accessKey = new AccessKey(5,"accessKey");

    public static AccessKey withExpire(int expireSeconds){
        return new AccessKey(expireSeconds,"accessKey");
    }

}
