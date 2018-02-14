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

package com.exo.registrator.util;

import javax.ejb.Singleton;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by atanasko on 9.11.17.
 */
@Singleton
public class PasswordUtil implements IPasswordUtil {

    @Override
    public String generateSalt() {
        SecureRandom secureRandom;
        byte[] salt = new byte[16];

        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return new BigInteger(salt).toString(16);
    }

    @Override
    public String encodePassword(String password, String salt) {
        String encodedPassword = null;
        MessageDigest messageDigest;
        byte[] bytes;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt.getBytes());
            bytes = messageDigest.digest(password.getBytes());
            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encodedPassword = stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return encodedPassword;
    }
}
