package com.astro.service;

import com.astro.dao.User1Dao;
import com.astro.domain.User1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by astro on 2018/2/12.
 */
@Service
public class User1Service {

    @Autowired
    private User1Dao userDao;

    public User1 getById(int i){
        return userDao.getById(1);
    }

}
