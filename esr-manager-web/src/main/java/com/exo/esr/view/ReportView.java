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

import com.exo.esr.domain.ERole;
import com.exo.esr.domain.User;
import com.exo.esr.domain.WorkLog;
import com.exo.esr.service.IRegistrationDbEao;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by atanasko on 11/09/17.
 */
@ManagedBean(name = "reportView")
@ViewScoped
public class ReportView implements Serializable, IESRManagerView {

    private User user;

    private User selectedUser;

    private List<User> userList;

    private Date fromDate;

    private Date toDate;

    private List<WorkLog> workLogList = new ArrayList<>();

    private Date logDate;

    @EJB
    private IRegistrationDbEao registrationDbEao;

    @PostConstruct
    public void init() {
        userList = registrationDbEao.getUserList();
        selectedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        workLogList = registrationDbEao.getWorkLogList(selectedUser, fromDate, toDate);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public List<WorkLog> getWorkLogList() {
        return workLogList;
    }

    public void onChange() {
        workLogList = registrationDbEao.getWorkLogList(selectedUser, fromDate, toDate);
    }

    public boolean getIsUserRole() {
        return selectedUser != null && selectedUser.getRole() == ERole.USER;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public void logTime() {
        ExternalContext externalContext;

        try {
            registrationDbEao.addWorkLog(selectedUser, new Timestamp(logDate.getTime()));
            externalContext = FacesContext.getCurrentInstance().getExternalContext();

            externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
