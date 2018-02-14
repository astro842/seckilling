package com.astro.Util;

import java.util.UUID;

/**
 * Created by astro on 2018/2/14.
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

//    public static void main(String[] args){
//        System.out.println(uuid());
//    }
}
