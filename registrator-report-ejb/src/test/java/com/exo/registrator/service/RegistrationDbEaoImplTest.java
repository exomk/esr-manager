package com.exo.registrator.service;

import com.exo.registrator.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by damjan on 12.1.18.
 */
public class RegistrationDbEaoImplTest {

    private RegistrationDbEaoImpl registrationDbEaoMock;

    @Before
    public void setUp() {
        User user1;
        User user2;
        List<User> userList = new ArrayList<>();

        registrationDbEaoMock = mock(RegistrationDbEaoImpl.class);

        user1 = new User(1, "Alfred", "Alfred", "Alfred", "Alfred");
        user2 = new User(2, "Test", "test123", "Test", "Test");
        userList.add(user1);
        userList.add(user2);

        when(registrationDbEaoMock.getUser(1)).thenReturn(user1);
        when(registrationDbEaoMock.getUserList()).thenReturn(userList);

    }

    @Test
    public void testGetUser() {

        assertEquals("Alfred", registrationDbEaoMock.getUser(1).getUsername());

        verify(registrationDbEaoMock).getUser(1);
    }

    @Test
    public void testGetUserList() {
        List<User> userList;

        userList = registrationDbEaoMock.getUserList();

        //not ok
        assertEquals(2, userList.size());

    }

}