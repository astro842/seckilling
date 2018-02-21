package com.astro.domain;

import lombok.Data;

import java.util.Date;

/**
 * Created by astro on 2018/2/15.
 */
@Data
public class SeckillGoods {

    private Long id;
    private Long goodsId;
    private Double skillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
