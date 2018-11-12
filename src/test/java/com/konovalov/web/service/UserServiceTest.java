package com.konovalov.web.service;

import com.konovalov.web.domain.User;
import com.konovalov.web.domain.UserRole;
import com.konovalov.web.repository.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private MailSender mailSender;


    @Test
    public void loadUserByUsername() {

    }

    @Test
    public void addUser() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("some@mail.ru");

        boolean isUserCreate = userService.addUser(user);
        Assert.assertTrue(isUserCreate);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(UserRole.ANONYMOUS)));
        //проверка на вызов метода
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void addUserFailTest() {
        User user = new User();
        user.setUsername("test");
        //подсовываем в базу уже юзера с таким именем
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("test");

        boolean isUserCreate = userService.addUser(user);
        Assert.assertFalse(isUserCreate);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test(expected = NullPointerException.class)
    public void addUserExceptionTest() {
        User user = new User();
        boolean isUserCreate = userService.addUser(user);
    }

    @Test
    public void activateUser() {
        User user = new User();
        user.setActivationCode("activate");
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ANONYMOUS);
        user.setRoles(roles);

        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(UserRole.USER)));
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }
}