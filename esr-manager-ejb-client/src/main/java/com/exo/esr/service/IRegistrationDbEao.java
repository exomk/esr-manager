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

package com.exo.esr.service;

import com.exo.esr.domain.Device;
import com.exo.esr.domain.User;
import com.exo.esr.domain.WorkLog;
import com.exo.esr.entity.UserEntity;

import javax.ejb.Remote;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by atanasko on 8.11.17.
 */
@Remote
public interface IRegistrationDbEao {

    User getUser(Integer id);

    User getUser(String username);

    UserEntity getUser(String username, String password);

    List<User> getUserList();

    List<User> getUserList(int month);

    List<User> getLoggedUserPerDayList(LocalDate date);

    List<User> getLoggedUserPerMonthList(int month);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(Integer id);

    List<Device> getDeviceList();

    Device getDevice(Integer id);

    void addDevice(Device device);

    void updateDevice(Device device);

    void deleteDevice(Integer id);

    List<WorkLog> getWorkLogList();

    List<WorkLog> getWorkLogList(User user, Date fromDate, Date toDate);

    void addWorkLog(User user, Timestamp timestamp);

}
