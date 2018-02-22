package com.astro.rabbitmq;

import com.astro.domain.User;
import lombok.Data;

/**
 * Created by astro on 2018/2/22.
 */
@Data
public class SeckillMsg {

    private User user;
    private long goodsId;
}
