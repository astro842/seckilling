package com.astro.Util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by astro on 2018/2/14.
 */
public class ValidatorUtil {

    private static final Pattern mobile_pattern=Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
       if (StringUtils.isEmpty(src)){
           return false;
       }
        Matcher m=mobile_pattern.matcher(src);
       return m.matches();
    }

//    public static void main(String[] args){
//        System.out.println(isMobile("11111111111"));
//        System.out.println(isMobile("1111111111"));
//    }
}
