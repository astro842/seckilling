package com.astro.domain;

import lombok.Data;

/**
 * Created by astro on 2018/2/15.
 */
@Data
public class SeckillOrder {
    private Long id;
    private Long orderId;
    private Long userId;
    private Long goodsId;
}
