package com.astro.result;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import lombok.Data;
import lombok.Getter;

/**
 * Created by astro on 2018/2/14.
 */
@Getter
public enum  CodeMsg {
    //通用
    SUCCESS(0, "操作成功"),
    SERVER_ERROR(500100,"服务端异常"),
    REQUEST_ILLEGAL(500101,"请求非法"),
    ACCESS_LIMIT(500102 ,"访问太频繁"),
    //登录模块  5002XX
    SESSION_ERROR(500210,"session不存在"),
    PASSWORD_EMPTY(500211,"密码不能为空"),
    MOBILE_EMPTY(500212,"手机号不能为空"),
    MOBILE_ERROR(500213,"手机号格式错误"),
    MOBILE_NOT_EXIST(500213,"用户不存在"),
    PASSWORD_ERROR(500214,"密码错误"),


    //订单模块
    ORDER_ORDER_EXIST(500400,"订单不存在"),

    //秒杀模块 5005XX
    SECKILL_OVER(500500,"商品秒杀完毕"),
    SECKILL_REPEATE(500501,"重复秒杀"),
    SECKILL_FAIL(500502,"秒杀失败"),
    VERIFYCODE_ERROR(500503,"验证码错误")
    ;

    private int code;
    private String msg;

    private CodeMsg(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public static CodeMsg stateOf(int state){
        for (CodeMsg stateEnum: values()){
            if (stateEnum.getCode() == state){
                return stateEnum;
            }
        }
        return null;
    }


}
