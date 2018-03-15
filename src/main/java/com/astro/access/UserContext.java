package com.astro.access;

import com.astro.domain.User;

/**
 * Created by astro on 2018/3/11.
 */
public class UserContext {


    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }

}
