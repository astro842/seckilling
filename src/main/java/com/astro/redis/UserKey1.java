package com.astro.redis;

/**
 * Created by astro on 2018/2/14.
 */
public class UserKey1 extends BasePrefix{


    public UserKey1(String prefix) {
        super(prefix);
    }

    public static UserKey1 getById = new UserKey1("id");
    public static UserKey1 getByName = new UserKey1("name");
}
