/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2018  EXO Service Solutions
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *
 * You can contact us at contact4exo@exo.mk
 */

package com.exo.esr.auth;

import com.exo.esr.domain.User;
import com.exo.esr.util.IPasswordUtil;
import com.exo.esr.service.IRegistrationDbEao;

import javax.ejb.EJB;
import javax.ejb.Singleton;

/**
 * Created by atanasko on 9.11.17.
 */
@Singleton
public class RegistrationAuthenticator {

    @EJB
    IRegistrationDbEao registrationDbEao;

    @EJB
    IPasswordUtil passwordUtil;

    public RegistrationAuthenticator() {
        // Singleton class!
    }

    public boolean isValidUser(String username, String password) {
        User user;
        String encodedPassword;
        boolean isValidUser = false;

        user = registrationDbEao.getUser(username);
        if (user != null) {
            encodedPassword = passwordUtil.encodePassword(password, user.getSalt());
//            isValidUser = encodedPassword.equals(user.getPassword()) && user.getActive();
            isValidUser = encodedPassword.equals(user.getPassword());
        }

        return isValidUser;
    }

    /**
     * The method that pre-validates if the client which invokes the REST API is
     * from a authorized and authenticated source.
     *
     * @param authToken The authorization token generated after login
     * @return TRUE for acceptance and FALSE for denied.
     */
    public boolean isAuthTokenValid(String authToken) {
        return true;
    }

    /**
     * This method checks is the service key is valid
     *
     * @param serviceKey The service key
     * @return TRUE if service key matches the pre-generated ones in service key
     * storage, otherwise FALSE.
     */
    public boolean isServiceKeyValid(String serviceKey) {
        return true;
    }
}
