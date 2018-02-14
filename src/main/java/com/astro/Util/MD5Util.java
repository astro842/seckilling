package com.astro.Util;

import org.apache.commons.codec.digest.DigestUtils;
import sun.applet.Main;

/**
 * Created by astro on 2018/2/14.
 */
public class MD5Util {

    private static final String salt="1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }


    public static String inputPassToFormPass(String inputPass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String FormPassToDbPass(String inputPass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String input,String saltDB){
        String FormPass=inputPassToFormPass(input);
        String dbPass=FormPassToDbPass(FormPass,saltDB);
        return dbPass;
    }

    public static void main(String[] args){
        System.out.println(inputPassToDbPass("123456","1a2b3c4d"));
    }

    //d3b1294a61a07da9b49b6e22b2cbd7f9
    //d3b1294a61a07da9b49b6e22b2cbd7f9

    //daPass:17831d20a8eb781923856d66d599dba3
    //validataPass:b7797cce01b4b131b433b6acf4add449
}
