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
    //登录模块
    SESSION_ERROR(500210,"session不存在"),
    PASSWORD_EMPTY(500211,"密码不能为空"),
    MOBILE_EMPTY(500212,"手机号不能为空"),
    MOBILE_ERROR(500213,"手机号格式错误"),
    MOBILE_NOT_EXIST(500213,"用户不存在"),
    PASSWORD_ERROR(500214,"密码错误")
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
