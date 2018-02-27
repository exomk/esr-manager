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

import com.exo.esr.domain.ERole;
import com.exo.esr.domain.WorkLog;
import com.exo.esr.entity.WorkLogEntity;
import com.exo.esr.domain.User;
import com.exo.esr.entity.DeviceEntity;
import com.exo.esr.util.IPasswordUtil;
import com.exo.esr.domain.Device;
import com.exo.esr.entity.UserEntity;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by atanasko on 8.11.17.
 */
@Stateless
@LocalBean
public class RegistrationDbEaoImpl implements IRegistrationDbEao {

    @PersistenceContext(name = "registrationPU")
    private EntityManager entityManager;

    @EJB
    IPasswordUtil passwordUtil;

    @Override
    public User getUser(Integer id) {
        UserEntity userEntity;

        userEntity = entityManager.find(UserEntity.class, id);

        return new User(userEntity);
    }

    @Override
    public User getUser(String username) {
        Query query;
        UserEntity userEntity;
        User user = null;

        try {
            query = entityManager.createQuery("FROM UserEntity as user WHERE user.username =:username", UserEntity.class);
            query.setParameter("username", username);
            userEntity = (UserEntity) query.getSingleResult();

            if (userEntity != null) {
                user = new User(userEntity);
            }
        } catch (Exception e) {
            // No entity found for query!
//            e.printStackTrace();
        }

        return user;
    }

    public UserEntity getUser(String username, String password) {
        Query query;
        UserEntity userEntity;

        query = entityManager.createQuery("FROM UserEntity as user WHERE user.username =:username AND user.password =:password", UserEntity.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        userEntity = (UserEntity) query.getSingleResult();

        return userEntity;
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        List<UserEntity> userEntityList;

        try {
            userEntityList = entityManager.createQuery("FROM UserEntity as user", UserEntity.class).getResultList();
            userList.addAll(userEntityList.stream().filter(userEntity -> !"admin".equals(userEntity.getUsername())).map(User::new).collect(Collectors.toList()));
        } catch (Exception e) {
            // No entity found for query!
//            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public List<User> getUserList(int month) {
        Query query;
        List<User> userList = new ArrayList<>();
        List<UserEntity> userEntityList;
        LocalDate now = LocalDate.now();
        LocalDate endDate;

        try {
            endDate = now.withMonth(month).withDayOfMonth(now.withMonth(month).lengthOfMonth());

            query = entityManager.createQuery("FROM UserEntity AS user WHERE DATE(user.date) <=:endDate");
            query.setParameter("endDate", endDate);
            userEntityList = query.getResultList();
            userList.addAll(userEntityList.stream().filter(userEntity -> !"admin".equals(userEntity.getUsername())).map(User::new).collect(Collectors.toList()));
        } catch (Exception e) {
            // No entity found for query!
            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public List<User> getLoggedUserPerDayList(LocalDate date) {
        Query query;
        List<User> userList = new ArrayList<>();
        List<UserEntity> userEntityList;

        try {
            query = entityManager.createQuery("FROM UserEntity as user where user.id in (" +
                    "SELECT DISTINCT workLog.deviceEntity.userEntity.id " +
                    "FROM WorkLogEntity AS workLog " +
                    "WHERE DATE(workLog.timestamp) =:date)");
            query.setParameter("date", date);
            userEntityList = query.getResultList();
            userList.addAll(userEntityList.stream().filter(userEntity -> !"admin".equals(userEntity.getUsername())).map(User::new).collect(Collectors.toList()));
        } catch (Exception e) {
            // No entity found for query!
//            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public List<User> getLoggedUserPerMonthList(int month) {
        Query query;
        List<User> userList = new ArrayList<>();
        List<UserEntity> userEntityList;
        LocalDate now = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = now.withMonth(month).withDayOfMonth(1);
            endDate = now.withMonth(month).withDayOfMonth(now.withMonth(month).lengthOfMonth());

            query = entityManager.createQuery("FROM UserEntity as user where user.id in (" +
                    "SELECT DISTINCT workLog.deviceEntity.userEntity.id " +
                    "FROM WorkLogEntity AS workLog " +
                    "WHERE DATE(workLog.timestamp) >:startDate AND DATE(workLog.timestamp) <:endDate)");
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            userEntityList = query.getResultList();
            userList.addAll(userEntityList.stream().filter(userEntity -> !"admin".equals(userEntity.getUsername())).map(User::new).collect(Collectors.toList()));
        } catch (Exception e) {
            // No entity found for query!
//            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public void updateUser(User user) {
        String salt;
        String encodedPassword;
        UserEntity userEntity;

        if (user.getId() == 0) {
            userEntity = new UserEntity();
        } else {
            userEntity = entityManager.find(UserEntity.class, user.getId());
        }

        userEntity.setUsername(user.getUsername());
        salt = passwordUtil.generateSalt();
        encodedPassword = passwordUtil.encodePassword(user.getPassword(), salt);
        userEntity.setSalt(salt);
        userEntity.setPassword(encodedPassword);
        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());
        userEntity.setDate(new java.sql.Date(System.currentTimeMillis()));
        userEntity.setRole(user.getRole());

        entityManager.persist(userEntity);
    }

    @Override
    public void deleteUser(Integer id) {
        UserEntity userEntity;

        userEntity = entityManager.find(UserEntity.class, id);
        if (userEntity != null) {
            entityManager.remove(userEntity);
        }
    }

    @Override
    public List<Device> getDeviceList() {
        List<Device> deviceList = new ArrayList<>();
        List<DeviceEntity> deviceEntityList;

        try {
            deviceEntityList = entityManager.createQuery("FROM DeviceEntity as device", DeviceEntity.class).getResultList();
            deviceList.addAll(deviceEntityList.stream().map(Device::new).collect(Collectors.toList()));
        } catch (Exception e) {
            // No entity found for query!
//            e.printStackTrace();
        }

        return deviceList;
    }

    @Override
    public Device getDevice(Integer userID) {
        Query query;
        DeviceEntity deviceEntity;
        Device device = null;

        try {
            query = entityManager.createQuery("FROM DeviceEntity as device WHERE device.userEntity.id =:user_id", DeviceEntity.class);
            query.setParameter("user_id", userID);
            deviceEntity = (DeviceEntity) query.getSingleResult();

            if (deviceEntity != null) {
                device = new Device(deviceEntity);
            }
        } catch (Exception e) {
            // No entity found for query!
//            e.printStackTrace();
        }

        return device;
    }

    @Override
    public void addDevice(Device device) {

    }

    @Override
    public void updateDevice(Device device) {
        DeviceEntity deviceEntity;
        UserEntity userEntity;

        if (device.getId() == 0) {
            deviceEntity = new DeviceEntity();
        } else {
            deviceEntity = entityManager.find(DeviceEntity.class, device.getId());
        }

        deviceEntity.setUid(device.getUid());
        deviceEntity.setDescription(device.getDescription());
        userEntity = entityManager.find(UserEntity.class, device.getUser().getId());
        deviceEntity.setUserEntity(userEntity);
        deviceEntity.setDate(new java.sql.Date(System.currentTimeMillis()));

        entityManager.persist(deviceEntity);
    }

    @Override
    public void deleteDevice(Integer id) {
        DeviceEntity deviceEntity;

        deviceEntity = entityManager.find(DeviceEntity.class, id);
        if (deviceEntity != null) {
            entityManager.remove(deviceEntity);
        }
    }

    @Override
    public List<WorkLog> getWorkLogList() {
        Query query;
        List<WorkLog> workLogList = new ArrayList<>();
        List<WorkLogEntity> workLogEntityList;

        query = entityManager.createQuery("FROM WorkLogEntity as workLog", WorkLogEntity.class);
        workLogEntityList = query.getResultList();
        workLogList.addAll(workLogEntityList.stream().map(WorkLog::new).collect(Collectors.toList()));

        return workLogList;
    }

    @Override
    public List<WorkLog> getWorkLogList(User user, Date fromDate, Date toDate) {
        Query query;
        String queryString;
        List<WorkLog> workLogList = new ArrayList<>();
        List<WorkLogEntity> workLogEntityList;

        queryString = "FROM WorkLogEntity as workLog";

        if (user != null && !(user.getRole() == ERole.ADMIN)) {
            queryString += " WHERE workLog.deviceEntity.userEntity.id =:id";
        }
        if (fromDate != null) {
            if (queryString.contains("WHERE")) {
                queryString += " AND  DATE_TRUNC('day', workLog.timestamp) >=:fromDate";
            } else {
                queryString += " WHERE  DATE_TRUNC('day', workLog.timestamp) >=:fromDate";
            }
        }
        if (toDate != null) {
            if (queryString.contains("WHERE")) {
                queryString += " AND  DATE_TRUNC('day', workLog.timestamp) <=:toDate";
            } else {
                queryString += " WHERE  DATE_TRUNC('day', workLog.timestamp) <=:toDate";
            }
        }

        query = entityManager.createQuery(queryString, WorkLogEntity.class);
        if (user != null && !(user.getRole() == ERole.ADMIN)) {
            query.setParameter("id", user.getId());
        }
        if (fromDate != null) {
            query.setParameter("fromDate", new Timestamp(fromDate.getTime()));
        }
        if (toDate != null) {
            query.setParameter("toDate", new Timestamp(toDate.getTime()));
        }

        workLogEntityList = query.getResultList();
        workLogList.addAll(workLogEntityList.stream().map(WorkLog::new).collect(Collectors.toList()));

        return workLogList;
    }

    @Override
    public void addWorkLog(User user, Timestamp timestamp) {
        UserEntity userEntity;
        DeviceEntity deviceEntity;
        WorkLogEntity workLogEntity = new WorkLogEntity();

        userEntity = entityManager.find(UserEntity.class, user.getId());
        deviceEntity = (DeviceEntity) userEntity.getDeviceEntityList().toArray()[0];
        workLogEntity.setDeviceEntity(deviceEntity);
        workLogEntity.setTimestamp(timestamp);

        entityManager.persist(workLogEntity);
    }
}
