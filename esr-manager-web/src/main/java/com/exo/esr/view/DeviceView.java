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

package com.exo.esr.view;

import com.exo.esr.domain.Device;
import com.exo.esr.domain.User;
import com.exo.esr.service.IRegistrationDbEao;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by atanasko on 11/21/17.
 */
@ManagedBean(name = "deviceView")
@ViewScoped
public class DeviceView implements IESRManagerView {

    private Device selectedDevice;
    private List<Device> deviceList;
    private List<User> userList;

    @EJB
    private IRegistrationDbEao registrationDbEao;

    @PostConstruct
    public void init() {
        RequestContext.getCurrentInstance().execute(JS_ROW_EDITOR);
        deviceList = registrationDbEao.getDeviceList();
        userList = registrationDbEao.getUserList();
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setSelectedDevice(Device selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public Device getSelectedDevice() {
        return selectedDevice;
    }

    public void addDevice() {
        deviceList.add(0, new Device(0, "", ""));

        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute(JS_ADD);
    }

    public void deleteDevice() {
        if (selectedDevice != null) {
            deviceList.removeIf(user -> user.getId() == selectedDevice.getId());
            registrationDbEao.deleteDevice(selectedDevice.getId());
        }
        RequestContext.getCurrentInstance().execute(JS_ROW_EDITOR);
    }

    public void onRowEdit(RowEditEvent event) {
        Device device;
        ExternalContext externalContext;

        try {
            RequestContext.getCurrentInstance().execute(JS_ROW_EDITOR);

            device = (Device) event.getObject();
            registrationDbEao.updateDevice(device);
            externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Device device;
        ExternalContext externalContext;

        try {
            RequestContext.getCurrentInstance().execute(JS_ROW_EDITOR);

            device = (Device) event.getObject();
            if (device.getId() == 0) {
                deviceList.remove(0);

                externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
