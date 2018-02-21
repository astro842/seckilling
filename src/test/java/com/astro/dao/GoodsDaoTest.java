package com.astro.dao;

import com.astro.VO.GoodsVo;
import com.astro.domain.Goods;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by astro on 2018/2/15.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GoodsDaoTest {

    @Autowired
    private GoodsDao goodsDao;

    @Test
    public void getGoodsVoList() throws Exception {
        List<GoodsVo> goodsVoList = goodsDao.getGoodsVoList();
        System.out.println(goodsVoList.get(0));
    }

    @Test
    public void getGoods() throws Exception {
        List<Goods> goodsList = goodsDao.getGoods();
        System.out.println(goodsList);
    }
    @Test
    public void getGoodsVoById() throws Exception {
        GoodsVo goodsVoById = goodsDao.getGoodsVoById(3);
        System.out.println(goodsVoById);
    }

}