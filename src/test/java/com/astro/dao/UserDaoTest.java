package com.astro.dao;

import com.astro.domain.User;
import com.astro.domain.User1;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by astro on 2018/2/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserDaoTest {

    @Autowired
    private User1Dao userDao;

    @Test
    public void getById() throws Exception {

        User1 id = userDao.getById(2);
        System.out.println(id);
    }

}