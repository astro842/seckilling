package com.astro.VO;

import com.astro.domain.Goods;
import lombok.Data;

import java.util.Date;

/**
 * Created by astro on 2018/2/15.
 */
@Data
public class GoodsVo extends Goods {

    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

}
