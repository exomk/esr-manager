
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

package com.exo.esr.domain;

import com.exo.esr.entity.DeviceEntity;

import java.io.Serializable;
import java.sql.Date;

public class Device implements Serializable {

    private Integer id;
    private String uid;
    private String description;
    private Date date;
    private User user;
    private Integer userId;

    public Device(Integer id, String uid, String description) {
        this.id = id;
        this.uid = uid;
        this.description = description;
    }

    public Device(DeviceEntity deviceEntity) {
        this.id = deviceEntity.getId();
        this.uid = deviceEntity.getUid();
        this.description = deviceEntity.getDescription();
        this.date = deviceEntity.getDate();
        this.user = new User(deviceEntity.getUserEntity());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (id == obj);
    }

    @Override
    public String toString() {
        return uid;
    }
}
