package com.astro.VO;

import com.astro.domain.Goods;
import com.astro.domain.OrderInfo;
import com.astro.domain.User;
import lombok.Data;

/**
 * Created by astro on 2018/2/21.
 */
@Data
public class OrderDetailVo {


    private GoodsVo goods;
    private OrderInfo order;
    private User user;
}
