package com.konovalov.web.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Test
    public void getByUserName() {
        boolean isByUser = userService.getByUserName("admin");
        Assert.assertTrue(isByUser);
    }
}