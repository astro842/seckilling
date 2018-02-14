package com.astro.dao;

import com.astro.domain.User;
import com.astro.domain.User1;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * Created by astro on 2018/2/12.
 */
@Mapper
public interface UserDao {

    @Select("select * from user where id=#{id}")
    public User getById(@Param("id") long id);
}
