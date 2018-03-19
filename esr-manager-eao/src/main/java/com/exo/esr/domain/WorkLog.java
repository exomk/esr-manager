
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

import com.exo.esr.entity.WorkLogEntity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class WorkLog implements Serializable {

    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    private int id;
    private String date;
    private String time;
    private Timestamp timestamp;
    private Device device;
    private ERegisterType registerType;

    public WorkLog(int id, String date, String time, Timestamp timestamp, Device device) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.timestamp = timestamp;
        this.device = device;
    }

    public WorkLog(WorkLogEntity workLogEntity) {
        this.id = workLogEntity.getId();
        this.timestamp = workLogEntity.getTimestamp();
        this.date = sdfDate.format(timestamp);
        this.time = sdfTime.format(timestamp);
        this.device = new Device(workLogEntity.getDeviceEntity());
        this.registerType = workLogEntity.getRegisterType();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public ERegisterType getRegisterType() {
        return registerType;
    }

    public void setRegisterType(ERegisterType registerType) {
        this.registerType = registerType;
    }
}
