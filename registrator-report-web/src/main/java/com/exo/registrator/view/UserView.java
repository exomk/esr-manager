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

package com.exo.registrator.view;

import com.exo.registrator.domain.ERole;
import com.exo.registrator.domain.User;
import com.exo.registrator.service.IRegistrationDbEao;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by atanasko on 11/21/17.
 */
@ManagedBean(name = "userView")
@ViewScoped
public class UserView implements IRegistratorView {

    private User selectedUser;
    private List<User> userList;
    private List<User> selectedUserList = new ArrayList<>();

    @EJB
    private IRegistrationDbEao registrationDbEao;

    @PostConstruct
    public void init() {
        RequestContext.getCurrentInstance().execute(JS_ROW_EDITOR);
        userList = registrationDbEao.getUserList();
        selectedUserList.addAll(userList.stream().collect(Collectors.toList()));
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void addUser() {
        RequestContext requestContext;

        userList.add(0, new User(0, "", "", "", ""));
        requestContext = RequestContext.getCurrentInstance();
        requestContext.execute(JS_ADD);
    }

    public void deleteUser() {
        if (selectedUser != null) {
            userList.removeIf(user -> user.getId() == selectedUser.getId());
            registrationDbEao.deleteUser(selectedUser.getId());
        }
        RequestContext.getCurrentInstance().execute(JS_ROW_EDITOR);
    }

    /**
     * Return array of roles.
     *
     * @return Array of roles.
     */
    public ERole[] getRoles() {
        return ERole.values();
    }

    public void onRowEdit(RowEditEvent event) {
        User user;
        ExternalContext externalContext;

        try {
            RequestContext.getCurrentInstance().execute(JS_ROW_EDITOR);

            user = (User) event.getObject();

            registrationDbEao.updateUser(user);
            externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowCancel(RowEditEvent event) {
        User user;
        ExternalContext externalContext;

        try {
            RequestContext.getCurrentInstance().execute(JS_ROW_EDITOR);

            user = (User) event.getObject();
            if (user.getId() == 0) {
                userList.remove(0);

                externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
