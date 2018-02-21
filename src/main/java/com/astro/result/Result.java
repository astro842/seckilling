package com.astro.result;

import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.Data;

import java.time.Year;

/**
 * Created by astro on 2018/2/14.
 */
@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    //成功
    private Result(T data){
        this.code=0;
        this.msg="success";
        this.data=data;
    }
    //失败
    private Result(CodeMsg cm){
        if (cm==null){
            return;
        }
        this.code=cm.getCode();
        this.msg=cm.getMsg();
    }
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    public static <T> Result<T> errer(CodeMsg cm){
        return new Result<T>(cm);
    }
}
