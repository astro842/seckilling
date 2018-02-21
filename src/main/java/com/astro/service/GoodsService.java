package com.astro.service;

import com.astro.VO.GoodsVo;
import com.astro.dao.GoodsDao;
import com.astro.domain.Goods;
import com.astro.domain.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by astro on 2018/2/15.
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> getGoodsVoList(){
        return goodsDao.getGoodsVoList();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodId) {
        return goodsDao.getGoodsVoById(goodId);
    }

    public void reduceStock(GoodsVo goods) {
        SeckillGoods g=new SeckillGoods();
        g.setId(goods.getId());
        goodsDao.reduceStock(g);
    }
}
