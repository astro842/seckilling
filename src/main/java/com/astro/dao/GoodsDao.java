package com.astro.dao;

import com.astro.VO.GoodsVo;
import com.astro.domain.Goods;
import com.astro.domain.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by astro on 2018/2/15.
 */
@Mapper
public interface GoodsDao {

    @Select("select g.* ,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date from seckill_goods sg left join goods as g on sg.goods_id = g.id")
    public List<GoodsVo> getGoodsVoList();


    @Select("select  * from goods")
    public List<Goods> getGoods();


    @Select("select g.* ,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date from " +
            "seckill_goods sg left join goods as g on sg.goods_id = g.id where g.id=#{goodid}")
    GoodsVo getGoodsVoById(@Param("goodid") long goodId);

    @Update("update seckill_goods set stock_count= stock_count-1 where goods_id = #{goodsId} and stock_count>0")
    int reduceStock(SeckillGoods g);
}
