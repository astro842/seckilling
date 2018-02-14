package com.astro.dao;

import com.astro.domain.User1;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * Created by astro on 2018/2/12.
 */
@Mapper
public interface User1Dao {

    @Select("select * from user1 where id=#{id}")
    public User1 getById(@Param("id") int id);
}
