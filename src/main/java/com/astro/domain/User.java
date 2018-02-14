package com.astro.domain;

import lombok.Data;

import java.util.Date;

/**
 * Created by astro on 2018/2/12.
 */
@Data
public class User {

    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
