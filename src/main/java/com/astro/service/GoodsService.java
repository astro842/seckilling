package com.astro.service;

import com.astro.VO.GoodsVo;
import com.astro.dao.GoodsDao;
import com.astro.domain.Goods;
import com.astro.domain.SeckillGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * Created by astro on 2018/2/15.
 */
@Service
@Slf4j
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> getGoodsVoList() {
        return goodsDao.getGoodsVoList();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodId) {
        return goodsDao.getGoodsVoById(goodId);
    }

    public boolean reduceStock(GoodsVo goods) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());
        int i = goodsDao.reduceStock(g);
        log.info("--------------goodsId："+g);
        log.info("--------------减库存："+i);
        return i > 0;
    }
}
