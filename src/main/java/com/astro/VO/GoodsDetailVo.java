package com.astro.VO;

import com.astro.domain.Goods;
import com.astro.domain.User;
import lombok.Data;

/**
 * Created by astro on 2018/2/21.
 */
@Data
public class GoodsDetailVo {

    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private Goods goods;
    private User user;
}
